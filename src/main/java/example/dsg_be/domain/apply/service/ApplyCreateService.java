package example.dsg_be.domain.apply.service;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.domain.MealType;
import example.dsg_be.domain.apply.exception.AlreadyAppliedException;
import example.dsg_be.domain.apply.presentation.dto.request.ApplyCreateRequest;
import example.dsg_be.domain.apply.presentation.dto.response.ApplyCreateResponse;
import example.dsg_be.domain.apply.repository.ApplyRepository;
import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.exception.TeacherNotFoundException;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ApplyCreateService {

    private static final LocalTime LUNCH_START = LocalTime.of(6, 40);
    private static final LocalTime DINNER_START = LocalTime.of(13, 30);

    private final ApplyRepository applyRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public ApplyCreateResponse execute(ApplyCreateRequest request) {
        TeacherEntity teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> TeacherNotFoundException.EXCEPTION);

        MealType meal = resolveMealType(request.getMeal());

        LocalDate today = LocalDate.now();
        boolean alreadyApplied = applyRepository.existsByTeacherAndMealAndCreatedAtBetween(
                teacher,
                meal,
                today.atStartOfDay(),
                today.atTime(LocalTime.MAX)
        );

        if (alreadyApplied) {
            throw AlreadyAppliedException.EXCEPTION;
        }

        ApplyEntity applyEntity = ApplyEntity.builder()
                .teacher(teacher)
                .meal(meal)
                .reason(request.getReason())
                .build();

        ApplyEntity saved = applyRepository.save(applyEntity);
        return new ApplyCreateResponse(saved);
    }

    private MealType resolveMealType(MealType requested) {
        if (requested != null) {
            return requested;
        }

        LocalTime now = LocalTime.now();

        if (now.isAfter(LUNCH_START) && now.isBefore(DINNER_START)) {
            return MealType.LUNCH;
        }

        if (now.isAfter(DINNER_START)) {
            return MealType.DINNER;
        }

        return MealType.LUNCH;
    }
}
