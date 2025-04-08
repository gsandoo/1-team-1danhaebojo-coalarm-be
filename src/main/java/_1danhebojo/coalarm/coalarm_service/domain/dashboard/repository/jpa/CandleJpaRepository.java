package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CandleCompositeKey;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CandleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandleJpaRepository extends JpaRepository<CandleEntity, CandleCompositeKey> {
}
