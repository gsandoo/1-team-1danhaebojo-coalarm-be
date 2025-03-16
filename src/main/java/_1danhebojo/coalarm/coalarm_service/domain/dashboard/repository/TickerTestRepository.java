package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerTestEntity;

import java.util.List;
import java.util.Optional;

public interface TickerTestRepository {
    List<TickerTestEntity> findByCoinIdOrderedByUtcDateTime(Long coinId);
    Optional<TickerTestEntity> findLatestByCode(String code);
}
