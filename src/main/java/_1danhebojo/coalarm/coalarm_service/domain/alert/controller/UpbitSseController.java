package _1danhebojo.coalarm.coalarm_service.domain.alert.controller;

import _1danhebojo.coalarm.coalarm_service.domain.alert.service.UpbitSseService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/sse/trade")
@PermitAll
@RequiredArgsConstructor
class UpbitSseController {

    private final UpbitSseService upbitService;

    @GetMapping(value = "/{symbol}")
    public SseEmitter subscribe(@PathVariable String symbol) {
        upbitService.subscribeSymbol(symbol);
        return upbitService.addEmitter(symbol);
    }
}
