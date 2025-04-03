package _1danhebojo.coalarm.coalarm_service.domain.alert.controller;

import _1danhebojo.coalarm.coalarm_service.domain.alert.service.AlertHistoryService;
import _1danhebojo.coalarm.coalarm_service.domain.alert.service.AlertSSEService;
import _1danhebojo.coalarm.coalarm_service.domain.alert.service.AlertService;
import _1danhebojo.coalarm.coalarm_service.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alerts/subscribe")
public class AlertSSEController {

    private final AlertSSEService alertSSEService;
    private final AuthService authService;


    // <editor-fold desc="알람 SSE 관련 메서드">
    // SSE 구독 (로그인 시 활성화된 알람 전송)
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        Long userId =  authService.getLoginUserId();
        return alertSSEService.subscribe(userId);
    }

    // SSE 구독 테스트용
    @GetMapping(value = "/test/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeTest(@PathVariable Long userId) {
        return alertSSEService.subscribe(userId);
    }
    // </editor-fold>
}
