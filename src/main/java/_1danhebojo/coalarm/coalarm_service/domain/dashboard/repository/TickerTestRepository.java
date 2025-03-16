package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerTestEntity;

import java.util.List;

public interface TickerTestRepository {
    List<TickerTestEntity> findByCodeOrderedByUtcDateTime(String code);
}
