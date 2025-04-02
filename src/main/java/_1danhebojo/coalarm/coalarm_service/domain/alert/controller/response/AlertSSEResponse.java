package _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AlertSSEResponse {
    private Long alertId;
    private String title;
    private String coinName;

    private boolean targetPriceFlag;
    private boolean goldenCrossFlag;
    private boolean volumeSpikeFlag;

    private TargetPriceDetail targetPrice;

    public AlertSSEResponse(AlertEntity alert) {
        this.alertId = alert.getId();
        this.title = alert.getTitle();
        this.coinName = alert.getCoin().getName();

        this.targetPriceFlag = alert.getIsTargetPrice();
        this.goldenCrossFlag = alert.getIsGoldenCross();
        this.volumeSpikeFlag = alert.getIsVolumeSpike();

        if (alert.getIsTargetPrice() && alert.getTargetPrice() != null) {
            this.targetPrice = new TargetPriceDetail(
                    alert.getTargetPrice().getPrice(),
                    alert.getTargetPrice().getPercentage()
            );
        }
    }

    public static class TargetPriceDetail {
        private BigDecimal price;
        private double percentage;

        public TargetPriceDetail(BigDecimal price, double percentage) {
            this.price = price;
            this.percentage = percentage;
        }

        public BigDecimal getPrice() { return price; }
        public double getPercentage() { return percentage; }
    }
}
