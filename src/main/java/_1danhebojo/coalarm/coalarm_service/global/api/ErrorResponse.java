package _1danhebojo.coalarm.coalarm_service.global.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {

    int code;                     // HTTP 상태 코드
    String message;               // 메인 에러 메시지
    Map<String, String> errors;   // (선택) 필드 에러나 상세 메시지 모음

    /**
     * 다양한 예외를 단일 메서드에서 처리하는 예시
     */
    public static ErrorResponse of(Throwable t) {
        // ApiException 유형 처리
        if (t instanceof ApiException e) {
            return ErrorResponse.builder()
                    .code(e.getStatus().getHttpStatus().value())
                    .message(e.getStatus().getMessage())
                    .build();
        }

        // Bean Validation 예외 처리
        else if (t instanceof ConstraintViolationException e) {
            Map<String, String> errors = new HashMap<>();

            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                String field = violation.getPropertyPath()
                        .toString()
                        .split("\\.")[1];
                errors.put(field, violation.getMessage());
            }

            return ErrorResponse.builder()
                    .code(400)
                    .message("유효성 검사 실패")
                    .errors(errors)
                    .build();
        }

        // 특정 리소스를 찾을 수 없을 때
        else if (t instanceof NoResourceFoundException e) {
            String message = e.getResourcePath() + " : " + AppHttpStatus.NOT_FOUND_ENDPOINT.getMessage();

            return ErrorResponse.builder()
                    .code(404)
                    .message(message)
                    .build();
        }

        // 그 밖의 런타임 예외 처리
        else if (t instanceof RuntimeException e) {
            return ErrorResponse.builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }

        // 그 외 알 수 없는 예외 처리
        return ErrorResponse.builder()
                .code(500)
                .message("알 수 없는 예외 발생")
                .build();
    }
}