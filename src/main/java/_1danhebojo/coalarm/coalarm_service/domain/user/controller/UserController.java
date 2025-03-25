package _1danhebojo.coalarm.coalarm_service.domain.user.controller;

import _1danhebojo.coalarm.coalarm_service.domain.user.controller.request.DiscordWebhookRequest;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
import _1danhebojo.coalarm.coalarm_service.domain.user.service.AuthService;
import _1danhebojo.coalarm.coalarm_service.domain.user.service.UserService;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import _1danhebojo.coalarm.coalarm_service.global.api.PkResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    // 회원 정보 불러오기
    @GetMapping("")
    public ResponseEntity<BaseResponse<UserDTO>> getMyInfo() {

        UserDTO userDTO = userService.getMyInfo(authService.getLoginUserId());
        return ResponseEntity.ok(BaseResponse.success(userDTO));
    }

    // 회원 프로필 수정 (닉네임, 프로필 사진)
    @PatchMapping("")
    public ResponseEntity<BaseResponse<PkResponse>> updateUserInfo(
            @RequestPart(value = "nickname", required = false) String nickname,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {

        return ResponseEntity.ok(BaseResponse.success(
                userService.updateUser(
                        authService.getLoginUserId(),
                        nickname,
                        profileImage
                )
        ));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout() {
//        userService.logout();
        return ResponseEntity.ok(BaseResponse.success());
    }

    // 회원 탈퇴
    @DeleteMapping("")
    public ResponseEntity<BaseResponse<Void>> withdraw() {
//        userService.deleteUser();
        return ResponseEntity.ok(BaseResponse.success());
    }

    // 디스코드 웹훅 URL 등록
    @PatchMapping("/discord")
    public ResponseEntity<BaseResponse<PkResponse>> updateDiscordWebhook(
            @RequestBody @Valid DiscordWebhookRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(
                userService.updateDiscordWebhook(
                        authService.getLoginUserId(),
                        request
                )
        ));
    }
}
