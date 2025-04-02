package _1danhebojo.coalarm.coalarm_service.domain.coin.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoinJpaRepository extends JpaRepository<CoinEntity,Long> {
    Optional<CoinEntity> findBySymbol(String symbol);
    List<CoinEntity> findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(String searchTerm, String searchTerm2);
}
