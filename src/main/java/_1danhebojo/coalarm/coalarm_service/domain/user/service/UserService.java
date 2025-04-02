package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.domain.user.controller.request.DiscordWebhookRequest;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
import _1danhebojo.coalarm.coalarm_service.global.api.PkResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDTO getMyInfo(Long userId);
    PkResponse updateUser(Long userId, String nickname, MultipartFile profileImage);
    UserDTO registerOrLogin(String kakaoId, String email);
    UserDTO findByKakaoId(String kakaoId);
    void logout(UserDetails userDetails, String authorizationHeader);
    void deleteUser(Long userId, HttpServletRequest request, HttpServletResponse response);
    PkResponse updateDiscordWebhook(Long userId, DiscordWebhookRequest request);
    PkResponse removeDiscordWebhook(Long userId);
}