package _1danhebojo.coalarm.coalarm_service.domain.auth.service;

import java.time.Instant;

public interface JwtBlacklistService {
    void addToBlacklist(String token, Instant expiryDate);

    boolean isBlacklisted(String token);

    //void removeExpiredTokens();
}
