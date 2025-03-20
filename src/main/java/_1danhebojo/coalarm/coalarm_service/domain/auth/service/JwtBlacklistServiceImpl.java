package _1danhebojo.coalarm.coalarm_service.domain.auth.service;

import _1danhebojo.coalarm.coalarm_service.domain.auth.entity.JwtBlacklistEntity;
import _1danhebojo.coalarm.coalarm_service.domain.auth.repository.JwtBlacklistJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtBlacklistServiceImpl implements JwtBlacklistService {

    private final JwtBlacklistJpaRepository jwtBlacklistJpaRepository;

    @Override
    public void addToBlacklist(String token, Instant expiryDate) {
        JwtBlacklistEntity entity = JwtBlacklistEntity.of(token, expiryDate);
        jwtBlacklistJpaRepository.save(entity);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return jwtBlacklistJpaRepository.findByToken(token).isPresent();
    }

    @Override
    @Scheduled(cron = "0 0 * * * ?") // 만료된 토큰을 매 시간 디비에서 삭제
    public void removeExpiredTokens() {
        jwtBlacklistJpaRepository.deleteByExpiryDateBefore(Instant.now());
    }
}
