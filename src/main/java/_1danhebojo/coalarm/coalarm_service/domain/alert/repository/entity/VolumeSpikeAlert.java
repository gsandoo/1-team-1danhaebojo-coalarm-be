package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "volume_spike")
public class VolumeSpikeAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long marketAlertId;

    @Column(nullable = true)
    private boolean tradingVolumeSoaring;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "alert_id", nullable = false, unique = true)
    private Alert alert;
}

