package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.GoldenCrossEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoldenCrossJpaRepository extends JpaRepository<GoldenCrossEntity, Long> {
    @Modifying
    @Query("DELETE FROM GoldenCrossEntity g WHERE g.alert.id IN :alertIds")
    void deleteByAlertIdIn(@Param("alertIds") List<Long> alertIds);
}
