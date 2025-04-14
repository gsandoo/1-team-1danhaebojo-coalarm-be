package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CandleEntity;

import java.util.List;

public interface CandleRepository {
    List<CandleEntity> findRecentCandles(String symbol, int limit);

    List<CandleEntity> findDailyCandles(String symbol, int limit);
}
