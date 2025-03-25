package _1danhebojo.coalarm.coalarm_service.global.oauth;

import java.io.IOException;

import _1danhebojo.coalarm.coalarm_service.global.properties.JwtProperties;
import _1danhebojo.coalarm.coalarm_service.global.properties.OAuthProperties;
import _1danhebojo.coalarm.coalarm_service.global.jwt.JwtRepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.global.jwt.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final String AUTHORIZATION_HEADER = "Authorization";
	private final String REFRESH_HEADER = "Refresh";
    private final String BEARER_PREFIX = "Bearer";
    private final String COOKIE_HEADER = "Set-Cookie";
	private final JwtRepositoryImpl jwtRepositoryImpl;
	private final JwtProperties jwtProperties;
	private final OAuthProperties oAuthProperties;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		CoalarmOAuth2User oAuthUser = (CoalarmOAuth2User) authentication.getPrincipal();
        log.info("카카오 ID : {} 인증 성공, 액세스 토큰 발급... ", oAuthUser.getKakaoId());

        Token token = jwtRepositoryImpl.generateTokenDto(oAuthUser.getId(), oAuthUser.getKakaoId());

        response.addHeader(COOKIE_HEADER, createCookie(AUTHORIZATION_HEADER, BEARER_PREFIX + token.getAccessToken()));
        response.addHeader(COOKIE_HEADER, createCookie(REFRESH_HEADER, token.getRefreshToken()));

        // TODO : 리프레쉬 토큰 저장

        response.sendRedirect(oAuthProperties.getUrl().getDashboard());
	}

    private String createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .path(jwtProperties.getCookie().getPath())
                .sameSite(jwtProperties.getCookie().getSameSite())
                .httpOnly(jwtProperties.getCookie().isHttpOnly())
                .secure(jwtProperties.getCookie().isSecure())
                .maxAge(jwtProperties.getCookie().getMaxAge())
                .build()
                .toString();
    }
}