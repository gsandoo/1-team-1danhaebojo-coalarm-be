package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.*;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.AlertResponse;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertHistoryRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.*;
import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;

import _1danhebojo.coalarm.coalarm_service.domain.user.repository.UserRepository;

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

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertHistoryRepository alertHistoryRepository;
    @Lazy
    @Autowired
    private final AlertSSEService alertSSEService;
    private final UserRepository userRepository;

    // 알람 추가
    public AlertResponse addAlert(BaseAlertRequest request) {
        // 해당 알람의 코인을 등록한 적이 있는지 체크
        boolean checkAlerts = alertRepository.findAlertsByUserIdAndSymbolAndAlertType(request.getUserId(), request.getSymbol(), request.getType());
        if (checkAlerts) {
            throw new ApiException(AppHttpStatus.ALREADY_EXISTS_ALERT);
        }

        UserEntity getMyInfo = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_USER));

        AlertEntity alert = convertToAlertEntity(request);
        AlertEntity savedAlert = alertRepository.save(alert);

        alert.setUser(getMyInfo);

        Long alertId = savedAlert.getId();
        if (alertId == null) {
            throw new ApiException(AppHttpStatus.FAILED_TO_SAVE_ALERT);
        }

        alertSSEService.addEmitter(request.getUserId(), alert);

        return new AlertResponse(alert);
    }

    // 알람 활성화 수정
    public Long updateAlertStatus(Long alertId, boolean active) {
        AlertEntity alert = alertRepository.findByIdWithCoin(alertId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_ALERT));
        boolean isActive = alert.getActive();

        if(active != isActive) {
            alert.setActive(active);
            AlertEntity saveAlert = alertRepository.save(alert);
            if (active) {
                alertSSEService.addEmitter(saveAlert.getUser().getId(), alert);
            } else {
                alertSSEService.deleteEmitter(saveAlert.getUser().getId(), alert);
            }
        }

        return alert.getId();
    }

    // 알람 삭제
    public void deleteAlert(Long alertId) {
        AlertEntity alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_ALERT));
        Long userId = alert.getUser().getId();

        alertSSEService.deleteEmitter(userId, alert);

        alertHistoryRepository.deleteByUserId(userId);
        alertRepository.deleteById(alertId);
    }

    // 알람 목록 조회
    @Transactional(readOnly = true)
    public OffsetResponse<AlertResponse> getMyAlerts(Long userId, String symbol, Boolean active, String sort, int offset, int limit) {
        Page<AlertEntity> alerts = alertRepository.findAllUserAlerts(userId, symbol, active, sort, offset, limit);

        return OffsetResponse.of(
                alerts.getContent().stream()
                        .map(AlertResponse::new)
                        .toList(),
                offset,
                limit,
                alerts.getTotalElements()
        );
    }

    private AlertEntity convertToAlertEntity(BaseAlertRequest request) {
        AlertEntity alert = new AlertEntity();
        alert.setActive(request.getActive());
        alert.setTitle(request.getTitle());

        if (request instanceof GoldenCrossAlertRequest goldenCrossRequest) {
            GoldenCrossEntity goldenCrossAlert = GoldenCrossEntity.builder()
                    .alert(alert)
                    .build();

            alert.setGoldenCross(goldenCrossAlert);
            alert.setIsGoldenCross(true);
        }

        if (request instanceof TargetPriceAlertRequest targetPriceRequest) {
            TargetPriceEntity targetPriceAlert = TargetPriceEntity.builder()
                    .alert(alert)
                    .price(targetPriceRequest.getPrice())
                    .percentage(targetPriceRequest.getPercentage())
                    .build();

            alert.setTargetPrice(targetPriceAlert);
            alert.setIsTargetPrice(true);
        }

        if (request instanceof VolumeSpikeAlertRequest volumeSpikeAlertRequest) {
            VolumeSpikeEntity volumeSpikeAlert = VolumeSpikeEntity.builder()
                    .alert(alert)
                    .tradingVolumeSoaring(volumeSpikeAlertRequest.getTradingVolumeSoaring())
                    .build();

            alert.setVolumeSpike(volumeSpikeAlert);
            alert.setIsVolumeSpike(true);
        }

        UserEntity user = UserEntity.builder()
                .id(request.getUserId())
                .build();

        alert.setUser(user);

        // 우선적으로 추가 추후에 변경 필요
        if (request.getSymbol() != null) {
            CoinEntity coin = alertRepository.findCoinBySymbol(request.getSymbol())
                    .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_COIN));
            alert.setCoin(coin);
        }

        return alert;
    }

}