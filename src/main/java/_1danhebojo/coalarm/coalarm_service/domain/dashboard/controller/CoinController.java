package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.CoinDTO;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.service.CoinService;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import _1danhebojo.coalarm.coalarm_service.global.api.OffsetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coins")
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<CoinDTO>>> getAllCoins() {
        List<CoinDTO> coins = coinService.getAllCoins();
        return ResponseEntity.ok(BaseResponse.success(coins));
    }

    @GetMapping("/paging")
    public ResponseEntity<BaseResponse<OffsetResponse<CoinDTO>>> getCoinsWithPaging(
            @RequestParam(name = "offset", defaultValue = "0") Integer offset,
            @RequestParam(name = "limit", defaultValue = "10") Integer limit
    ) {
        OffsetResponse<CoinDTO> response = coinService.getCoinsWithPaging(offset, limit);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @GetMapping("/{coinId}")
    public ResponseEntity<BaseResponse<CoinDTO>> getCoinById(@PathVariable("coinId") Long coinId) {
        CoinDTO coin = coinService.getCoinById(coinId);
        return ResponseEntity.ok(BaseResponse.success(coin));
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<CoinDTO>> searchCoins(@RequestParam("term") String term) {
        CoinDTO coin = coinService.searchCoinByNameOrSymbol(term);
        return ResponseEntity.ok(BaseResponse.success(coin));
    }
}