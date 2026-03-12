package example.dsg_be.domain.apply.service;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.exception.AlreadyAppliedException;
import example.dsg_be.domain.apply.presentation.dto.request.ApplyCreateRequest;
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

    private final ApplyRepository applyRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public void execute(ApplyCreateRequest request) {
        TeacherEntity teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> TeacherNotFoundException.EXCEPTION);

        LocalDate today = LocalDate.now();
        boolean alreadyApplied = applyRepository.existsByTeacherAndMealAndCreatedAtBetween(
                teacher,
                request.getMeal(),
                today.atStartOfDay(),
                today.atTime(LocalTime.MAX)
        );

        if (alreadyApplied) {
            throw AlreadyAppliedException.EXCEPTION;
        }

        ApplyEntity applyEntity = ApplyEntity.builder()
                .teacher(teacher)
                .meal(request.getMeal())
                .reason(request.getReason())
                .build();

        applyRepository.save(applyEntity);
    }
}
