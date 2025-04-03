package _1danhebojo.coalarm.coalarm_service.domain.alert.repository;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AlertSSERepository {

    List<TickerEntity> findBySymbolAndDateRangeAndExchange(String baseSymbol, Instant startDate, String exchange, String quoteSymbol);

    List<AlertEntity> findAllVolumeSpikeAlertByStatus();
}
