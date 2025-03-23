package _1danhebojo.coalarm.coalarm_service.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.security.oauth2.web")
@ConfigurationPropertiesBinding
public class OAuthProperties {

	private final URL url;
	private final Cookie cookie;

	@Getter @ToString
	@AllArgsConstructor
	public static class URL {
		private final String dashboard;
		private final String login;
	}
	@Getter @ToString
	@AllArgsConstructor
	public static class Cookie {
		private final int maxAge;
		private final String path;
		private final boolean httpOnly;
		private final boolean secure;
        private final String sameSite;
	}
}
