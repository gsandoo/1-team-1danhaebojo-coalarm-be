package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "candles")
@Getter
@NoArgsConstructor
public class CandleEntity {

    @EmbeddedId
    private CandleCompositeKey id;

    @Column(name = "open", nullable = false, precision = 20, scale = 8)
    private BigDecimal open;

    @Column(name = "high", nullable = false, precision = 20, scale = 8)
    private BigDecimal high;

    @Column(name = "low", nullable = false, precision = 20, scale = 8)
    private BigDecimal low;

    @Column(name = "close", nullable = false, precision = 20, scale = 8)
    private BigDecimal close;

    @Column(name = "volume", nullable = false, precision = 30, scale = 12)
    private BigDecimal volume;

    @Builder
    public CandleEntity(CandleCompositeKey id, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, BigDecimal volume) {
        this.id = id;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
}
