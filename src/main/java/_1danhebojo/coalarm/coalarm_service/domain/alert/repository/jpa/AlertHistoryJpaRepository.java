package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertHistoryJpaRepository extends JpaRepository<AlertHistory, Long> {

    // alert, coin 정보를 한 번에 가져오도록 fetch join 설정
    @Query(
            value = "SELECT ah FROM AlertHistory ah " +
                    "JOIN FETCH ah.alert a " +
                    "JOIN FETCH a.coin c " +
                    "WHERE ah.user.userId = :userId",
            countQuery = "SELECT COUNT(ah) FROM AlertHistory ah WHERE ah.user.userId = :userId"
    )
    Page<AlertHistory> findByUserId(@Param("userId") Long userId, Pageable pageable);

    // 특정 사용자(userId)에게 특정 알람(alertId)이 최근 특정 시간 내에 전송되었는지 확인
    @Query("SELECT CASE WHEN COUNT(ah) > 0 THEN TRUE ELSE FALSE END " +
            "FROM AlertHistory ah " +
            "JOIN ah.alert a " +
            "WHERE a.alertId = :alertId " +
            "AND ah.user.userId = :userId " +
            "AND ah.registeredDate >= :minutesAgo")
    boolean findRecentHistory(@Param("userId") Long userId,
                                @Param("alertId") Long alertId,
                                @Param("minutesAgo") LocalDateTime minutesAgo);

}
