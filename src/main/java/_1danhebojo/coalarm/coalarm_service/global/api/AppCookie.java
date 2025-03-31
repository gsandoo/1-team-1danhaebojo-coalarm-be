package _1danhebojo.coalarm.coalarm_service.global.api;

import _1danhebojo.coalarm.coalarm_service.global.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppCookie {
    private final JwtProperties jwtProperties;

    public String createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .path(jwtProperties.getCookie().getPath())
                .sameSite(jwtProperties.getCookie().getSameSite())
                .httpOnly(jwtProperties.getCookie().isHttpOnly())
                .secure(jwtProperties.getCookie().isSecure())
                .maxAge(jwtProperties.getCookie().getMaxAge())
                .build()
                .toString();
    }

    public String deleteCookie(String key) {
        return ResponseCookie.from(key, "")
                .path(jwtProperties.getCookie().getPath())
                .sameSite(jwtProperties.getCookie().getSameSite())
                .httpOnly(jwtProperties.getCookie().isHttpOnly())
                .secure(jwtProperties.getCookie().isSecure())
                .maxAge(0)
                .build()
                .toString();
    }
}
