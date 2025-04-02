package _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.alertHistory;

import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.AlertResponse;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertHistoryEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
public class AlertHistoryResponse {
    private Long alertHistoryId;
    private Long userId;
    private AlertResponse alert;
    private LocalDateTime registeredDate;

    public AlertHistoryResponse(AlertHistoryEntity alertHistory) {
        this.alertHistoryId = alertHistory.getId();
        this.userId = alertHistory.getAlert().getUser().getId();
        this.alert = new AlertResponse(alertHistory.getAlert()); // Alert 정보 포함
        this.registeredDate = LocalDateTime.ofInstant(alertHistory.getRegDt(), ZoneId.of("Asia/Seoul"));
    }
}
