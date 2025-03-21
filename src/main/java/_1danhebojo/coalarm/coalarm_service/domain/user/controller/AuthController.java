package _1danhebojo.coalarm.coalarm_service.domain.user.controller;

import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.TokenResponse;
import _1danhebojo.coalarm.coalarm_service.domain.user.service.RefreshTokenService;
import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import _1danhebojo.coalarm.coalarm_service.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<TokenResponse>> refreshAccessToken(
            @RequestHeader("Authorization") String authorizationHeader) {

        String refreshToken = authorizationHeader.replace("Bearer ", "").trim();

        // Refresh Token 검증
        refreshTokenService.validateRefreshToken(refreshToken);

        // Refresh Token으로 userId 조회
        Long userId = refreshTokenService.getUserIdByToken(refreshToken);

        // 새로운 Access Token 발급
        String newAccessToken = jwtTokenProvider.generateToken(userId.toString());

        // Refresh Token을 재발급
        String newRefreshToken = refreshTokenService.createRefreshToken(userId);

        TokenResponse response = TokenResponse.of(newAccessToken, newRefreshToken);

        return ResponseEntity.ok(BaseResponse.success(response));
    }
}