package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tickers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TickerEntity {

    @EmbeddedId
    private TickerCompositeKey id;

    @Column(name = "vwap", nullable = false)
    private BigDecimal vwap;

    @Column(name = "open", nullable = false)
    private BigDecimal open;

    @Column(name = "high", nullable = false)
    private BigDecimal high;

    @Column(name = "low", nullable = false)
    private BigDecimal low;

    @Column(name = "close", nullable = false)
    private BigDecimal close;

    @Column(name = "last", nullable = false)
    private BigDecimal last;

    @Column(name = "previous_close", nullable = false)
    private BigDecimal previousClose;

    @Column(name = "change", nullable = false)
    private BigDecimal change;

    @Column(name = "percentage", nullable = false)
    private BigDecimal percentage;

    @Column(name = "base_volume", nullable = false)
    private BigDecimal baseVolume;

    @Column(name = "quote_volume", nullable = false)
    private BigDecimal quoteVolume;
}