package _1danhebojo.coalarm.coalarm_service.domain.user.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByUserId(Long userId);
}
