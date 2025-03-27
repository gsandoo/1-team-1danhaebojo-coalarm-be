package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.*;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.AlertResponse;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.*;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.UserRepository;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.UserRepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.domain.user.service.UserServiceImpl;
import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
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
    private final UserRepository userRepository;

    // 알람 추가
    public void addAlert(BaseAlertRequest request) {
        // 해당 알람의 코인을 등록한 적이 있는지 체크
        boolean checkAlerts = alertRepository.findAlertsByUserIdAndSymbolAndAlertType(request.getUserId(), request.getSymbol(), request.getType());
        if (checkAlerts) {
            throw new ApiException(AppHttpStatus.ALREADY_EXISTS_ALERT);
        }

        UserEntity getMyInfo = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_USER));

        Alert alert = convertToAlertEntity(request);
        Alert savedAlert = alertRepository.save(alert);

        alert.setUser(getMyInfo);

        Long alertId = savedAlert.getAlertId();
        if (alertId == null) {
            throw new ApiException(AppHttpStatus.FAILED_TO_SAVE_ALERT);
        }

        alertSSEService.addEmitter(request.getUserId(), alert);
    }

    // 알람 활성화 수정
    public Long updateAlertStatus(Long alertId, boolean active) {
        Alert alert = alertRepository.findByIdWithCoin(alertId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_ALERT));
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
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_ALERT));

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
                    .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_COIN));
            alert.setCoin(coin);
        }

        return alert;
    }

}