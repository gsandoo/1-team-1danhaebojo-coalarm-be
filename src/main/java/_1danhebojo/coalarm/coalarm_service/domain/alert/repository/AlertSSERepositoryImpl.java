package _1danhebojo.coalarm.coalarm_service.domain.alert.repository;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.*;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.AlertSSEJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.GoldenCrossJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.TargetPriceJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.VolumeSpikeJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AlertSSERepositoryImpl {
    private final AlertSSEJpaRepository alertSSEJpaRepository;
    private final VolumeSpikeJpaRepository volumeSpikeJpaRepository;

    public Optional<Ticker> findLatestBySymbol(String symbol, String exchange) {
        return alertSSEJpaRepository.findLatestBySymbol(symbol, exchange);
    }

    public List<Ticker> findBySymbolAndDateRangeAndExchange(String symbol, Instant startDate, String exchange){
        return alertSSEJpaRepository.findBySymbolAndDateRangeAndExchange(symbol, startDate, exchange);
    }

    public List<VolumeSpikeAlert> findAllVolumeSpikeAlertByStatus() {
        return volumeSpikeJpaRepository.findAllVolumeSpikeAlertByStatus();
    }
}
