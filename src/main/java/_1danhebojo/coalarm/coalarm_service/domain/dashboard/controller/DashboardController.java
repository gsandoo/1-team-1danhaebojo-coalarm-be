package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.ResponseKimchiPremium;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.service.KimchiPremiumService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.MacdDTO;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.service.CoinMarketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final CoinMarketService coinMarketService;
    private final KimchiPremiumService kimchiPremiumService;

    @GetMapping("/{symbol}/macd")
    public ResponseEntity<MacdDTO> getMacd(@PathVariable("symbol") String symbol) {
        MacdDTO response = coinMarketService.getMacdForSymbol(symbol);
        return ResponseEntity.ok(response);
    }

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
