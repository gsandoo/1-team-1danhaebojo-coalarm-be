package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coin_indicators")
@Getter
@NoArgsConstructor
public class CoinIndicatorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coin_indicator_id")
    private Long coinIndicatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id", nullable = false)
    private CoinEntity coin;

    @Column(name = "long_short_strength", precision = 19, scale = 6)
    private BigDecimal longShortStrength;

    @Column(name = "macd", precision = 19, scale = 6)
    private BigDecimal macd;

    @Column(name = "signal", precision = 19, scale = 6)
    private BigDecimal signal;

    @Column(name = "histogram", precision = 19, scale = 6)
    private BigDecimal histogram;

    @Column(name = "trend")
    private String trend;

    @Column(name = "rsi", precision = 19, scale = 6)
    private BigDecimal rsi;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public CoinIndicatorEntity(CoinEntity coin, BigDecimal longShortStrength, BigDecimal macd,
                               BigDecimal signal, BigDecimal histogram, String trend, BigDecimal rsi) {
        this.coin = coin;
        this.longShortStrength = longShortStrength;
        this.macd = macd;
        this.signal = signal;
        this.histogram = histogram;
        this.trend = trend;
        this.rsi = rsi;
        this.createdAt = LocalDateTime.now();
    }
}
