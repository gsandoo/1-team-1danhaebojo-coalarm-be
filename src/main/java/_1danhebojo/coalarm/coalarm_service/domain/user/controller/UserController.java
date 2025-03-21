package _1danhebojo.coalarm.coalarm_service.domain.user.controller;

import _1danhebojo.coalarm.coalarm_service.domain.user.controller.request.DiscordWebhookRequest;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.DiscordWebhookResponse;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
import _1danhebojo.coalarm.coalarm_service.domain.user.service.UserService;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원 정보 불러오기
    @GetMapping
    public ResponseEntity<BaseResponse<UserDTO>> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UserDTO userDTO = userService.getMyInfo(userDetails);
        return ResponseEntity.ok(BaseResponse.success(userDTO));
    }

    // 회원 프로필 수정 (닉네임, 프로필 사진)
    @PatchMapping
    public ResponseEntity<BaseResponse<Long>> updateUserInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                             @RequestPart(value = "nickname", required = false) String nickname,
                                                             @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        Long userId = userService.updateUser(userDetails, nickname, profileImage);
        return ResponseEntity.ok(BaseResponse.success(userId));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestHeader("Authorization") String authorizationHeader) {
        userService.logout(userDetails, authorizationHeader);
        return ResponseEntity.ok(BaseResponse.success());
    }

    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> withdraw(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestHeader("Authorization") String authorizationHeader) {
        userService.deleteUser(userDetails, authorizationHeader);
        return ResponseEntity.ok(BaseResponse.success());
    }

    // 디스코드 웹훅 URL 등록
    @PatchMapping("/discord")
    public ResponseEntity<BaseResponse<DiscordWebhookResponse>> updateDiscordWebhook(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid DiscordWebhookRequest request) {

        DiscordWebhookResponse response = userService.updateDiscordWebhook(userDetails, request);
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}
