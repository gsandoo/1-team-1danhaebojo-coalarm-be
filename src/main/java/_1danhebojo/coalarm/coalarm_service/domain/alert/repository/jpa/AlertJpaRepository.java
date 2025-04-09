package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.*;
import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertJpaRepository extends JpaRepository<AlertEntity, Long> {

    @Query("SELECT a " +
            "FROM AlertEntity a " +
            "JOIN FETCH a.coin c " +
            "JOIN FETCH a.user u " +
            "WHERE (:active IS NULL OR a.active = :active) " +
            "AND (:filter IS NULL OR c.symbol = :filter)")
    Page<AlertEntity> findAlertsByFilter(@Param("active") Boolean active, @Param("filter") String filter, Pageable pageable);

    // 사용자가 등록한 모든 활성화된 알람 조회
    @Query("SELECT a " +
            "FROM AlertEntity a " +
            "JOIN FETCH a.coin c " +
            "JOIN FETCH a.user u " +
            "WHERE a.user.id = :userId AND a.active = true")
    List<AlertEntity> findActiveAlertsByUserId(Long userId);

    @Query("SELECT a " +
            "FROM AlertEntity a " +
            "JOIN FETCH a.coin c " +
            "JOIN FETCH a.user u " +
            "LEFT JOIN FETCH a.targetPrice " +
            "LEFT JOIN FETCH a.goldenCross " +
            "LEFT JOIN FETCH a.volumeSpike " +
            "WHERE a.active = true")
    List<AlertEntity> findAllActiveAlerts();

    @Modifying
    @Query("DELETE FROM AlertEntity a WHERE a.user.id = :userId")
    void deleteAlertByUserId(Long userId);

    @Query("SELECT c " +
            "FROM CoinEntity c " +
            "WHERE c.symbol = :symbol")
    Optional<CoinEntity> findCoinBySymbol(String symbol);

    @Query("SELECT a FROM AlertEntity a JOIN FETCH a.coin WHERE a.id = :alertId")
    Optional<AlertEntity> findByIdWithCoin(Long alertId);
}
