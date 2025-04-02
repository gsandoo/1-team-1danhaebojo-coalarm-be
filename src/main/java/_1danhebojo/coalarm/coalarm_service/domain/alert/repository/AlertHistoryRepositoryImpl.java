package _1danhebojo.coalarm.coalarm_service.domain.alert.repository;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertHistoryEntity;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.AlertHistoryJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AlertHistoryRepositoryImpl {

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
}