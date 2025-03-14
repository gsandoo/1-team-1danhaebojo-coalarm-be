package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "tickers_test")
public class TickerTestEntity {
    @EmbeddedId  // 복합 키 사용
    private TickerTestId id;

    @Column(name = "opening_price", nullable = false, precision = 18, scale = 8)
    private BigDecimal openingPrice;

    @Column(name = "high_price", nullable = false, precision = 18, scale = 8)
    private BigDecimal highPrice;

    @Column(name = "low_price", nullable = false, precision = 18, scale = 8)
    private BigDecimal lowPrice;

    @Column(name = "trade_price", nullable = false, precision = 18, scale = 8)
    private BigDecimal tradePrice; // 종가

    @Column(name = "candle_acc_trade_volume", nullable = false, precision = 24, scale = 8)
    private BigDecimal tradeVolume;

    @Column(name = "candle_acc_trade_price", nullable = false, precision = 24, scale = 8)
    private BigDecimal tradeAmount;
}
