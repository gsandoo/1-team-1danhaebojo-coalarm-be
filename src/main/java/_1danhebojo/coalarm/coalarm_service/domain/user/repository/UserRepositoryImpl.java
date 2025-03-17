package _1danhebojo.coalarm.coalarm_service.domain.user.repository;

import _1danhebojo.coalarm.coalarm_service.domain.user.entity.UserEntity;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.jpa.UserJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
    public UserEntity save(UserEntity user) {
        return userJpaRepository.save(user);
    }
}
