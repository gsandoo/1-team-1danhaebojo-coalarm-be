package _1danhebojo.coalarm.coalarm_service.domain.user.repository.jpa;

import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByKakaoId(String kakaoId);
    Optional<UserEntity> findById(Long userId);
}
