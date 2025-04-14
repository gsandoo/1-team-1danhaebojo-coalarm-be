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
    @Query("DELETE FROM VolumeSpikeEntity v WHERE v.alert.id IN :alertIds")
    void deleteByAlertIdIn(@Param("alertIds") List<Long> alertIds);

    @Query("    SELECT a" +
            "    FROM AlertEntity a" +
            "    JOIN FETCH a.volumeSpike v" +
            "    JOIN FETCH a.user u " +
            "    JOIN FETCH a.coin c" +
            "    WHERE a.isVolumeSpike = true")
    List<AlertEntity> findAllVolumeSpikeAlertByStatus();
}
