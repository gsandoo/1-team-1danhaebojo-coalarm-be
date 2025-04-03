package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.TargetPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TargetPriceJpaRepository extends JpaRepository<TargetPriceEntity,Long> {
    @Modifying
    @Query("DELETE FROM TargetPriceEntity t WHERE t.alert.id = :alertId")
    void deleteByAlertId(@Param("alertId") Long alertId);

    // 지정가 알람이 있는 경우 target_price 정보 포함 조회
    @Query("    SELECT t" +
            "    FROM TargetPriceEntity t" +
            "    JOIN FETCH t.alert a" +
            "    JOIN FETCH a.user u " +
            "    JOIN FETCH a.coin c" +
            "    WHERE a.isTargetPrice = true AND a.id = :alertId")
    Optional<TargetPriceEntity> findTargetPriceAlertsByAlertId(Long alertId);
}
