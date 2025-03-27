package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.global.oauth.CoalarmOAuth2User;
import _1danhebojo.coalarm.coalarm_service.global.properties.JwtProperties;
import _1danhebojo.coalarm.coalarm_service.global.properties.KakaoProperties;
import _1danhebojo.coalarm.coalarm_service.global.properties.OAuthProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String REFRESH_HEADER = "Refresh";
    private final String BEARER_PREFIX = "Bearer";
    private final String COOKIE_HEADER = "Set-Cookie";
    private final JwtProperties jwtProperties;
    private final OAuthProperties oAuthProperties;

	@Override
	public Long getLoginUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) {
            return null;
        }

        // 로그인 O -> UsernamePasswordAuthenticationToken
        // 로그인 X -> AnonymousAuthenticationToken
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            return null;
        }

        // 로그인 O -> SingKUserDetails
        // 로그인 X -> anonymousUser
        Object details = authentication.getPrincipal();
        if (!(details instanceof CoalarmOAuth2User)) {
            return null;
        }

		return ((CoalarmOAuth2User) details).getId();
	}

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. SecurityContext 제거
        SecurityContextHolder.clearContext();

        // 2. 쿠키 제거
        response.addHeader(COOKIE_HEADER, deleteCookie(AUTHORIZATION_HEADER));
        response.addHeader(COOKIE_HEADER, deleteCookie(REFRESH_HEADER));

        // 3.카카오 로그아웃 URL로 리다이렉트
        String kakaoLogoutUrl = String.format(
                "https://kauth.kakao.com/oauth/logout?client_id=%s&logout_redirect_uri=%s",
                oAuthProperties.getClient().getRegistration().getKakao().getClientId(),
                oAuthProperties.getWeb().getUrl().getLogin()
        );

        response.sendRedirect(kakaoLogoutUrl);
    }

    private String deleteCookie(String key) {
        return ResponseCookie.from(key, "")
                .path(jwtProperties.getCookie().getPath())
                .sameSite(jwtProperties.getCookie().getSameSite())
                .httpOnly(jwtProperties.getCookie().isHttpOnly())
                .secure(jwtProperties.getCookie().isSecure())
                .maxAge(0)
                .build()
                .toString();
    }
}
