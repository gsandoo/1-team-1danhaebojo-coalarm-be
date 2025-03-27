package _1danhebojo.coalarm.coalarm_service.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.web")
public class KakaoProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
