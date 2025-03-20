package _1danhebojo.coalarm.coalarm_service.domain.health.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {
    @GetMapping("")
    public String health() {
        return "health check successsful";
    }
}
