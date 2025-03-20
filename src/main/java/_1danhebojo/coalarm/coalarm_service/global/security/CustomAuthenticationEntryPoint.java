package _1danhebojo.coalarm.coalarm_service.global.security;

import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import _1danhebojo.coalarm.coalarm_service.global.api.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
        throws IOException,ServletException {

        // 인증 실패 시 HTTP 응답 설정
        response.setContentType(("application/json;charset=UTF-8"));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 에러 응답 객체 생성
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(AppHttpStatus.UNAUTHORIZED.getHttpStatus().value())
                .message(AppHttpStatus.UNAUTHORIZED.getMessage())
                .build();

        BaseResponse<Void> baseResponse = BaseResponse.error(errorResponse);

        try {
            // 응답을 JSON으로 변환하여 츌력
            response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
        } catch (JsonProcessingException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JSON 변환 중 오류 발생");
        } catch (java.io.IOException e) {
            throw new ServletException("응답 스트림 처리 중 오류 발생", e);
        }
    }
}