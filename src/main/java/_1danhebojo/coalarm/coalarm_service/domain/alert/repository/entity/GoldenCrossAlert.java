package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "golden_cross")
public class GoldenCrossAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goldenCrossId;

    @Column(nullable = false)
    private Long shortMa;  // 단기 이동평균선

    @Column(nullable = false)
    private Long longMa;   // 장기 이동평균선

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "alert_id", nullable = false, unique = true)
    private Alert alert;
}

