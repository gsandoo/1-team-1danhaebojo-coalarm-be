package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinJpaRepository extends JpaRepository<CoinEntity,Long> {
    Optional<CoinEntity> findBySymbol(String symbol);
    Optional<CoinEntity> findByCoinId(Long coinId);
}
