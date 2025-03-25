package _1danhebojo.coalarm.coalarm_service.domain.coin.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinJpaRepository extends JpaRepository<CoinEntity,Long> {
    Optional<CoinEntity> findBySymbol(String symbol);
    Optional<CoinEntity> findByCoinId(Long coinId);

    Optional<CoinEntity> findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(String Start_term, String End_term);
}
