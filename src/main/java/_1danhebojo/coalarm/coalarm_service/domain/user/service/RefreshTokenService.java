package _1danhebojo.coalarm.coalarm_service.domain.user.service;

public interface RefreshTokenService {
    String createRefreshToken(Long userId);
    boolean validateRefreshToken(String token);
    void deleteRefreshToken(Long userId);
    Long getUserIdByToken(String token);
}
