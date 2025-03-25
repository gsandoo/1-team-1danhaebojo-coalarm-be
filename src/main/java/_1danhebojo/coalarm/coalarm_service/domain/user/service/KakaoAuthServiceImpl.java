package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import _1danhebojo.coalarm.coalarm_service.global.properties.KakaoProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Kakao OAuth 인증 및 사용자 정보를 가져오는 서비스
 */
@Service
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements KakaoAuthService {

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    //  카카오 API에서 액세스 토큰을 가져오는 메서드
    @Override
    public String getAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP 요청 파라미터 설정 (카카오 토큰 발급에 필요한 정보)
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", code);
        params.add("client_secret", kakaoProperties.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            // 카카오 API에 액세스 토큰 요청
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

            // 응답값을 JSON으로 변환
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // 액세스 토큰 반환
            return rootNode.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve access token from Kakao API", e);
        }
    }


    // 카카오 API에서 사용자 정보를 가져오는 메서트
    @Override
    public Map<String, String> getUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);

        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            Map<String, String> userInfo = new HashMap<>();

            userInfo.put("kakaoId", rootNode.get("id").asText()); // 카카오 고유 ID
            userInfo.put("email", rootNode.path("kakao_account").path("email").asText("")); // 이메일 (없을 경우 빈 문자열)
            return userInfo;
        } catch (RestClientException e) {
            throw new ApiException(AppHttpStatus.KAKAO_API_ERROR);
        } catch (JsonProcessingException e) {
            throw new ApiException(AppHttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
