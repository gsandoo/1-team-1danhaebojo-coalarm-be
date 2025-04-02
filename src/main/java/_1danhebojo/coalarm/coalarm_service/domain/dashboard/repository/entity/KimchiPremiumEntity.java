package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "kimchi_premiums")
@Getter
@NoArgsConstructor
public class KimchiPremiumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "domestic_price", nullable = false, precision = 18, scale = 8)
    private BigDecimal domesticPrice;

    @Column(name = "global_price", nullable = false, precision = 18, scale = 8)
    private BigDecimal globalPrice;

    @Column(name = "exchange_rate", nullable = false, precision = 18, scale = 8)
    private BigDecimal exchangeRate;

    @Column(name = "kimchi_premium", nullable = false, precision = 18, scale = 8)
    private BigDecimal kimchiPremium;

    @Column(name = "daily_change", nullable = false, precision = 18, scale = 8)
    private BigDecimal dailyChange;

    @Column(name = "reg_dt", nullable = false)
    private Instant regDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id")
    private CoinEntity coin;

    @Builder
    public KimchiPremiumEntity(Long id, BigDecimal domesticPrice, BigDecimal globalPrice, BigDecimal exchangeRate, BigDecimal kimchiPremium, BigDecimal dailyChange, Instant regDt, CoinEntity coin) {
        this.id = id;
        this.domesticPrice = domesticPrice;
        this.globalPrice = globalPrice;
        this.exchangeRate = exchangeRate;
        this.kimchiPremium = kimchiPremium;
        this.dailyChange = dailyChange;
        this.regDt = regDt;
        this.coin = coin;
    }

    @PrePersist
    private void prePersist() {
        this.regDt = Instant.now();
    }
}
