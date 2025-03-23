package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.CoinIndicatorResponse;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.ResponseKimchiPremium;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.service.KimchiPremiumService;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import _1danhebojo.coalarm.coalarm_service.global.api.OffsetResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.service.CoinIndicatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final CoinIndicatorService coinIndicatorService;
    private final KimchiPremiumService kimchiPremiumService;

    @GetMapping("/{coinId}/index")
    public ResponseEntity<BaseResponse<CoinIndicatorResponse>> getDashboardIndicators(@PathVariable("coinId") Long coinId) {
        CoinIndicatorResponse response = coinIndicatorService.getDashboardIndicators(coinId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @GetMapping("/kimchi")
    public ResponseEntity<OffsetResponse<ResponseKimchiPremium>> getKimchiPremium(
            @RequestParam(name = "offset") @Min(0) Integer offset,
            @RequestParam(name = "limit") @Min(1) Integer limit
    ) {

        OffsetResponse<ResponseKimchiPremium> response = kimchiPremiumService.getKimchiPremiums(offset, limit);
        return ResponseEntity.ok(response);
    }

}
