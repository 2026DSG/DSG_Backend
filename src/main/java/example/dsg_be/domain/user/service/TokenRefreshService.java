package example.dsg_be.domain.user.service;

import example.dsg_be.domain.user.domain.RefreshToken;
import example.dsg_be.domain.user.domain.Role;
import example.dsg_be.domain.user.domain.User;
import example.dsg_be.domain.user.exception.RefreshTokenMisMatchException;
import example.dsg_be.domain.user.exception.RefreshTokenNotFoundException;
import example.dsg_be.domain.user.exception.UserNotFoundException;
import example.dsg_be.domain.user.presentation.dto.request.RefreshTokenRequest;
import example.dsg_be.domain.user.presentation.dto.resposnse.TokenResponse;
import example.dsg_be.domain.user.repository.RefreshTokenRepository;
import example.dsg_be.domain.user.repository.UserRepository;
import example.dsg_be.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public TokenResponse execute(RefreshTokenRequest request) {
        jwtTokenProvider.validateToken(request.getRefreshToken());

        String username = jwtTokenProvider.getAuthentication(request.getRefreshToken()).getName();
        RefreshToken savedRefreshToken = refreshTokenRepository.findById(username)
                .orElseThrow(() -> RefreshTokenNotFoundException.EXCEPTION);

        if(!savedRefreshToken.getToken().equals(request.getRefreshToken())) {
            throw RefreshTokenMisMatchException.EXCEPTION;
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        Role role = user.getRole();

        String accessToken = jwtTokenProvider.generateAccessToken(username, role);
        String refreshToken = jwtTokenProvider.generateRefreshToken(username, role);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
