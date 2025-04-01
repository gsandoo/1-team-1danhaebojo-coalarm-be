package _1danhebojo.coalarm.coalarm_service.global.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final RateLimitProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // API 경로에만 레이트 리밋 적용
        if (!request.getRequestURI().startsWith("/api/v1/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();

        // 클라이언트 복합 식별자 생성
        String clientIp = extractClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String sessionId = request.getSession(true).getId();

        // 복합 키 생성 (IP + User-Agent 해시 + 세션 ID 해시)
        String clientKey = clientIp + ":" +
                (userAgent != null ? Math.abs(userAgent.hashCode() % 1000) : "0") + ":" +
                (sessionId.hashCode() % 10000);

        log.info("생성된 클라이언트 키: {}, 경로: {}", clientKey, path);

        // 버킷 가져오기 또는 새로 생성
        Bucket bucket = buckets.computeIfAbsent(clientKey, this::createNewBucket);

        // 토큰 소비 시도
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Rate Limit - Too Many Requests for Client: {}, Path: {}", clientKey, path);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.getWriter().write("{\"message\":\"API 호출 횟수가 제한을 초과했습니다. 잠시 후 다시 시도해주세요.\"}");
        }
    }

    private Bucket createNewBucket(String key) {
        Refill refill = Refill.intervally(properties.getRefillTokens(),
                Duration.ofMinutes(properties.getRefillDurationMinutes()));
        Bandwidth limit = Bandwidth.classic(properties.getCapacity(), refill);
        return Bucket4j.builder().addLimit(limit).build();
    }

    private String extractClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty()) {
            log.info("전체 X-Forwarded-For 헤더: {}", xfHeader);

            String[] ips = xfHeader.split(",");
            // 모든 IP를 로그로 출력
            for (int i = 0; i < ips.length; i++) {
                log.info("IP[{}]: {}", i, ips[i].trim());
            }

            return ips[0].trim();
        }

        // X-Real-IP 헤더 확인
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isEmpty()) {
            return realIp.trim();
        }

        // fallback
        return request.getRemoteAddr();
    }
}