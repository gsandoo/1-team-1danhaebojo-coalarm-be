package _1danhebojo.coalarm.coalarm_service.domain.alert.repository;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.*;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.AlertSSEJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.VolumeSpikeJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AlertSSERepositoryImpl {
    private final AlertSSEJpaRepository alertSSEJpaRepository;
    private final VolumeSpikeJpaRepository volumeSpikeJpaRepository;

    public Optional<TickerEntity> findLatestBySymbol(String symbol, String exchange) {
        return alertSSEJpaRepository.findLatestBySymbol(symbol, exchange);
    }

    public List<TickerEntity> findBySymbolAndDateRangeAndExchange(String symbol, Instant startDate, String exchange){
        return alertSSEJpaRepository.findBySymbolAndDateRangeAndExchange(symbol, startDate, exchange);
    }

    public List<AlertEntity> findAllVolumeSpikeAlertByStatus() {
        return volumeSpikeJpaRepository.findAllVolumeSpikeAlertByStatus();
    }
}
