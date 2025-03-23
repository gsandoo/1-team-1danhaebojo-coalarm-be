package _1danhebojo.coalarm.coalarm_service.global.oauth;

import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class CoalarmOAuth2User extends UserEntity implements OAuth2User {
	private Long id;
	private String kakaoId;
	private boolean isNewbie;

	// 회원가입 전
	private CoalarmOAuth2User(String kakaoId, boolean isNewbie) {
		this.kakaoId = kakaoId;
		this.isNewbie = isNewbie;
	}
	private CoalarmOAuth2User(Long id, String kakaoId, boolean isNewbie) {
		this.id = id;
		this.kakaoId = kakaoId;
		this.isNewbie = isNewbie;
	}

	public static CoalarmOAuth2User of(Long id, String kakaoId) {
		return new CoalarmOAuth2User(
				id,
				kakaoId,
				false
		);
	}

	public static CoalarmOAuth2User of(OAuthResponse response, boolean isNewbie) {
		return new CoalarmOAuth2User(
			response.getProviderId(),
			isNewbie
		);
	}

	public static CoalarmOAuth2User of(Long id, OAuthResponse response, boolean isNewbie) {
		return new CoalarmOAuth2User(
			id,
            response.getProviderId(),
			isNewbie
		);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	// TODO : ROLE 추가 필요
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public String getName() {
		return this.kakaoId;
	}

}
