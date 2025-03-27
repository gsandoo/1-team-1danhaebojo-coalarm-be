package _1danhebojo.coalarm.coalarm_service.domain.user.repository;

import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserEntity> findByKakaoId(String kakaoId) {
        return userJpaRepository.findByKakaoId(kakaoId);
    }

    @Override
    public Optional<UserEntity> findByUserId(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userJpaRepository.save(user);
    }

    @Override
    public void delete(UserEntity userEntity) {
        userJpaRepository.delete(userEntity);
    }
}
