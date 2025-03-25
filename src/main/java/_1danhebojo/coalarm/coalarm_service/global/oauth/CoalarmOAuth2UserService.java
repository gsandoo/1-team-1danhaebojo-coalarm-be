package _1danhebojo.coalarm.coalarm_service.global.oauth;

import _1danhebojo.coalarm.coalarm_service.domain.user.repository.UserRepository;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import _1danhebojo.coalarm.coalarm_service.domain.user.util.NicknameGenerator;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoalarmOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// OAuth 식별
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		KakaoResponse response = null;
		if (registrationId.equals("kakao")) {
			response = KakaoResponse.of(oAuth2User.getAttributes());
		} else {
			throw new OAuth2AuthenticationException(new OAuth2Error(AppHttpStatus.INVALID_OAUTH_TYPE.getMessage()));
		}

		UserEntity user = userRepository.findByKakaoId(response.getProviderId()).orElse(null);

		if (user != null) {
			return CoalarmOAuth2User.of(user.getUserId(), response, false);
		}

		String randomNickname = NicknameGenerator.generateNickname(); // 랜덤 닉네임 생성
		UserEntity newbie = UserEntity.builder()
				.kakaoId(response.getProviderId())
				.nickname(randomNickname)
				.email(response.getEmail())
				.profileImg(null)
				.build();

        newbie = userRepository.save(newbie);

        return CoalarmOAuth2User.of(newbie.getUserId(), response, true);
	}
}
