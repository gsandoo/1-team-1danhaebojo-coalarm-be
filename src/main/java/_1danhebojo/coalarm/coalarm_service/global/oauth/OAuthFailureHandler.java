package _1danhebojo.coalarm.coalarm_service.global.oauth;

import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import _1danhebojo.coalarm.coalarm_service.global.api.ErrorResponse;
import _1danhebojo.coalarm.coalarm_service.global.properties.OAuthProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final OAuthProperties oAuthProperties;
    private final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {

        response.setContentType(CONTENT_TYPE);
        response.setStatus(401);
        String body = objectMapper.writeValueAsString(
                BaseResponse.error(ErrorResponse.builder()
                        .code(AppHttpStatus.FAILED_AUTHENTICATION_OAUTH.getHttpStatus().value())
                        .message(exception.getMessage())
                        .build())
        );
        response.getWriter().write(body);

        // 로그인 페이지로 리디렉션
		response.sendRedirect(oAuthProperties.getWeb().getUrl().getLogin());
	}
}
