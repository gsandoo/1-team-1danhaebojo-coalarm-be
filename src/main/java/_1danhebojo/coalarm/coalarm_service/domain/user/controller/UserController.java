package _1danhebojo.coalarm.coalarm_service.domain.user.controller;

import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
import _1danhebojo.coalarm.coalarm_service.domain.user.service.UserService;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserDTO>> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String kakaoId = userDetails.getUsername();
        UserDTO userDTO = userService.findByKakaoId(kakaoId);
        return ResponseEntity.ok(BaseResponse.success(userDTO));
    }

}
