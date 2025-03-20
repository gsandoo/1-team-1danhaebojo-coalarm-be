package _1danhebojo.coalarm.coalarm_service.domain.health.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HealthController {
    @GetMapping("/")
    public String health() {
        return "health check successsful";
    }
}
