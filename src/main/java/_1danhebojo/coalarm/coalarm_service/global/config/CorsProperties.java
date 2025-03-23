package _1danhebojo.coalarm.coalarm_service.global.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@ConfigurationProperties(prefix = "cors")
@ConfigurationPropertiesBinding
public class CorsProperties {
	private final List<String> allowOrigins;
	private final List<String> allowMethods;
	private final boolean allowCredentials;
	private final List<String> exposedHeaders;
	private final List<String> allowedHeaders;
	private final long maxAge;
}
