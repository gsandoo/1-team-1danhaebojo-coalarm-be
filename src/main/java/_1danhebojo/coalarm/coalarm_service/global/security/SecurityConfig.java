package _1danhebojo.coalarm.coalarm_service.global.security;

import _1danhebojo.coalarm.coalarm_service.domain.auth.service.JwtBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private final JwtBlacklistService jwtBlacklistService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 X
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/oauth/kakao/**").permitAll()// 카카오 로그인 API 인증 없이 허용
                        .requestMatchers("/health").permitAll()// 헬스 체크 인증 없이 허용
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 인증 실패 처리
                        .accessDeniedHandler(customAccessDeniedHandler) // 권한 부족(403) 처리
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, jwtBlacklistService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
