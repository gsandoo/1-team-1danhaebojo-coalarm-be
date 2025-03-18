package _1danhebojo.coalarm.coalarm_service.domain.user.controller;

import _1danhebojo.coalarm.coalarm_service.domain.user.controller.request.UserUpdateRequest;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
import _1danhebojo.coalarm.coalarm_service.domain.user.service.UserService;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<BaseResponse<UserDTO>> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String kakaoId = userDetails.getUsername();
        UserDTO userDTO = userService.findByKakaoId(kakaoId);
        return ResponseEntity.ok(BaseResponse.success(userDTO));
    }

    // NOTE: 프로필 수정
    @PatchMapping()
    public ResponseEntity<BaseResponse<Long>> updateUserInfo(@AuthenticationPrincipal UserDetails userDetails,
         @RequestPart(value = "nickname", required = false) String nickname,
         @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        String kakaoId = userDetails.getUsername();
        UserUpdateRequest request = UserUpdateRequest.of(nickname, profileImage);
        UserDTO userDTO = userService.updateUser(kakaoId, request);

        return ResponseEntity.ok(BaseResponse.success(userDTO.getUserId()));
    }

}
