package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
import _1danhebojo.coalarm.coalarm_service.domain.user.entity.UserEntity;

public interface UserService {
    UserDTO registerOrLogin(String kakaoId, String email);
}