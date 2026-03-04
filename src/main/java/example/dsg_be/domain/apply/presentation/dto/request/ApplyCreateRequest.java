package example.dsg_be.domain.apply.presentation.dto.request;

import example.dsg_be.domain.apply.domain.MealType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplyCreateRequest {

    @NotNull(message = "교직원 아이디는 필수입니다")
    private Long teacherId;

    private MealType meal;

    @NotBlank(message = "신청 사유는 비어있을 수 없습니다")
    private String reason;
}
