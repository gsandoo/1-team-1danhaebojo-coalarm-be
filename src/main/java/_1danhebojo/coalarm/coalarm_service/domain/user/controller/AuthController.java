package _1danhebojo.coalarm.coalarm_service.domain.user.controller;

import _1danhebojo.coalarm.coalarm_service.domain.user.service.AuthService;
import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.logout(request, response);
        return ResponseEntity.ok(BaseResponse.success());
    }
}
