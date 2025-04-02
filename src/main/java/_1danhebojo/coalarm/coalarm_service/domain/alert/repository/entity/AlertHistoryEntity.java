package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "alert_histories")
@Getter
@NoArgsConstructor
public class AlertHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reg_dt", nullable = false)
    private Instant regDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_id")
    private AlertEntity alert;

    @Builder
    public AlertHistoryEntity(Long id, Instant regDt, UserEntity user, AlertEntity alert) {
        this.id = id;
        this.regDt = regDt;
        this.user = user;
        this.alert = alert;
    }

    @PrePersist
    private void prePersist() {
        this.regDt = Instant.now();
    }
}
