package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.GoldenCrossAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoldenCrossJpaRepository extends JpaRepository<GoldenCrossAlert, Long> {
    @Modifying
    @Query("DELETE FROM TargetPriceAlert t WHERE t.alert.alertId = :alertId")
    void deleteByAlertId(@Param("alertId") Long alertId);

    // 골든 크로스 감지가 있는 경우 golden_cross 정보 포함 조회
    @Query("    SELECT t" +
            "    FROM GoldenCrossAlert t" +
            "    JOIN FETCH t.alert a" +
//            "    JOIN FETCH a.user u " +
            "    JOIN FETCH a.coin c" +
            "    WHERE a.isGoldenCrossFlag = true AND a.alertId = :alertId")
    Optional<GoldenCrossAlert> findGoldenCrossAlertsByAlertId(Long alertId);
}
