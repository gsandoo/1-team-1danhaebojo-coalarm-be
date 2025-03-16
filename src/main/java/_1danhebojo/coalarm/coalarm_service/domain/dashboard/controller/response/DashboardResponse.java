package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardResponse {
    private MacdDTO macd;
    private RsiDTO rsi;
}
