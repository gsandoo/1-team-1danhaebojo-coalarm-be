package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "coin_indicators")
@Getter
@NoArgsConstructor
public class CoinIndicatorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "long_strength", nullable = false, precision = 19, scale = 6)
    private BigDecimal longStrength;

    @Column(name = "macd", nullable = false, precision = 19, scale = 6)
    private BigDecimal macd;

    @Column(name = "signal", nullable = false, precision = 19, scale = 6)
    private BigDecimal signal;

    @Column(name = "histogram", nullable = false, precision = 19, scale = 6)
    private BigDecimal histogram;

    @Column(name = "trend", nullable = false)
    private String trend;

    @Column(name = "rsi", nullable = false, precision = 19, scale = 6)
    private BigDecimal rsi;

    @Column(name = "reg_dt", nullable = false, updatable = false)
    private Instant regDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id", nullable = false)
    private CoinEntity coin;

    @Builder
    public CoinIndicatorEntity(Long id, BigDecimal longStrength, BigDecimal macd, BigDecimal signal, BigDecimal histogram, String trend, BigDecimal rsi, Instant regDt, CoinEntity coin) {
        this.id = id;
        this.longStrength = longStrength;
        this.macd = macd;
        this.signal = signal;
        this.histogram = histogram;
        this.trend = trend;
        this.rsi = rsi;
        this.regDt = regDt;
        this.coin = coin;
    }

    @PrePersist
    private void prePersist() {
        this.regDt = Instant.now();
    }
}