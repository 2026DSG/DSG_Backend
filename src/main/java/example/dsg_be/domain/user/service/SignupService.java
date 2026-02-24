package example.dsg_be.domain.user.service;

import example.dsg_be.domain.user.domain.User;
import example.dsg_be.domain.user.presentation.dto.request.AuthRequest;
import example.dsg_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void execute(AuthRequest authRequest) {
        userRepository.save(
                User.builder()
                        .username(authRequest.getUsername())
                        .password(passwordEncoder.encode(authRequest.getPassword())) // 암호화
                        .build()
        );
    }
}
