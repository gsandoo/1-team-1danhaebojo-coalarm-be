package _1danhebojo.coalarm.coalarm_service.global.security;

import _1danhebojo.coalarm.coalarm_service.global.properties.CorsProperties;
import _1danhebojo.coalarm.coalarm_service.global.jwt.JwtRepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.global.jwt.JwtVerificationFilter;
import _1danhebojo.coalarm.coalarm_service.global.oauth.CoalarmOAuth2UserService;
import _1danhebojo.coalarm.coalarm_service.global.oauth.OAuthFailureHandler;
import _1danhebojo.coalarm.coalarm_service.global.oauth.OAuthSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CoalarmOAuth2UserService oAuth2UserService;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final OAuthFailureHandler oAuthFailureHandler;
    private final JwtRepositoryImpl jwtRepositoryImpl;
    private final CorsProperties corsProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 X
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
//                        .requestMatchers("/api/v1/health").permitAll()// 헬스 체크 인증 없이 허용
                        .requestMatchers("/api/v1/**").permitAll()
                )
                // OAuth 설정 (기본)
                .oauth2Login((oauth) -> {
                    oauth
                            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2UserService))
                            .successHandler(oAuthSuccessHandler)
                            .failureHandler(oAuthFailureHandler);
                })
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 인증 실패 처리
                        .accessDeniedHandler(customAccessDeniedHandler) // 권한 부족(403) 처리
                )
                .addFilterBefore(
                        new JwtVerificationFilter(
                                jwtRepositoryImpl
                        ),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsProperties.getAllowOrigins());
        configuration.setAllowedMethods(corsProperties.getAllowMethods());
        configuration.setAllowCredentials(corsProperties.isAllowCredentials());
        for (String exposedHeader : corsProperties.getExposedHeaders()) {
            configuration.addExposedHeader(exposedHeader);
        }
        for (String allowedHeader : corsProperties.getAllowedHeaders()) {
            configuration.addAllowedHeader(allowedHeader);
        }
        configuration.setMaxAge(corsProperties.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
