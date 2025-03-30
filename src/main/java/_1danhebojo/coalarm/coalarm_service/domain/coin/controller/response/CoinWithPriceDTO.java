package _1danhebojo.coalarm.coalarm_service.domain.coin.controller.response;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CoinWithPriceDTO {
    private final Long coinId;
    private final String name;
    private final String symbol;
    private final BigDecimal price;

    public CoinWithPriceDTO(CoinEntity coin, BigDecimal price) {
        this.coinId = coin.getCoinId();
        this.name = coin.getName();
        this.symbol = coin.getSymbol();
        this.price = price;
    }
}