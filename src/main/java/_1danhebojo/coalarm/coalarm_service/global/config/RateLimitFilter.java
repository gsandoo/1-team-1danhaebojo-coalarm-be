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
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
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

        // 클라이언트 식별자 가져오기
        String clientKey = getClientIdentifier(request);

        // 버킷 가져오기 또는 새로 생성
        Bucket bucket = buckets.computeIfAbsent(clientKey, this::createNewBucket);

        // 토큰 소비 시도
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"API 호출 횟수가 제한을 초과했습니다. 잠시 후 다시 시도해주세요.\"}");
        }
    }

    private Bucket createNewBucket(String key) {
        Refill refill = Refill.intervally(properties.getRefillTokens(),
                Duration.ofMinutes(properties.getRefillDurationMinutes()));
        Bandwidth limit = Bandwidth.classic(properties.getCapacity(), refill);
        return Bucket4j.builder().addLimit(limit).build();
    }


    private String getClientIdentifier(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            // 첫 번째 IP 주소 사용 (원래 클라이언트 IP)
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
