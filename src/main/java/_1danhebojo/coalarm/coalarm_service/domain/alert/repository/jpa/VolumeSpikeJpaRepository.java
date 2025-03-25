package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.VolumeSpikeAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolumeSpikeJpaRepository extends JpaRepository<VolumeSpikeAlert, Long> {
    @Modifying
    @Query("DELETE FROM TargetPriceAlert t WHERE t.alert.alertId = :alertId")
    void deleteByAlertId(@Param("alertId") Long alertId);

    // 거래량 급등 감지가 있는 경우 volume_spike 정보 포함 조회
    @Query("    SELECT t" +
            "    FROM VolumeSpikeAlert t" +
            "    JOIN FETCH t.alert a" +
            "    JOIN FETCH a.coin c" +
            "    WHERE a.isVolumeSpike = true AND a.alertId = :alertId")
    Optional<VolumeSpikeAlert> findVolumeSpikeAlertByAlertId(Long alertId);

    @Query("    SELECT t" +
            "    FROM VolumeSpikeAlert t" +
            "    JOIN FETCH t.alert a" +
            "    JOIN FETCH a.user u " +
            "    JOIN FETCH a.coin c" +
            "    WHERE a.isVolumeSpike = true")
    List<VolumeSpikeAlert> findAllVolumeSpikeAlertByStatus();
}
