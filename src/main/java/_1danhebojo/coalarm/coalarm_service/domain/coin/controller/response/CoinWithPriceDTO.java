package _1danhebojo.coalarm.coalarm_service.domain.coin.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class CoinWithPriceDTO {
    private final Long coinId;
    private final String name;
    private final String symbol;
    private final BigDecimal price;
    private final OffsetDateTime timestamp;
}