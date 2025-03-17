package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoinIndicatorResponse {
    private MacdDTO macd;
    private RsiDTO rsi;
}
