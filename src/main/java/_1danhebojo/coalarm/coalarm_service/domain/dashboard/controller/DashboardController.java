package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.ResponseKimchiPremium;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.service.KimchiPremiumService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final KimchiPremiumService kimchiPremiumService;

    @GetMapping("/kimchi")
    public ResponseEntity<Map<String, Object>> getKimchiPremium(
            @RequestParam(name = "offset") @Min(0) Integer offset,
            @RequestParam(name = "limit") @Min(1) Integer limit
    ) {
        List<ResponseKimchiPremium> premiums = kimchiPremiumService.getKimchiPremiums(offset, limit);

        Map<String, Object> response = Map.of(
                "status", "success",
                "data", Map.of(
                        "contents", premiums,
                        "offset", offset,
                        "limit", limit,
                        "totalElements", premiums.size(),
                        "hasNext", premiums.size() == limit
                )
        );

        return ResponseEntity.ok(response);
    }

}
