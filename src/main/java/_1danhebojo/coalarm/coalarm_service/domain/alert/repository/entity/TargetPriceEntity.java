package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "target_prices")
@Getter
@NoArgsConstructor
public class TargetPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price", nullable = false, precision = 18, scale = 8)
    private BigDecimal price;

    @Column(name = "percentage", nullable = false)
    private Integer percentage;

    @Column(name = "reg_dt", nullable = false, updatable = false)
    private Instant regDt;

    @Column(name = "chg_dt")
    private Instant chgDt;

    @OneToOne
    @JoinColumn(name = "alert_id")
    private AlertEntity alert;

    @Builder
    public TargetPriceEntity(Long id, BigDecimal price, Integer percentage, Instant regDt, Instant chgDt, AlertEntity alert) {
        this.id = id;
        this.price = price;
        this.percentage = percentage;
        this.regDt = regDt;
        this.chgDt = chgDt;
        this.alert = alert;
    }

    @PrePersist
    private void prePersist() {
        this.regDt = Instant.now();
    }
}

