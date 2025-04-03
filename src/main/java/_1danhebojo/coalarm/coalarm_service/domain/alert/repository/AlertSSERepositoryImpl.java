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
public class AlertSSERepositoryImpl implements AlertSSERepository {
    private final AlertSSEJpaRepository alertSSEJpaRepository;
    private final VolumeSpikeJpaRepository volumeSpikeJpaRepository;

    public List<TickerEntity> findBySymbolAndDateRangeAndExchange(String baseSymbol, Instant startDate, String exchange, String quoteSymbol) {
        return alertSSEJpaRepository.findBySymbolAndDateRangeAndExchange(baseSymbol, startDate, exchange, quoteSymbol);
    }

    public List<AlertEntity> findAllVolumeSpikeAlertByStatus() {
        return volumeSpikeJpaRepository.findAllVolumeSpikeAlertByStatus();
    }
}
