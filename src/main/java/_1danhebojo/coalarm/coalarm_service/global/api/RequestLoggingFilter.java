package _1danhebojo.coalarm.coalarm_service.global.api;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;

@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n")
                .append("method: ").append(request.getMethod()).append("\n")
                .append("path: ").append(request.getServletPath()).append("\n")
                .append("headers: \n");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement().toLowerCase();
            if (ALLOWED_HEADERS.contains(name)){
                String value = request.getHeader(name);
                logMessage.append(name).append(": ").append(value).append("\n");
            }
        }

        log.info(logMessage.toString());

        filterChain.doFilter(request, response);
    }

    private static final Set<String> ALLOWED_HEADERS = Set.of(
            "content-type", "user-agent", "accept", "host", "connection"
    );
}
