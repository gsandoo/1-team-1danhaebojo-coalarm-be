package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.RefreshTokenEntity;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.jpa.RefreshTokenJpaRepository;
import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    // 주어진 userId에 대한 새로운 Refresh Token을 생성하고 저장
    @Override
    @Transactional
    public String createRefreshToken(Long userId) {
        refreshTokenJpaRepository.deleteByUserId(userId);

        String refreshToken = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plusSeconds(60 * 60 * 24 * 7);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .userId(userId)
                .token(refreshToken)
                .expiryDate(expiryDate)
                .build();refreshTokenJpaRepository.save(refreshTokenEntity);

        return refreshToken;
    }

    // 주어진 Refresh Token이 유효한지 검증
    @Override
    public boolean validateRefreshToken(String token) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenJpaRepository.findByToken(token)
                .orElseThrow(()-> new ApiException(AppHttpStatus.INVALID_REFRESH_TOKEN));

        // refresh token 만료 확인
        if (refreshTokenEntity.getExpiryDate().isBefore(Instant.now())) {
            throw new ApiException(AppHttpStatus.UNAUTHORIZED);
        }

        return true;
    }

    // 주어진 userId의 Refresh Token을 삭제
    @Override
    @Transactional
    public void deleteRefreshToken(Long userId) {
        refreshTokenJpaRepository.deleteByUserId(userId);
    }

    // 주어진 Refresh Token을 기반으로 사용자 ID를 조회
    @Override
    public Long getUserIdByToken(String token) {
        return refreshTokenJpaRepository.findByToken(token)
                .map(RefreshTokenEntity::getUserId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.INVALID_ACCESS_TOKEN));
    }
}
