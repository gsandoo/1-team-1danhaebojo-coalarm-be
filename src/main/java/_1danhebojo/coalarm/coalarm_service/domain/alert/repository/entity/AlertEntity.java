package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "alerts")
@Setter
@Getter
@NoArgsConstructor
public class AlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name="is_golden_cross", nullable = false)
    private Boolean isGoldenCross;

    @Column(name="is_target_price", nullable = false)
    private Boolean isTargetPrice;

    @Column(name="is_volume_spike", nullable = false)
    private Boolean isVolumeSpike;

    @Column(name = "reg_dt", nullable = false, updatable = false)
    private Instant regDt = Instant.now();

    @Column(name = "chg_dt")
    private Instant chgDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id")
    private CoinEntity coin;

    @OneToOne(mappedBy = "alert", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private TargetPriceEntity targetPrice;

    @OneToOne(mappedBy = "alert", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private GoldenCrossEntity goldenCross;

    @OneToOne(mappedBy = "alert", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private VolumeSpikeEntity volumeSpike;

    @Builder
    public AlertEntity(Long id, Boolean active, String title, Boolean isGoldenCross, Boolean isTargetPrice, Boolean isVolumeSpike, Instant regDt, Instant chgDt, UserEntity user, CoinEntity coin, TargetPriceEntity targetPrice, GoldenCrossEntity goldenCross, VolumeSpikeEntity volumeSpike) {
        this.id = id;
        this.active = active;
        this.title = title;
        this.isGoldenCross = isGoldenCross;
        this.isTargetPrice = isTargetPrice;
        this.isVolumeSpike = isVolumeSpike;
        this.regDt = regDt;
        this.chgDt = chgDt;
        this.user = user;
        this.coin = coin;
        this.targetPrice = targetPrice;
        this.goldenCross = goldenCross;
        this.volumeSpike = volumeSpike;
    }

    @PrePersist
    private void prePersist() {
        this.regDt = Instant.now();
        this.active = this.active != null;
        this.isGoldenCross = this.isGoldenCross != null && this.isGoldenCross;
        this.isTargetPrice = this.isTargetPrice != null && this.isTargetPrice;
        this.isVolumeSpike = this.isVolumeSpike != null && this.isVolumeSpike;
    }

    @PreUpdate
    private void preUpdate() {
        this.chgDt = Instant.now();
    }
}
