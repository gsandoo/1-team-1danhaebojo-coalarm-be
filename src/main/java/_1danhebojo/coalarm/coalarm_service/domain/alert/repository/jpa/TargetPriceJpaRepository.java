package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.TargetPriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TargetPriceJpaRepository extends JpaRepository<TargetPriceAlert,Long> {
    @Modifying
    @Query("DELETE FROM TargetPriceAlert t WHERE t.alert.alertId = :alertId")
    void deleteByAlertId(@Param("alertId") Long alertId);

    // 지정가 알람이 있는 경우 target_price 정보 포함 조회
    @Query("    SELECT t" +
            "    FROM TargetPriceAlert t" +
            "    JOIN FETCH t.alert a" +
            "    JOIN FETCH a.user u " +
            "    JOIN FETCH a.coin c" +
            "    WHERE a.isTargetPriceFlag = true AND a.alertId = :alertId")
    Optional<TargetPriceAlert> findTargetPriceAlertsByAlertId(Long alertId);
}
