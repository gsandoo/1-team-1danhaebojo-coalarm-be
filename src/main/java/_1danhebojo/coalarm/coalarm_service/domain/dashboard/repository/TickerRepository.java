package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerEntity;

import java.util.List;
import java.util.Optional;

public interface TickerRepository {
    List<TickerEntity> findByCoinIdOrderedByUtcDateTime(Long coinId);
}
