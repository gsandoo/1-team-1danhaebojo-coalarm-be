package _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinResponse {
    private Long id;
    private String name;
    private String symbol;

    public CoinResponse(CoinEntity coin) {
        if (coin != null) {
            this.id = coin.getId();
            this.name = coin.getName();
            this.symbol = coin.getSymbol();
        }
    }
}

