package example.dsg_be.domain.apply.presentation.dto.request;

import example.dsg_be.domain.apply.domain.MealType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyCreateRequest {
    private MealType mealType; // null이면 시간 기반 자동 설정

    @NotBlank(message = "신청 사유는 필수입니다.")
    private String reason;

    @NotBlank(message = "교직원 이름은 필수입니다.")
    private String staffName;

    @NotBlank(message = "부서는 필수입니다.")
    private String department;
}
