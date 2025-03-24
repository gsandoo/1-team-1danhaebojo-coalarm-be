package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "kimchi_premium")
@Getter
@NoArgsConstructor
public class KimchiPremiumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "premium_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id", nullable = false)
    private CoinEntity coin;

    @Column(name = "domestic_price",nullable = false)
    private BigDecimal domesticPrice;

    @Column(name = "global_price", nullable = false)
    private BigDecimal globalPrice;

    @Column(name = "exchange_rate", nullable = false)
    private BigDecimal exchangeRate;

    @Column(name = "kimchi_premium", nullable = false)
    private BigDecimal kimchiPremium;

    @Column(name = "daily_change", nullable = false)
    private BigDecimal dailyChange;

    @Column(name = "reg_dt", nullable = false)
    private Instant regDt;

    public KimchiPremiumEntity(CoinEntity coin, BigDecimal domesticPrice, BigDecimal globalPrice, BigDecimal exchangeRate,
                               BigDecimal kimchiPremium, BigDecimal dailyChange) {
        this.coin = coin;
        this.domesticPrice = domesticPrice;
        this.globalPrice = globalPrice;
        this.exchangeRate = exchangeRate;
        this.kimchiPremium = kimchiPremium;
        this.dailyChange = dailyChange;
        this.regDt = Instant.now();
    }
}
