package example.dsg_be.domain.apply.service;

import example.dsg_be.domain.apply.domain.MealType;
import example.dsg_be.domain.apply.presentation.dto.response.ApplyListResponse;
import example.dsg_be.domain.apply.repository.ApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyListReadService {

    private final ApplyRepository applyRepository;

    @Transactional(readOnly = true)
    public List<ApplyListResponse> execute(MealType meal, LocalDate applyDate) {
        return applyRepository.findAllByMealAndApplyDateOrderByCreatedAtDesc(meal, applyDate)
                .stream()
                .map(ApplyListResponse::new)
                .toList();
    }
}
