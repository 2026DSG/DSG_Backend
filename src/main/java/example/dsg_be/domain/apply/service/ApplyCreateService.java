package example.dsg_be.domain.apply.service;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.domain.MealType;
import example.dsg_be.domain.apply.exception.AlreadyAppliedException;
import example.dsg_be.domain.apply.presentation.dto.request.*;
import example.dsg_be.domain.apply.presentation.dto.response.*;
import example.dsg_be.domain.apply.presentation.dto.response.ApplyCreateResponse;
import example.dsg_be.domain.apply.repository.ApplyRepository;
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

    @Transactional
    public ApplyCreateResponse create(ApplyCreateRequest request) {
        MealType mealType = resolveMealType(request.getMealType());

        if (applyRepository.existsByApplyDateAndMealType(LocalDate.now(), mealType)) {
            throw new AlreadyAppliedException();
        }

        ApplyEntity apply = ApplyEntity.builder()
                .mealType(mealType)
                .reason(request.getReason())
                .staffName(request.getStaffName())
                .department(request.getDepartment())
                .build();

        ApplyEntity saved = applyRepository.save(apply);
        return new ApplyCreateResponse(saved);
    }

    private MealType resolveMealType(MealType requested) {
        if (requested != null) {
            return requested;
        }

        LocalTime now = LocalTime.now();

        if (now.isAfter(DINNER_START)) {
            return MealType.DINNER;
        }

        if (now.isAfter(LUNCH_START)) {
            return MealType.LUNCH;
        }
        // 기본값
        return MealType.LUNCH;
    }
}