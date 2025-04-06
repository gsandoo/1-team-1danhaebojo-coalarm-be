package _1danhebojo.coalarm.coalarm_service.domain.alert.repository;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AlertHistoryRepository {
    Page<AlertHistoryEntity> findAlertHistoryByFilter(Long userId, Pageable pageable);
    void save(AlertHistoryEntity alertHistory);
    Optional<AlertHistoryEntity> findById(Long alertHistoryId);
    boolean findRecentHistory(Long userId, Long alertId, LocalDateTime minutesAgo);
    List<Long> findRecentHistories(LocalDateTime minutesAgo);
    List<Long> findRecentAlertIdsByUser(Long userId, LocalDateTime since);

    List<AlertHistoryEntity> findAllByAlertId(Long alertId);

    void deleteAll(List<AlertHistoryEntity> alertHistories);
}
