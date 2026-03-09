package example.dsg_be.domain.user.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 3, max = 12, message = "이름 : 3 ~ 12자 범위의 값을 요구합니다")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자이상의 값을 요구합니다") // TODO : pattern 강화 정책 고려
    private String password;
}
