package example.dsg_be.global.config;

import example.dsg_be.global.security.jwt.JwtAuthenticationFilter;
import example.dsg_be.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf ->
                        csrf.disable())
                .cors(cors ->
                        cors.configure(http))
                .formLogin(form ->
                        form.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth ->
                        auth
                                // user
                                .requestMatchers(HttpMethod.POST, "/main/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/main/refresh").permitAll()

                                //teacher
                                .requestMatchers(HttpMethod.POST, "/admin/teacher").hasAuthority("OFFICE")
                                .requestMatchers(HttpMethod.GET, "/admin/teacher").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/admin/teacher/{teacher-id}").hasAuthority("OFFICE")
                                .requestMatchers(HttpMethod.POST, "/admin/teacher/excel").hasAuthority("OFFICE")
                                .requestMatchers(HttpMethod.PUT, "/admin/teacher/excel").hasAuthority("OFFICE")
                                .requestMatchers(HttpMethod.GET, "/admin/teacher/excel").hasAuthority("OFFICE")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
