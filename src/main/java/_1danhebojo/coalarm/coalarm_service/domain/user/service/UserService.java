package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.domain.user.controller.request.DiscordWebhookRequest;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
import _1danhebojo.coalarm.coalarm_service.global.api.PkResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDTO getMyInfo(Long userId);
    PkResponse updateUser(Long userId, String nickname, MultipartFile profileImage);
    UserDTO registerOrLogin(String kakaoId, String email);
    UserDTO findByKakaoId(String kakaoId);
    void logout();
    void deleteUser();
    PkResponse updateDiscordWebhook(Long userId, DiscordWebhookRequest request);
}