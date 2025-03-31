package _1danhebojo.coalarm.coalarm_service.domain.alert.repository;

import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.GoldenCrossAlertRequest;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.TargetPriceAlertRequest;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.VolumeSpikeAlertRequest;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.Alert;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.Coin;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.TargetPriceAlert;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.VolumeSpikeAlert;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.GoldenCrossAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface AlertRepository {
    Long saveTargetPriceAlert(TargetPriceAlert targetPriceAlert);
    Long saveGoldenCrossAlert(GoldenCrossAlert goldenCrossAlert);
    Long saveVolumeSpikeAlert(VolumeSpikeAlert volumeSpikeAlert);
    List<Alert> findAll();
    Optional<Alert> findById(Long alertId);
    Optional<Alert> findByIdWithCoin(Long alertId);
    void deleteById(Long alertId);
    void deleteByUserId(Long userId);
    Alert save(Alert alert);
    Page<Alert> findAlertsByFilter(Boolean active, String filter, Pageable pageable);
    List<Alert> findActiveAlertsByUserId(Long userId);
    List<Alert> findAllActiveAlerts();
    Page<Alert> findAllUserAlerts(Long userId, String symbol, Boolean active, String sort, int offset, int limit);
    Optional<Coin> findCoinBySymbol(String symbol);
    boolean findAlertsByUserIdAndSymbolAndAlertType(Long userId, String symbol, String alertType);
}
