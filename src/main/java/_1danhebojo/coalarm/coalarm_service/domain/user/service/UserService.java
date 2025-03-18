package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;

public interface UserService {
    UserDTO registerOrLogin(String kakaoId, String email);
    UserDTO findByKakaoId(String kakaoId);
}