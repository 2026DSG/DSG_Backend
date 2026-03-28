package example.dsg_be.domain.user.presentation;

import example.dsg_be.domain.user.presentation.dto.request.AuthRequest;
import example.dsg_be.domain.user.presentation.dto.request.RefreshTokenRequest;
import example.dsg_be.domain.user.presentation.dto.resposnse.TokenWithRoleResponse;
import example.dsg_be.domain.user.service.LoginService;
import example.dsg_be.domain.user.service.TokenRefreshService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class UserController {
    private final LoginService loginService;
    private final TokenRefreshService tokenRefreshService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenWithRoleResponse login(@Valid @RequestBody AuthRequest authRequest) {
        return loginService.execute(authRequest);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public TokenWithRoleResponse refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return tokenRefreshService.execute(refreshTokenRequest);
    }
}
