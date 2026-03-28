package example.dsg_be.domain.user.presentation.dto.resposnse;

import example.dsg_be.domain.user.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenWithRoleResponse {
    private final String accessToken;
    private final String refreshToken;
    private final Role role;
}
