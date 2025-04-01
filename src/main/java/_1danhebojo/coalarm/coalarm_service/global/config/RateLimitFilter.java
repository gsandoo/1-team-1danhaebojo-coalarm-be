package _1danhebojo.coalarm.coalarm_service.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {
    private static final String RATE_LIMIT_COOKIE_NAME = "rate_limit_id";
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final RateLimitProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      
        // API 경로에만 레이트 리밋 적용
        if (!request.getRequestURI().startsWith("/api/v1/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 클라이언트 식별자 가져오기 (쿠키 기반)
        String clientId = getClientIdFromCookie(request);

        // 식별자가 없는 경우 새로 생성
        if (clientId == null) {
            clientId = UUID.randomUUID().toString();
            log.info("새로운 쿠키 생성 clientId = {}",clientId);
            response.addCookie(createRateLimitCookie(clientId));
        }

        // 버킷 가져오기 또는 생성
        Bucket bucket = getBucket(clientId);

        // 요청 처리 가능 여부 확인
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            // 요청 제한 초과 응답
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Map<String, Object> errorDetails = Map.of(
                    "status", HttpStatus.TOO_MANY_REQUESTS.value(),
                    "error", "Too Many Requests",
                    "message", "API 요청 한도를 초과했습니다. 잠시 후 다시 시도해주세요."
            );

            response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
        }
    }

    private String getClientIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (RATE_LIMIT_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private Cookie createRateLimitCookie(String clientId) {
        Cookie cookie = new Cookie(RATE_LIMIT_COOKIE_NAME, clientId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60); // 1시간
        return cookie;
    }

    private Bucket getBucket(String clientId) {
        return buckets.computeIfAbsent(clientId, id -> createNewBucket());
    }

    private Bucket createNewBucket() {
        Refill refill = Refill.intervally(properties.getRefillTokens(),
                Duration.ofMinutes(properties.getRefillDurationMinutes()));

        Bandwidth limit = Bandwidth.classic(properties.getCapacity(), refill);

        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
}