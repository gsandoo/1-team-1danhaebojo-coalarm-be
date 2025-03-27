package _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.Alert;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

    public AlertSSEResponse(Alert alert) {
        this.alertId = alert.getAlertId();
        this.title = alert.getTitle();
        this.coinName = alert.getCoin().getName();

        this.targetPriceFlag = alert.isTargetPriceFlag();
        this.goldenCrossFlag = alert.isGoldenCrossFlag();
        this.volumeSpikeFlag = alert.isVolumeSpikeFlag();

        if (alert.isTargetPriceFlag() && alert.getTargetPrice() != null) {
            this.targetPrice = new TargetPriceDetail(
                    alert.getTargetPrice().getPrice(),
                    alert.getTargetPrice().getPercentage()
            );
        }
    }

    public static class TargetPriceDetail {
        private double price;
        private double percentage;

        public TargetPriceDetail(double price, double percentage) {
            this.price = price;
            this.percentage = percentage;
        }

        public double getPrice() { return price; }
        public double getPercentage() { return percentage; }
    }
}
