package _1danhebojo.coalarm.coalarm_service.domain.user.repository;

import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByKakaoId(String kakaoId);
    UserEntity save(UserEntity userEntity);
    Optional<UserEntity> findByUserId(Long userId);
    void delete(UserEntity userEntity);
}
