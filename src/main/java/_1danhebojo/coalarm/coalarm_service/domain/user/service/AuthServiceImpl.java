package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.global.api.AppCookie;
import _1danhebojo.coalarm.coalarm_service.global.oauth.CoalarmOAuth2User;
import _1danhebojo.coalarm.coalarm_service.global.properties.OAuthProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final String UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String REFRESH_HEADER = "Refresh";
    private final String BEARER_PREFIX = "Bearer";
    private final String COOKIE_HEADER = "Set-Cookie";

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    private final OAuthProperties oAuthProperties;
    private final AppCookie appCookie;

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
        response.addHeader(COOKIE_HEADER, appCookie.deleteCookie(AUTHORIZATION_HEADER));
        response.addHeader(COOKIE_HEADER, appCookie.deleteCookie(REFRESH_HEADER));

        // 3.카카오 로그아웃 URL로 리다이렉트
        String kakaoLogoutUrl = String.format(
                "https://kauth.kakao.com/oauth/logout?client_id=%s&logout_redirect_uri=%s",
                oAuthProperties.getClient().getRegistration().getKakao().getClientId(),
                oAuthProperties.getWeb().getUrl().getLogin()
        );

        response.sendRedirect(kakaoLogoutUrl);
    }

    @Override
    public void unlinkKakaoAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oAuthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
                oAuthToken.getAuthorizedClientRegistrationId(),
                oAuthToken.getName()
        );
        String kakaoAccessToken = client.getAccessToken().getTokenValue();

        // 카카오 로그인 언링크
        unlinkKakaoUser(kakaoAccessToken);
    }

    public void unlinkKakaoUser(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Authorization: Bearer ...
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. HTTP 엔티티 생성
        HttpEntity<String> request = new HttpEntity<>("", headers);

        // 3. 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                UNLINK_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        // 4. 응답 로깅
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("카카오 사용자 언링크 성공: {}", response.getBody());
        }
    }
}
