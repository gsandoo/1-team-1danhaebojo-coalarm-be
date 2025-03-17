package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import java.util.Map;

public interface KakaoAuthService {
    String getAccessToken(String code);
    Map<String, String> getUserInfo(String accessToken);
}
