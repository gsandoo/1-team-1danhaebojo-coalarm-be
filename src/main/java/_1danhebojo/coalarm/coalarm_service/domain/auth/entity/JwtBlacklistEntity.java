package _1danhebojo.coalarm.coalarm_service.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor
@Table(name = "jwt_blacklist")
@AllArgsConstructor
public class JwtBlacklistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    public static JwtBlacklistEntity of(String token, Instant expiryDate) {
        return JwtBlacklistEntity.builder()
                .token(token)
                .expiryDate(expiryDate)
                .build();
    }
}