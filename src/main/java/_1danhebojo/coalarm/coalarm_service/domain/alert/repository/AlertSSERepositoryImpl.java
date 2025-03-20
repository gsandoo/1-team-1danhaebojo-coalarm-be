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
public class AlertSSERepositoryImpl {
    private final AlertSSEJpaRepository alertSSEJpaRepository;
    private final TargetPriceJpaRepository targetPriceJpaRepository;
    private final GoldenCrossJpaRepository goldenCrossJpaRepository;
    private final VolumeSpikeJpaRepository volumeSpikeJpaRepository;

    public AlertSSERepositoryImpl(AlertSSEJpaRepository alertSSEJpaRepository, TargetPriceJpaRepository targetPriceJpaRepository, GoldenCrossJpaRepository goldenCrossJpaRepository, VolumeSpikeJpaRepository volumeSpikeJpaRepository) {
        this.alertSSEJpaRepository = alertSSEJpaRepository;
        this.targetPriceJpaRepository = targetPriceJpaRepository;
        this.goldenCrossJpaRepository = goldenCrossJpaRepository;
        this.volumeSpikeJpaRepository = volumeSpikeJpaRepository;
    }

    @Transactional(readOnly = true)
    public List<Alert> findByUserId(Long userId) {
        return alertSSEJpaRepository.findByUserId(userId);
    }

    public List<Alert> findActiveAlertsByUserId(Long userId) {
        return alertSSEJpaRepository.findActiveAlertsByUserId(userId);
    }

    public Optional<Ticker> findLatestBySymbol(String symbol, String exchange) {
        return alertSSEJpaRepository.findLatestBySymbol(symbol, exchange);
    }

    public List<Ticker> findBySymbolAndDateRange(String symbol, Instant startDate){
        return alertSSEJpaRepository.findBySymbolAndDateRange(symbol, startDate);
    }

    public List<Ticker> findBySymbolAndDateRangeAndExchange(String symbol, Instant startDate, String exchange){
        return alertSSEJpaRepository.findBySymbolAndDateRangeAndExchange(symbol, startDate, exchange);
    }

    // 특정 거래소의 최신 티커 조회
    public Optional<Ticker> findLatestByExchangeAndSymbol(String exchange, String symbol){
        return alertSSEJpaRepository.findLatestByExchangeAndSymbol(exchange, symbol);
    }

    public Optional<TargetPriceAlert> findTargetPriceAlertsByAlertId(Long alertId){
        return targetPriceJpaRepository.findTargetPriceAlertsByAlertId(alertId);
    }

    public Optional<GoldenCrossAlert> findGoldenCrossAlertByAlertId(Long alertId){
        return goldenCrossJpaRepository.findGoldenCrossAlertsByAlertId(alertId);
    }

    public Optional<VolumeSpikeAlert> findVolumeSpikeAlertByAlertId(Long alertId){
        return volumeSpikeJpaRepository.findVolumeSpikeAlertByAlertId(alertId);
    }

    public List<VolumeSpikeAlert> findAllVolumeSpikeAlertByStatus() {
        return volumeSpikeJpaRepository.findAllVolumeSpikeAlertByStatus();
    }
}
