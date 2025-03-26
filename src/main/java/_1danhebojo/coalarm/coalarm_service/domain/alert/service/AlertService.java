package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.*;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.AlertResponse;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.*;
import _1danhebojo.coalarm.coalarm_service.global.api.OffsetResponse;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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

        // 해당 알람의 코인을 등록한 적이 있는지 체크
//        if(alertSSEService.isAlertSetForSymbolAndType(request.getUserId(), request.getSymbol(), request.getType()))
//        {
//            throw new RuntimeException("🚨 이미 등록된 알람 있음!");
//        }

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
                TargetPriceAlert targetPriceAlert = new TargetPriceAlert();
                targetPriceAlert.setPrice(((TargetPriceAlertRequest) request).getPrice());
                targetPriceAlert.setPercentage(((TargetPriceAlertRequest) request).getPercentage());

                Alert tartgetAlert = new Alert();
                tartgetAlert.setAlertId(alertId);
                targetPriceAlert.setAlert(tartgetAlert);

                savedAlert.setTargetPrice(targetPriceAlert);
                Long target = alertRepository.saveTargetPriceAlert(targetPriceAlert);
                if (target == null) {
                    throw new RuntimeException("Target Price Alert 저장 실패");
                }
                break;

            case "GOLDEN_CROSS":
                GoldenCrossAlert goldenCrossAlert = new GoldenCrossAlert();

                Alert goldenAlert = new Alert();
                goldenAlert.setAlertId(alertId);
                goldenCrossAlert.setAlert(goldenAlert);

                Long goldenCrossId = alertRepository.saveGoldenCrossAlert(goldenCrossAlert);
                if (goldenCrossId == null) {
                    throw new RuntimeException("Golden Cross Alert 저장 실패");
                }
                break;

            case "VOLUME_SPIKE":
                VolumeSpikeAlert volumeSpikeAlert = new VolumeSpikeAlert();
                volumeSpikeAlert.setTradingVolumeSoaring(((VolumeSpikeAlertRequest) request).getTradingVolumeSoaring());

                Alert volumeAlert = new Alert();
                volumeAlert.setAlertId(alertId);
                volumeSpikeAlert.setAlert(volumeAlert);

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

        if (request instanceof GoldenCrossAlertRequest goldenCrossRequest) {
            GoldenCrossAlert goldenCrossAlert = new GoldenCrossAlert();
            goldenCrossAlert.setAlert(alert); // 양방향 관계 연결

            alert.setGoldenCross(goldenCrossAlert);
            alert.setGoldenCrossFlag(true);
        }

        if (request instanceof TargetPriceAlertRequest targetPriceRequest) {
            TargetPriceAlert targetPriceAlert = new TargetPriceAlert();
            targetPriceAlert.setAlert(alert);
            targetPriceAlert.setPrice(((TargetPriceAlertRequest) request).getPrice()); // 필드 세팅
            targetPriceAlert.setPercentage(((TargetPriceAlertRequest) request).getPercentage());

            alert.setTargetPrice(targetPriceAlert);
            alert.setTargetPriceFlag(true);
        }

        if (request instanceof VolumeSpikeAlertRequest volumeSpikeAlertRequest) {
            VolumeSpikeAlert volumeSpikeAlert = new VolumeSpikeAlert();
            volumeSpikeAlert.setAlert(alert);
            volumeSpikeAlert.setTradingVolumeSoaring(volumeSpikeAlertRequest.getTradingVolumeSoaring());

            alert.setVolumeSpike(volumeSpikeAlert);
            alert.setVolumeSpikeFlag(true);
        }

        UserEntity user = UserEntity.builder()
                .userId(request.getUserId())
                .build();

        alert.setUser(user);

        // 우선적으로 추가 추후에 변경 필요
        if (request.getSymbol() != null) {
            Coin coin = alertRepository.findCoinBySymbol(request.getSymbol())
                    .orElseThrow(() -> new RuntimeException("해당 심볼의 코인이 존재하지 않습니다."));
            alert.setCoin(coin);
        }

        return alert;
    }

}