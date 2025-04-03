package _1danhebojo.coalarm.coalarm_service.domain.alert.repository;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertHistoryEntity;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.AlertHistoryJpaRepository;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.List;

import static _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.QAlertHistoryEntity.alertHistoryEntity;

@Repository
@RequiredArgsConstructor
public class AlertHistoryRepositoryImpl implements AlertHistoryRepository {
    private final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");
    private final AlertHistoryJpaRepository alertHistoryJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Page<AlertHistoryEntity> findAlertHistoryByFilter(Long userId, Pageable pageable) {
        return alertHistoryJpaRepository.findByUserId(userId, pageable);
    }

    @Transactional
    public void save(AlertHistoryEntity alertHistory) {
        alertHistoryJpaRepository.save(alertHistory);
    }

    public Optional<AlertHistoryEntity> findById(Long alertHistoryId) {
        return alertHistoryJpaRepository.findById(alertHistoryId);
    }

    public boolean findRecentHistory(Long userId, Long alertId, LocalDateTime minutesAgo) {
        return alertHistoryJpaRepository.findRecentHistory(userId, alertId, minutesAgo);
    }

    // LocalDateTime → Instant 변환 메서드
    private Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZONE_ID).toInstant();
    }
    @Override
    public List<Long> findRecentHistories(LocalDateTime minutesAgo) {
        return new JPAQuery<Long>(entityManager)
                .select(alertHistoryEntity.alert.id)
                .from(alertHistoryEntity)
                .where(
                        alertHistoryEntity.regDt.goe(toInstant(minutesAgo))
                )
                .fetch();
    }

    public List<Long> findRecentAlertIdsByUser(Long userId, LocalDateTime since) {

        return new JPAQuery<Long>(entityManager)
                .select(alertHistoryEntity.alert.id)
                .from(alertHistoryEntity)
                .where(
                        alertHistoryEntity.user.id.eq(userId),
                        alertHistoryEntity.regDt.goe(toInstant(since))
                )
                .fetch();
    }
}