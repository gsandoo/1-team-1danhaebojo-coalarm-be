package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "golden_crosses")
public class GoldenCrossEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "short_ma", nullable = false)
    private Integer shortMa;

    @Column(name = "long_ma", nullable = false)
    private Integer longMa;

    @Column(name = "reg_dt", nullable = false, updatable = false)
    private Instant regDt;

    @Column(name = "chg_dt")
    private Instant chgDt;

    @OneToOne
    @JoinColumn(name = "alert_id")
    private AlertEntity alert;

    @Builder
    public GoldenCrossEntity(Long id, Integer shortMa, Integer longMa, Instant regDt, Instant chgDt, AlertEntity alert) {
        this.id = id;
        this.shortMa = shortMa;
        this.longMa = longMa;
        this.regDt = regDt;
        this.chgDt = chgDt;
        this.alert = alert;
    }

    public GoldenCrossEntity() {

    }

    @PrePersist
    private void prePersist() {
        this.regDt = Instant.now();
        this.shortMa = this.shortMa == null ? 7 : this.shortMa;
        this.longMa = this.longMa == null ? 20 : this.longMa;
    }

    @PreUpdate
    private void preUpdate() {
        this.chgDt = Instant.now();
    }
}

