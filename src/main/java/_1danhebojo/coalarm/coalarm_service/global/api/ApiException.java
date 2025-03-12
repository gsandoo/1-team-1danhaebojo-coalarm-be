package _1danhebojo.coalarm.coalarm_service.global.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    private final AppHttpStatus status;
}