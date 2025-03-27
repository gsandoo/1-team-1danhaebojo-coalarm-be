package _1danhebojo.coalarm.coalarm_service.global.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ToString
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.security.oauth2")
public class OAuthProperties {

	private final Web web;
	private final Client client;

	@Getter
	@ToString
	@AllArgsConstructor
	public static class Web {
		private final URL url;
		private final Cookie cookie;

		@Getter
		@ToString
		@AllArgsConstructor
		public static class URL {
			private final String dashboard;
			private final String login;
		}

		@Getter
		@ToString
		@AllArgsConstructor
		public static class Cookie {
			private final int maxAge;
			private final String path;
			private final boolean httpOnly;
			private final boolean secure;
			private final String sameSite;
		}
	}

	@Getter
	@ToString
	@AllArgsConstructor
	public static class Client {
		private final Registration registration;

		@Getter
		@ToString
		@AllArgsConstructor
		public static class Registration {
			private final Kakao kakao;

			@Getter
			@ToString
			@AllArgsConstructor
			public static class Kakao {
				private final String clientId;
			}
		}
	}
}