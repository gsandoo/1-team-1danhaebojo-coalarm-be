package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response;


import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import lombok.Getter;

@Getter
public class CoinDTO {
    private final Long coinId;
    private final String name;
    private final String symbol;

    public CoinDTO(CoinEntity entity) {
        this.coinId = entity.getCoinId();
        this.name = entity.getName();
        this.symbol = entity.getSymbol();
    }
}
