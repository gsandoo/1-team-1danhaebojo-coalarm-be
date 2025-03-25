package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.KimchiPremiumEntity;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ResponseKimchiPremium {
    private final Long premiumId;
    private final BigDecimal domesticPrice;
    private final BigDecimal globalPrice;
    private final BigDecimal exchangeRate;
    private final BigDecimal kimchiPremium;
    private final BigDecimal dailyChange;
    private final CoinDTO coin;

    // 생성자
    public ResponseKimchiPremium(KimchiPremiumEntity entity) {
        this.premiumId = entity.getId();
        this.domesticPrice = entity.getDomesticPrice();
        this.globalPrice = entity.getGlobalPrice();
        this.exchangeRate = entity.getExchangeRate();
        this.kimchiPremium = entity.getKimchiPremium();
        this.dailyChange = entity.getDailyChange();
        this.coin = new CoinDTO(entity.getCoin());
    }

    // 정적 팩토리 메서드 추가 (fromEntity)
    public static ResponseKimchiPremium fromEntity(KimchiPremiumEntity entity) {
        return new ResponseKimchiPremium(entity);
    }

}
