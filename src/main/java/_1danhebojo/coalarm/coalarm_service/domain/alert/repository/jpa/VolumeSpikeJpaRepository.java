package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertEntity;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.VolumeSpikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolumeSpikeJpaRepository extends JpaRepository<VolumeSpikeEntity, Long> {
    @Modifying
    @Query("DELETE FROM VolumeSpikeEntity t WHERE t.alert.id = :alertId")
    void deleteByAlertId(@Param("alertId") Long alertId);

    // 거래량 급등 감지가 있는 경우 volume_spike 정보 포함 조회
    @Query("    SELECT t" +
            "    FROM VolumeSpikeEntity t" +
            "    JOIN FETCH t.alert a" +
            "    JOIN FETCH a.coin c" +
            "    WHERE a.isVolumeSpike = true AND a.id = :alertId")
    Optional<VolumeSpikeEntity> findVolumeSpikeAlertByAlertId(Long alertId);

    @Query("    SELECT a" +
            "    FROM AlertEntity a" +
            "    JOIN FETCH a.volumeSpike v" +
            "    JOIN FETCH a.user u " +
            "    JOIN FETCH a.coin c" +
            "    WHERE a.isVolumeSpike = true")
    List<AlertEntity> findAllVolumeSpikeAlertByStatus();
}
