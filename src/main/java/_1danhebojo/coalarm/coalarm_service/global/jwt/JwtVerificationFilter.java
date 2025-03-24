package _1danhebojo.coalarm.coalarm_service.global.jwt;

import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import _1danhebojo.coalarm.coalarm_service.global.api.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
	private final String CONTENT_TYPE = "application/json;charset=UTF-8";

	private final JwtRepositoryImpl jwtRepository;
	// TODO : 로그아웃 처리 필요
	private ObjectMapper objectMapper = new ObjectMapper();
	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String accessToken = jwtRepository.resolveAccessToken(request);

		// Access Token 없으면 다음 필터로 넘어가기
		if (!StringUtils.hasText(accessToken)) {
			filterChain.doFilter(request, response);
			return;
		}

        // 토큰 유효성 검사
		try {
			jwtRepository.parseToken(accessToken);
			setAuthenticationToSecurityContextHolder(accessToken);
			filterChain.doFilter(request, response);
		} catch (JwtException e) {
			log.error("JWT 토큰 검증 실패: {}", e.getMessage());
			response.setContentType(CONTENT_TYPE);
			response.setStatus(401);
			String body = objectMapper.writeValueAsString(
					BaseResponse.error(ErrorResponse.builder()
							.code(AppHttpStatus.INVALID_ACCESS_TOKEN.getHttpStatus().value())
							.message(e.getMessage())
							.build())
			);
			response.getWriter().write(body);
		}
	}


	private void setAuthenticationToSecurityContextHolder(String accessToken) {
		Authentication authentication = jwtRepository.getAuthentication(accessToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
