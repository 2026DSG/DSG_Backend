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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Bean // https 배포시 삭제
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:3000",
                "https://dsg-ecru.vercel.app"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf ->
                        csrf.disable())
                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource()))
                .formLogin(form ->
                        form.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
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

                                // apply
                                .requestMatchers(HttpMethod.POST, "/main/apply").authenticated()
                                .requestMatchers(HttpMethod.GET, "/main/apply").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/main/apply/{apply-id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/admin/apply").hasAuthority("OFFICE")
                                .requestMatchers(HttpMethod.GET, "/admin/apply/excel/monthly").hasAuthority("OFFICE")
                                .requestMatchers(HttpMethod.GET, "/admin/apply/excel/summary").hasAuthority("OFFICE")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, objectMapper),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
