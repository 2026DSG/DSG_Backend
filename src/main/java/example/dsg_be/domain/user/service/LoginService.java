package example.dsg_be.domain.user.service;

import example.dsg_be.domain.user.domain.User;
import example.dsg_be.domain.user.exception.PasswordMisMatchException;
import example.dsg_be.domain.user.exception.UserNotFoundException;
import example.dsg_be.domain.user.presentation.dto.request.AuthRequest;
import example.dsg_be.domain.user.presentation.dto.resposnse.TokenResponse;
import example.dsg_be.domain.user.repository.UserRepository;
import example.dsg_be.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponse execute(AuthRequest authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);


        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw PasswordMisMatchException.EXCEPTION;
        }

        return jwtTokenProvider.generateBothToken(authRequest.getUsername(), user.getRole());
    }
}
