package _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "coins")
@Getter
@NoArgsConstructor
public class CoinEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "symbol", unique = true)
    private String symbol;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public CoinEntity(Long id, String symbol, String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
    }
}

