package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlertSSEJpaRepository extends JpaRepository<AlertEntity, Long> {

    @Query("SELECT a " +
            "FROM AlertEntity a " +
            "JOIN FETCH a.coin " +
            "JOIN FETCH a.user u " +
            "WHERE a.user.id = :userId " +
            "AND a.active = true")
    List<AlertEntity> findByUserId(Long userId);

    @Query("SELECT MAX(h.regDt) FROM AlertHistoryEntity h WHERE h.alert.id = :alertId")
    LocalDateTime findLastAlertTime(Long alertId);

    @Query("SELECT a " +
            "FROM AlertEntity a " +
            "JOIN FETCH a.user u " +
            "WHERE a.user.id = :userId " +
            "AND a.active = true")
    List<AlertEntity> findActiveAlertsByUserId(Long userId);

    // <editor-fold desc="티커 데이터 관련 조회">
    // 최신 티커 데이터 조회 (특정 심볼 기준)
    @Query("SELECT t FROM TickerEntity t WHERE t.id.baseSymbol = :symbol AND t.id.exchange=:exchange ORDER BY t.id.timestamp DESC LIMIT 1")
    Optional<TickerEntity> findLatestBySymbol(String symbol, String exchange);

    // 특정 거래소의 최신 티커 조회
    @Query("SELECT t FROM TickerEntity t WHERE t.id.exchange = :exchange AND t.id.baseSymbol = :symbol ORDER BY t.id.timestamp DESC LIMIT 1")
    Optional<TickerEntity> findLatestByExchangeAndSymbol(String exchange, String symbol);

    // 최신 티커 데이터 조회 (특정 심볼, 최근 N개)
    @Query("SELECT t FROM TickerEntity t WHERE t.id.baseSymbol = :symbol AND t.id.timestamp >= :startDate ORDER BY t.id.timestamp ASC")
    List<TickerEntity> findBySymbolAndDateRange(String symbol, Instant startDate);

    // 최신 티커 데이터 조회 (특정 심볼, 최근 N개, 거래소)
    @Query("SELECT t FROM TickerEntity t WHERE t.id.baseSymbol = :baseSymbol AND t.id.timestamp >= :startDate AND t.id.exchange=:exchange AND t.id.quoteSymbol = :quoteSymbol ORDER BY t.id.timestamp ASC")
    List<TickerEntity> findBySymbolAndDateRangeAndExchange(String baseSymbol, Instant startDate, String exchange, String quoteSymbol);
    // </editor-fold>
}
