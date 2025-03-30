package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "temp_ticker")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TickerEntity {

    @EmbeddedId
    private TickerCompositeKey id;

    @Column(name = "open", nullable = false, precision = 20, scale = 8)
    private BigDecimal open;

    @Column(name = "high", nullable = false, precision = 20, scale = 8)
    private BigDecimal high;

    @Column(name = "low", nullable = false, precision = 20, scale = 8)
    private BigDecimal low;

    @Column(name = "close", nullable = false, precision = 20, scale = 8)
    private BigDecimal close;

    @Column(name = "last", nullable = false, precision = 20, scale = 8)
    private BigDecimal last;

    @Column(name = "previous_close", nullable = false, precision = 20, scale = 8)
    private BigDecimal previousClose;

    @Column(name = "change", nullable = false, precision = 20, scale = 8)
    private BigDecimal change;

    @Column(name = "percentage", nullable = false, precision = 10, scale = 8)
    private BigDecimal percentage;

    @Column(name = "base_volume", nullable = false, precision = 30, scale = 12)
    private BigDecimal baseVolume;

    @Column(name = "quote_volume", nullable = false, precision = 30, scale = 8)
    private BigDecimal quoteVolume;
}