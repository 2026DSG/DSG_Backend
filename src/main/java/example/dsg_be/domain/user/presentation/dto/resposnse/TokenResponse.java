package example.dsg_be.domain.user.presentation.dto.resposnse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private final String accessToken;
    private final String refreshToken;
}
