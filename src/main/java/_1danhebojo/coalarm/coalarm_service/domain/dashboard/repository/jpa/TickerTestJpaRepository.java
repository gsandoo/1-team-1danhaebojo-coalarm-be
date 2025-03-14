package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerTestEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerTestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TickerTestJpaRepository extends JpaRepository<TickerTestEntity, TickerTestId> {
}