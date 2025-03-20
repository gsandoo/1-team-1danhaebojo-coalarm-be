package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "target_price")
public class TargetPriceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long targetPriceId;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer percentage;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "alert_id", nullable = false, unique = true)
    private Alert alert;
}

