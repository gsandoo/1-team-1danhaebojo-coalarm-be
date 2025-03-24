package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.Alert;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlertSSEJpaRepository extends JpaRepository<Alert, Long> {

    @Query("SELECT a " +
            "FROM Alert a " +
            "JOIN FETCH a.coin " +
            "JOIN FETCH a.user u " +
            "WHERE a.user.userId = :userId " +
            "AND a.active = true")
    List<Alert> findByUserId(Long userId);

    @Query("SELECT MAX(h.registeredDate) FROM AlertHistory h WHERE h.alert.alertId = :alertId")
    LocalDateTime findLastAlertTime(Long alertId);

    @Query("SELECT a " +
            "FROM Alert a " +
            "JOIN FETCH a.user u " +
            "WHERE a.user.userId = :userId " +
            "AND a.active = true")
    List<Alert> findActiveAlertsByUserId(Long userId);

    // <editor-fold desc="티커 데이터 관련 조회">
    // 최신 티커 데이터 조회 (특정 심볼 기준)
    @Query("SELECT t FROM Ticker t WHERE t.symbol = :symbol AND t.exchange=:exchange ORDER BY t.timestamp DESC LIMIT 1")
    Optional<Ticker> findLatestBySymbol(String symbol, String exchange);

    // 특정 거래소의 최신 티커 조회
    @Query("SELECT t FROM Ticker t WHERE t.exchange = :exchange AND t.symbol = :symbol ORDER BY t.timestamp DESC LIMIT 1")
    Optional<Ticker> findLatestByExchangeAndSymbol(String exchange, String symbol);

    // 최신 티커 데이터 조회 (특정 심볼, 최근 N개)
    @Query("SELECT t FROM Ticker t WHERE t.symbol = :symbol AND t.timestamp >= :startDate ORDER BY t.timestamp ASC")
    List<Ticker> findBySymbolAndDateRange(String symbol, Instant startDate);

    // 최신 티커 데이터 조회 (특정 심볼, 최근 N개, 거래소)
    @Query("SELECT t FROM Ticker t WHERE t.symbol = :symbol AND t.timestamp >= :startDate AND t.exchange=:exchange ORDER BY t.timestamp ASC")
    List<Ticker> findBySymbolAndDateRangeAndExchange(String symbol, Instant startDate, String exchange);
    // </editor-fold>
}
