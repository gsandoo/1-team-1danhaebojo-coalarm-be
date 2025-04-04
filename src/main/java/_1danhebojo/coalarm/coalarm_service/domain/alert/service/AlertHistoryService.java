package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.PaginationRequest;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.alertHistory.AlertHistoryResponse;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.alertHistory.AlertHistoryListResponse;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertHistoryRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertEntity;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertHistoryEntity;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertHistoryService {

    private final AlertHistoryRepository alertHistoryRepository;

    // 알람 리스트 조회
    @Transactional(readOnly = true)
    public AlertHistoryListResponse getAlertHistoryList(Long userId, PaginationRequest paginationRequest) {
        int offset = paginationRequest.getOffset();
        int limit = paginationRequest.getLimit();
        Pageable pageable = PageRequest.of(offset, limit);
        Page<AlertHistoryEntity> historyPage = alertHistoryRepository.findAlertHistoryByFilter(userId, pageable);

        List<AlertHistoryListResponse.AlertHistoryContent> contents = historyPage.getContent().stream()
                .map(AlertHistoryListResponse.AlertHistoryContent::new)
                .collect(Collectors.toList());

        return new AlertHistoryListResponse(
                contents,
                offset,
                limit,
                historyPage.getTotalElements(),
                historyPage.hasNext()
        );
    }

    // 알람 정보 조회
    @Transactional(readOnly = true)
    public AlertHistoryResponse getAlertHistory(Long alertHistoryId) {
        AlertHistoryEntity alertHistory = alertHistoryRepository.findById(alertHistoryId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_ALERT_HISTORY));

        return new AlertHistoryResponse(alertHistory);
    }

    // 알람 히스토리 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addAlertHistory(Long alertId, Long userId) {
        AlertEntity alert = AlertEntity.builder()
                .id(alertId)
                .build();

        UserEntity user = UserEntity.builder()
                .id(userId)
                .build();

        AlertHistoryEntity alertHistory = AlertHistoryEntity.builder()
                        .user(user)
                        .alert(alert)
                        .build();

        alertHistoryRepository.save(alertHistory);
    }
}
