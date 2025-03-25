package _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coins")
@Getter
@NoArgsConstructor
public class CoinEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coin_id")
    private Long coinId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String symbol;

    public CoinEntity(String name, String symbol){
        this.name = name;
        this.symbol = symbol;
    }
}
