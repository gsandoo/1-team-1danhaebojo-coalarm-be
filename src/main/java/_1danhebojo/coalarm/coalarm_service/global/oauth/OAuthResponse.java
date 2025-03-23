package _1danhebojo.coalarm.coalarm_service.global.oauth;

public interface OAuthResponse {
	// 제공자 : Naver, Kakao ...
	String getProvider();
	// 제공자에서 발급해주는 아이디
	String getProviderId();
}
