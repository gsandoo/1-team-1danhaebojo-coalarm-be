package _1danhebojo.coalarm.coalarm_service.global.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppHttpStatus {
    /**
     * 20X : 성공
     */
    OK(HttpStatus.OK, "요청이 정상적으로 수행되었습니다."),
    CREATED(HttpStatus.CREATED, "리소스를 생성하였습니다."),

    /**
     * 400 : 잘못된 문법으로 인해 요청을 이해할 수 없음
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    /**
     * 401 : 인증된 사용자가 아님
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),

    /**
     * 403 : 접근 권한이 없음
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    /**
     * 404 : 응답할 리소스가 없음
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리소스입니다."),
    NOT_FOUND_ENDPOINT(HttpStatus.NOT_FOUND, "존재하지 않는 엔드포인트입니다."),

    /**
     * 415 : 미디어 타입 에러
     */
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "허용되지 않은 파일 형식입니다."),

    /**
     * 500 : 서버 내부에서 에러가 발생함
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에 에러가 발생했습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}