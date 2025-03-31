package _1danhebojo.coalarm.coalarm_service.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rate-limit")
@Getter
@Setter
public class RateLimitProperties {
    private int capacity = 10;
    private int refillTokens = 10;
    private int refillDurationMinutes = 1;
}
