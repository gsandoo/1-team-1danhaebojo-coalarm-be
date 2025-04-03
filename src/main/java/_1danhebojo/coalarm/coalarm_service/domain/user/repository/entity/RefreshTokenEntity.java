package _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    @Column(name = "is_revoked", nullable = false)
    private Boolean isRevoked;

    @Column(name = "expires_at", nullable = false, updatable = false)
    private Instant expiresAt;

    @Column(name = "issued_at", nullable = false, updatable = false)
    private Instant issuedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public RefreshTokenEntity(Long id, String token, String userAgent, Boolean isRevoked, Instant expiresAt, Instant issuedAt, UserEntity user) {
        this.id = id;
        this.token = token;
        this.userAgent = userAgent;
        this.isRevoked = isRevoked;
        this.expiresAt = expiresAt;
        this.issuedAt = issuedAt;
        this.user = user;
    }

    @PrePersist
    private void prePersist() {
        this.isRevoked = this.isRevoked != null && this.isRevoked;
        this.issuedAt = Instant.now();
    }
}
