package _1danhebojo.coalarm.coalarm_service.domain.auth.repository;

import _1danhebojo.coalarm.coalarm_service.domain.auth.entity.JwtBlacklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface JwtBlacklistJpaRepository extends JpaRepository<JwtBlacklistEntity, Long> {
    Optional<JwtBlacklistEntity> findByToken(String token);
    void deleteByExpiryDateBefore(Instant now);
}
