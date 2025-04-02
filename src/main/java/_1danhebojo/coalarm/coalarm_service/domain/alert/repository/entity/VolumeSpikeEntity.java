package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "volume_spikes")
@Getter
@NoArgsConstructor
public class VolumeSpikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trading_volume_soaring", nullable = false)
    private Boolean tradingVolumeSoaring;

    @Column(name = "reg_dt", nullable = false, updatable = false)
    private Instant regDt;

    @Column(name = "chg_dt")
    private Instant chgDt;

    @OneToOne
    @JoinColumn(name = "alert_id")
    private AlertEntity alert;

    @Builder
    public VolumeSpikeEntity(Long id, Boolean tradingVolumeSoaring, Instant regDt, Instant chgDt, AlertEntity alert) {
        this.id = id;
        this.tradingVolumeSoaring = tradingVolumeSoaring;
        this.regDt = regDt;
        this.chgDt = chgDt;
        this.alert = alert;
    }

    @PrePersist
    private void prePersist() {
        this.regDt = Instant.now();
        this.tradingVolumeSoaring = this.tradingVolumeSoaring != null && this.tradingVolumeSoaring;
    }

    @PreUpdate
    private void preUpdate() {
        this.chgDt = Instant.now();
    }
}

