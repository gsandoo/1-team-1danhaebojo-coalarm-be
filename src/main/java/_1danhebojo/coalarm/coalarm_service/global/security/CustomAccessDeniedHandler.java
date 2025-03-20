package _1danhebojo.coalarm.coalarm_service.global.security;

import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import _1danhebojo.coalarm.coalarm_service.global.api.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        // 인가 실패 시 응답 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // JSON 응답 포맷 설정
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(AppHttpStatus.FORBIDDEN.getHttpStatus().value())
                .message(AppHttpStatus.FORBIDDEN.getMessage())
                .build();

        BaseResponse<Void> baseResponse = BaseResponse.error(errorResponse);

        response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
    }

}
