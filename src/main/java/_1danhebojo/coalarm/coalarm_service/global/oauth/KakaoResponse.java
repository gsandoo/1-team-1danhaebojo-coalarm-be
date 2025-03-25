package _1danhebojo.coalarm.coalarm_service.global.oauth;

import lombok.*;

import java.util.Map;
import java.util.Optional;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoResponse implements OAuthResponse {

    private Map<String, Object> attributes;
    private Map<String, Object> accounts;
    private Map<String, Object> profiles;

    @SuppressWarnings("unchecked")
    public static KakaoResponse of(Map<String, Object> attributes) {
        Map<String, Object> accounts = Optional.ofNullable(attributes)
                .map(attr -> (Map<String, Object>) attr.get("kakao_account"))
                .orElse(Map.of());

        Map<String, Object> profiles = Optional.ofNullable(accounts)
                .map(acc -> (Map<String, Object>) acc.get("profile"))
                .orElse(Map.of());

        return KakaoResponse.builder()
                .attributes(Optional.ofNullable(attributes).orElse(Map.of()))
                .accounts(accounts)
                .profiles(profiles)
                .build();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return Optional.ofNullable(attributes.get("id"))
                .map(Object::toString)
                .orElse("");
    }

    public String getEmail() {
        return Optional.ofNullable(accounts.get("email"))
                .map(Object::toString)
                .orElse("");
    }

    public String getName() {
        return Optional.ofNullable(profiles.get("nickname"))
                .map(Object::toString)
                .orElse("");
    }
}