package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.Alert;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.GoldenCrossAlertRequest;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.TargetPriceAlertRequest;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.VolumeSpikeAlertRequest;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.*;
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
public interface AlertJpaRepository extends JpaRepository<Alert, Long> {

    @Query("SELECT a " +
            "FROM Alert a " +
            "JOIN FETCH a.coin c " +
            "JOIN FETCH a.user u " +
            "WHERE (:active IS NULL OR a.active = :active) " +
            "AND (:filter IS NULL OR c.symbol = :filter)")
    Page<Alert> findAlertsByFilter(@Param("active") Boolean active, @Param("filter") String filter, Pageable pageable);

    // 사용자가 등록한 모든 활성화된 알람 조회
    @Query("SELECT a " +
            "FROM Alert a " +
            "JOIN FETCH a.coin c " +
            "JOIN FETCH a.user u " +
            "WHERE a.user.userId = :userId AND a.active = true")
    List<Alert> findActiveAlertsByUserId(Long userId);

    @Query("SELECT a " +
            "FROM Alert a " +
            "JOIN FETCH a.coin c " +
            "JOIN FETCH a.user u " +
            "WHERE a.active = true")
    List<Alert> findAllActiveAlerts();

    @Modifying
    @Query("DELETE FROM Alert a WHERE a.user.userId = :userId")
    void deleteAlertByUserId(Long userId);

    @Query("SELECT c " +
            "FROM Coin c " +
            "WHERE c.symbol = :symbol")
    Optional<Coin> findCoinBySymbol(String symbol);
}
