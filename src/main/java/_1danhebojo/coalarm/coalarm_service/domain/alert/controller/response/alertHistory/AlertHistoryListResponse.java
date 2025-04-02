package _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.alertHistory;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertEntity;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertHistoryEntity;
import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import lombok.Getter;

import java.time.ZoneId;
import java.util.List;

import java.time.LocalDateTime;

@Getter
public class AlertHistoryListResponse {
    private final List<AlertHistoryContent> contents;
    private final int offset;
    private final int limit;

    private final long totalElements;
    private final boolean hasNext;

    public AlertHistoryListResponse(List<AlertHistoryContent> contents, int offset, int limit, long totalElements, boolean hasNext) {
        this.contents = contents;
        this.offset = offset;
        this.limit = limit;
        this.totalElements = totalElements;
        this.hasNext = hasNext;
    }

    @Getter
    public static class AlertHistoryContent {
        private final Long alertHistoryId;
        private final Long userId;
        private final CoinEntity coin;
        private final AlertInfo alert;
        private final LocalDateTime registeredDate;

        public AlertHistoryContent(AlertHistoryEntity alertHistory) {
            this.alertHistoryId = alertHistory.getId();
            this.userId = alertHistory.getUser().getId();
            this.coin = alertHistory.getAlert().getCoin(); // 코인 정보 포함
            this.alert = new AlertInfo(alertHistory.getAlert());
            this.registeredDate = LocalDateTime.ofInstant(alertHistory.getRegDt(), ZoneId.of("Asia/Seoul"));
        }
    }

    @Getter
    private static class AlertInfo {
        private Long alertId;
        private String title;

        public AlertInfo(AlertEntity alert) {
            this.alertId = alert.getId();
            this.title = alert.getTitle();
        }
    }
}