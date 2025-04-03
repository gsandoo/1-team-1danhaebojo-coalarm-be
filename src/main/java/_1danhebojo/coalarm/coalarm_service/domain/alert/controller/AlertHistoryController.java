package _1danhebojo.coalarm.coalarm_service.domain.alert.controller;

import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.PaginationRequest;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.alertHistory.AlertHistoryListResponse;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.alertHistory.AlertHistoryResponse;
import _1danhebojo.coalarm.coalarm_service.domain.alert.service.AlertHistoryService;
import _1danhebojo.coalarm.coalarm_service.domain.alert.service.AlertSSEService;
import _1danhebojo.coalarm.coalarm_service.domain.alert.service.AlertService;
import _1danhebojo.coalarm.coalarm_service.domain.user.service.AuthService;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alerts/history")
public class AlertHistoryController {
    private final AlertService alertService;
    private final AlertSSEService alertSSEService;
    private final AlertHistoryService alertHistoryService;
    private final AuthService authService;

    // <editor-fold desc="알람 히스토리 관련 메서드">
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAlertHistoryList(
            @RequestParam int offset,
            @RequestParam int limit
    ) {
        Long userId = authService.getLoginUserId();

        PaginationRequest paginationRequest = new PaginationRequest();
        paginationRequest.setOffset(offset);
        paginationRequest.setLimit(limit);

        AlertHistoryListResponse alertHistoryList = alertHistoryService.getAlertHistoryList(userId, paginationRequest);
        return ResponseEntity.ok(BaseResponse.success(alertHistoryList));
    }

    @GetMapping("/{alertHistoryId}")
    public ResponseEntity<BaseResponse<?>>  getAlertHistoryDetail(
            @PathVariable Long alertHistoryId) {
        AlertHistoryResponse alertHistory = alertHistoryService.getAlertHistory(alertHistoryId);
        return ResponseEntity.ok(BaseResponse.success(alertHistory));
    }

    @PostMapping("/{alert_id}")
    public ResponseEntity<?> addAlertHistory(@PathVariable("alert_id") Long alertId) {
        Long userId = authService.getLoginUserId();

        alertHistoryService.addAlertHistory(alertId, userId);
        return ResponseEntity.ok(BaseResponse.success());
    }
    // </editor-fold>
}
