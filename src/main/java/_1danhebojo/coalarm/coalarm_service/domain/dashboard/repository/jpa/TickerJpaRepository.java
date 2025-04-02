package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerCompositeKey;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TickerJpaRepository extends JpaRepository<TickerEntity, TickerCompositeKey> {

    Optional<TickerEntity> findFirstByIdBaseSymbolAndIdQuoteSymbolOrderByIdTimestampDesc(
            String baseSymbol, String quoteSymbol);
}
