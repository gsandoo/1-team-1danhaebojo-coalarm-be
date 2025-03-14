package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.MacdDTO;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.service.CoinMarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final CoinMarketService coinMarketService;

    @GetMapping("/{symbol}/macd")
    public ResponseEntity<MacdDTO> getMacd(@PathVariable("symbol") String symbol) {
        MacdDTO response = coinMarketService.getMacdForSymbol(symbol);
        return ResponseEntity.ok(response);
    }

}
