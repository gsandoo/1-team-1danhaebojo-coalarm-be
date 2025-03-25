package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.*;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.AlertListResponse;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.AlertResponse;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertRepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.Coin;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.Alert;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepositoryImpl alertRepositoryImpl;
    @Lazy
    @Autowired
    private final AlertSSEService alertSSEService;

    // 알람 추가
    @Transactional
    public void addAlert(BaseAlertRequest request) {
        Alert alert = convertToAlertEntity(request);
        Alert savedAlert = alertRepositoryImpl.save(alert);

        Optional<Alert> checkAlert = alertRepositoryImpl.findById(alert.getAlertId());
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

                Long target = alertRepositoryImpl.saveTargetPriceAlert(targetPriceAlert);
                if (target == null) {
                    throw new RuntimeException("Target Price Alert 저장 실패");
                }
                break;

            case "GOLDEN_CROSS":
                GoldenCrossAlertRequest goldenCrossAlert = (GoldenCrossAlertRequest) request;
                goldenCrossAlert.setIsGoldenCross(true);
                goldenCrossAlert.setAlertId(alertId);

                Long goldenCrossId = alertRepositoryImpl.saveGoldenCrossAlert(goldenCrossAlert);
                if (goldenCrossId == null) {
                    throw new RuntimeException("Golden Cross Alert 저장 실패");
                }
                break;

            case "VOLUME_SPIKE":
                VolumeSpikeAlertRequest volumeSpikeAlert = (VolumeSpikeAlertRequest) request;
                volumeSpikeAlert.setAlertId(alertId);
                volumeSpikeAlert.setIsTradingVolumeSoaring(true);

                Long volumeSpikeId = alertRepositoryImpl.saveVolumeSpikeAlert(volumeSpikeAlert);
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
    @Transactional
    public Long updateAlertStatus(Long alertId, boolean active) {
        Alert alert = alertRepositoryImpl.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        boolean isActive = alert.isActive();

        if(active != isActive) {
            alert.setActive(active);
            Alert saveAlert = alertRepositoryImpl.save(alert);
            if (active) {
                alertSSEService.addEmitter(saveAlert.getUser().getUserId(), alert);
            } else {
                alertSSEService.deleteEmitter(saveAlert.getUser().getUserId(), alert);
            }
        }

        return alert.getAlertId();
    }

    // 알람 삭제
    @Transactional
    public void deleteAlert(Long alertId) {
        Alert alert = alertRepositoryImpl.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        alertSSEService.deleteEmitter(alert.getUser().getUserId(), alert);
        alertRepositoryImpl.deleteById(alertId);
    }

    // 알람 목록 조회
    public AlertListResponse getAllAlerts(AlertFilterRequest request, long userId) {
        // 정렬 방식 설정
        Sort sort = request.getSort().equalsIgnoreCase("LATEST")
                ? Sort.by(Sort.Direction.DESC, "regDt")
                : Sort.by(Sort.Direction.ASC, "regDt");

        PageRequest pageRequest = PageRequest.of(request.getOffset(), request.getLimit(), sort);

        // `active`가 null이면 전체 조회, 아니면 필터링 적용
        Boolean active = request.getActive();

        Page<Alert> alerts = alertRepositoryImpl.findAlertsByFilter(active, request.getFilter(), pageRequest, userId);

        List<AlertResponse> alertResponses = alerts.getContent().stream()
                .map(AlertResponse::new)
                .collect(Collectors.toList());

        return new AlertListResponse(
                alertResponses,
                request.getOffset(),
                request.getLimit(),
                alerts.getTotalElements(),
                alerts.hasNext()
        );
    }

    private Alert convertToAlertEntity(BaseAlertRequest request) {
        Alert alert = new Alert();
        alert.setActive(request.getActive());
        alert.setTitle(request.getTitle());
        alert.setGoldenCross(request instanceof GoldenCrossAlertRequest);
        alert.setTargetPrice(request instanceof TargetPriceAlertRequest);
        alert.setVolumeSpike(request instanceof VolumeSpikeAlertRequest);
        alert.setUserId(request.getUserId());

        // 우선적으로 추가 추후에 변경 필요
        if (request.getSymbol() != null) {
            Coin coin = alertRepositoryImpl.findCoinBySymbol(request.getSymbol())
                    .orElseThrow(() -> new RuntimeException("해당 심볼의 코인이 존재하지 않습니다."));
            alert.setCoin(coin);
        }

        return alert;
    }

}