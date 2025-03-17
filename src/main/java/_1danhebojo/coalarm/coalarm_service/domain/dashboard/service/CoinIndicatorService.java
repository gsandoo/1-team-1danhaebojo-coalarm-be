package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.CoinIndicatorResponse;

public interface CoinIndicatorService {
    CoinIndicatorResponse getDashboardIndicators(Long coinId);
}
