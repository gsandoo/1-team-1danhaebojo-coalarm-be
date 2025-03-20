package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.domain.user.controller.request.DiscordWebhookRequest;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.DiscordWebhookResponse;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDTO registerOrLogin(String kakaoId, String email);
    UserDTO findByKakaoId(String kakaoId);
    Long updateUser(UserDetails userDetails, String nickname, MultipartFile profileImage);
    UserDTO getMyInfo(UserDetails userDetails);
    void logout(UserDetails userDetails, String authorizationHeader);
    void deleteUser(UserDetails userDetails, String authorizationHeader);
    DiscordWebhookResponse updateDiscordWebhook(UserDetails userDetails, DiscordWebhookRequest request);
}