package _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.TargetPriceEntity;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TargetPriceResponse {
    private BigDecimal price;
    private int percentage;

    public TargetPriceResponse(TargetPriceEntity targetPrice) {
        if (targetPrice != null) {
            this.price = targetPrice.getPrice();
            this.percentage = targetPrice.getPercentage();
        }
    }
}
