package _1danhebojo.coalarm.coalarm_service.global.jwt;

import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import _1danhebojo.coalarm.coalarm_service.global.properties.JwtProperties;
import _1danhebojo.coalarm.coalarm_service.global.oauth.CoalarmOAuth2User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtRepositoryImpl {

	private final String BEARER_PREFIX = "Bearer";
	private final String AUTHORIZATION_HEADER = "Authorization";
	private final String REFRESH_HEADER = "Refresh";
	private final JwtProperties jwtProperties;
	private SecretKey key;

	@PostConstruct
	public void init() {
		String base64EncodedSecretKey = encodeBase64(jwtProperties.getSecretKey());
		this.key = getSecretKeyFromBase64EncodedKey(base64EncodedSecretKey);
	}

	private String encodeBase64(String target) {
		return Encoders.BASE64.encode(target.getBytes(StandardCharsets.UTF_8));
	}

	// HS256 (HMAC with SHA-256)
	private SecretKey getSecretKeyFromBase64EncodedKey(String key) {
		byte[] keyBytes = Decoders.BASE64.decode(key);
		return new SecretKeySpec(keyBytes, Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	// 액세스 토큰, 리프레쉬 토큰 생성
	public Token generateTokenDto(Long id, String kakaoId) {
		Date issuedAt = new Date(System.currentTimeMillis());

		String accessToken = Jwts.builder()
			.claims(generatePublicClaims(id, kakaoId))
			.subject(kakaoId)
			.expiration(getTokenExpiration(jwtProperties.getAccessExpirationMillis()))
			.issuedAt(issuedAt)
			.signWith(key)
			.compact();

		String refreshToken = Jwts.builder()
			.subject(kakaoId)
			.expiration(getTokenExpiration(jwtProperties.getRefreshExpirationMillis()))
			.issuedAt(issuedAt)
			.signWith(key)
			.compact();

		return Token.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	private Date getTokenExpiration(long expirationMillis) {
		return new Date(new Date().getTime() + expirationMillis);
	}

	// 공개 클레임
	private Map<String, String> generatePublicClaims(Long id, String kakaoId) {
		Map<String, String> claims = new HashMap<>();
		claims.put("id", String.valueOf(id));
		claims.put("kakao_id", kakaoId);
		return claims;
	}

	// JWT 토큰 복호화 및 검증
	public Claims parseToken(String token) {
		try {
			return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (MalformedJwtException e) {
			throw new JwtException(AppHttpStatus.MALFORMED_TOKEN.getMessage());
		} catch (ExpiredJwtException e) {
			throw new JwtException(AppHttpStatus.EXPIRED_TOKEN.getMessage());
		} catch (UnsupportedJwtException e) {
			throw new JwtException(AppHttpStatus.UNSUPPORTED_TOKEN.getMessage());
		} catch (Exception e) {
			throw new JwtException(AppHttpStatus.INVALID_TOKEN.getMessage());
		}
	}

	// 액세스 토큰으로 Authentication 객체 가져오기
	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseToken(accessToken);
		String id = claims.get("id").toString();
		String kakaoId = claims.get("kakao_id").toString();


		CoalarmOAuth2User user = CoalarmOAuth2User.of(
				Long.valueOf(id),
			kakaoId
		);

		return new OAuth2AuthenticationToken(
			user,
			null,
			"kakao"
		);
	}

	public String resolveAccessToken(HttpServletRequest request) {
        // 쿠키가 존재하는지 확인
        if (request.getCookies() == null) {
            return null;
        }

        // 쿠키 중 AUTHORIZATION_HEADER Key를 갖는 쿠키를 찾고 Value 가져오기
        String accessToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(AUTHORIZATION_HEADER))
                .findAny()
                .map(Cookie::getValue)
                .orElse(null);

        if (!StringUtils.hasText(accessToken) || !accessToken.startsWith(BEARER_PREFIX)) {
			return null;
		}
		return accessToken.substring(BEARER_PREFIX.length());
	}

	public String resolveRefreshToken(HttpServletRequest request) {
        // 쿠키가 존재하는지 확인
        if (request.getCookies() == null) {
            return null;
        }

        // 쿠키 중 REFRESH_HEADER Key를 갖는 쿠키를 찾고 Value 가져오기
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(REFRESH_HEADER))
                .findAny()
                .map(Cookie::getValue)
                .orElse(null);

		if (!StringUtils.hasText(refreshToken)) {
			return null;
		}

		return refreshToken;
	}
}
