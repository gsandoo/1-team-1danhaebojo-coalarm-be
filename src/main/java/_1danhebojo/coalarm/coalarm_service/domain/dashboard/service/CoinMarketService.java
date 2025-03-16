package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.DashboardResponse;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.MacdDTO;

public interface CoinMarketService {
    DashboardResponse getDashboardIndicators(Long coinId);
}
