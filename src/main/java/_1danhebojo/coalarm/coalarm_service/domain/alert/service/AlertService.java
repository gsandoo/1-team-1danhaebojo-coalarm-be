package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.*;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.AlertResponse;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.Coin;
import _1danhebojo.coalarm.coalarm_service.global.api.OffsetResponse;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.Alert;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    @Lazy
    @Autowired
    private final AlertSSEService alertSSEService;

    // 알람 추가
    public void addAlert(BaseAlertRequest request) {
        Alert alert = convertToAlertEntity(request);
        Alert savedAlert = alertRepository.save(alert);

        Optional<Alert> checkAlert = alertRepository.findById(alert.getAlertId());
        if (checkAlert.isEmpty()) {
            throw new RuntimeException("🚨 flush() 후에도 저장 안 됨!");
        }

        Long alertId = savedAlert.getAlertId();
        if (alertId == null) {
            throw new RuntimeException("Alert 저장 실패");
        }

        switch (request.getType()) {
            case "TARGET_PRICE":
                TargetPriceAlertRequest targetPriceAlert = (TargetPriceAlertRequest) request;
                targetPriceAlert.setIsTargetPrice(true);
                targetPriceAlert.setAlertId(alertId);

                Long target = alertRepository.saveTargetPriceAlert(targetPriceAlert);
                if (target == null) {
                    throw new RuntimeException("Target Price Alert 저장 실패");
                }
                break;

            case "GOLDEN_CROSS":
                GoldenCrossAlertRequest goldenCrossAlert = (GoldenCrossAlertRequest) request;
                goldenCrossAlert.setIsGoldenCross(true);
                goldenCrossAlert.setAlertId(alertId);

                Long goldenCrossId = alertRepository.saveGoldenCrossAlert(goldenCrossAlert);
                if (goldenCrossId == null) {
                    throw new RuntimeException("Golden Cross Alert 저장 실패");
                }
                break;

            case "VOLUME_SPIKE":
                VolumeSpikeAlertRequest volumeSpikeAlert = (VolumeSpikeAlertRequest) request;
                volumeSpikeAlert.setAlertId(alertId);
                volumeSpikeAlert.setIsTradingVolumeSoaring(true);

                Long volumeSpikeId = alertRepository.saveVolumeSpikeAlert(volumeSpikeAlert);
                if (volumeSpikeId == null) {
                    throw new RuntimeException("Volume Spike Alert 저장 실패");
                }
                break;

            default:
                throw new IllegalArgumentException("잘못된 알람 타입: " + request.getType());
        }
        alertSSEService.addEmitter(request.getUserId(), checkAlert.get());
    }

    // 알람 활성화 수정
    public Long updateAlertStatus(Long alertId, boolean active) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        boolean isActive = alert.isActive();

        if(active != isActive) {
            alert.setActive(active);
            Alert saveAlert = alertRepository.save(alert);
            if (active) {
                alertSSEService.addEmitter(saveAlert.getUser().getUserId(), alert);
            } else {
                alertSSEService.deleteEmitter(saveAlert.getUser().getUserId(), alert);
            }
        }

        return alert.getAlertId();
    }

    // 알람 삭제
    public void deleteAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        alertSSEService.deleteEmitter(alert.getUser().getUserId(), alert);
        alertRepository.deleteById(alertId);
    }

    // 알람 목록 조회
    @Transactional(readOnly = true)
    public OffsetResponse<AlertResponse> getMyAlerts(Long userId, String symbol, Boolean active, String sort, int offset, int limit) {


        Page<Alert> alerts = alertRepository.findAllUserAlerts(userId, symbol, active, sort, offset, limit);

        return OffsetResponse.of(
                alerts.getContent().stream()
                        .map(AlertResponse::new)
                        .toList(),
                offset,
                limit,
                alerts.getTotalElements()
        );
    }

    private Alert convertToAlertEntity(BaseAlertRequest request) {
        Alert alert = new Alert();
        alert.setActive(request.getActive());
        alert.setTitle(request.getTitle());
        alert.setGoldenCross(request instanceof GoldenCrossAlertRequest);
        alert.setTargetPrice(request instanceof TargetPriceAlertRequest);
        alert.setVolumeSpike(request instanceof VolumeSpikeAlertRequest);
        if (request.getCoinId() != null) {
            Coin coin = new Coin();
            coin.setCoinId(request.getCoinId());
            alert.setCoin(coin);
        }
        UserEntity user = UserEntity.builder()
                .userId(1L)
                .build();

        alert.setUser(user);

        return alert;
    }
}