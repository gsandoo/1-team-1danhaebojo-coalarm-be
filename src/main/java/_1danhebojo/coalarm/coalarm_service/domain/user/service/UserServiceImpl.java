package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertRepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.domain.alert.service.AlertSSEService;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.request.DiscordWebhookRequest;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.UserRepository;
import _1danhebojo.coalarm.coalarm_service.domain.user.util.NicknameGenerator;
import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppCookie;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import _1danhebojo.coalarm.coalarm_service.global.api.PkResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String REFRESH_HEADER = "Refresh";
    private final String COOKIE_HEADER = "Set-Cookie";
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final RefreshTokenService refreshTokenService;
    private final AlertSSEService alertSSEService;
    private final AlertRepositoryImpl alertRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final AppCookie appCookie;

    @Override
    public UserDTO getMyInfo(Long userId) {
        UserEntity user = userRepository.findByUserId(userId).orElseThrow(
                () -> new ApiException(AppHttpStatus.NOT_FOUND)
        );

        return UserDTO.fromEntity(user);
    }

    @Override
    @Transactional
    public UserDTO registerOrLogin(String kakaoId, String email) {
        Optional<UserEntity> userOptional = userRepository.findByKakaoId(kakaoId);

        if (userOptional.isPresent()) {
            return UserDTO.fromEntity(userOptional.get());
        }

        String randomNickname = NicknameGenerator.generateNickname(); // 랜덤 닉네임 생성
        UserEntity newUser = UserEntity.builder()
                .kakaoId(kakaoId)
                .nickname(randomNickname)
                .email(email)
                .profileImg(null)
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        alertSSEService.subscribe(savedUser.getUserId());

        return UserDTO.fromEntity(savedUser);
    }

    @Override
    @Transactional
    public void logout(UserDetails userDetails, String authorizationHeader) {
        if (userDetails == null) {
            throw new ApiException(AppHttpStatus.UNAUTHORIZED);
        }

        Long userId = findByKakaoId(userDetails.getUsername()).getUserId();
        refreshTokenService.deleteRefreshToken(userId);

        // TODO : 액세스 토큰 블랙리스트 처리

        alertSSEService.removeEmitter(userId);
    }

    @Override
    public UserDTO findByKakaoId(String kakaoId) {
        UserEntity user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_USER));

        return UserDTO.fromEntity(user); // Entity → DTO 변환
    }

    @Override
    public PkResponse updateUser(Long userId, String nickname, MultipartFile profileImage) {
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_USER));

        // 닉네임 업데이트
        if (StringUtils.hasText(nickname)) {
            validateNickname(nickname);
            user.updateNickname(nickname);
        }

        // 프로필 이미지 업데이트
        if (profileImage != null && !profileImage.isEmpty()) {
            if (user.getProfileImg() != null) {
                s3Service.deleteImage(user.getProfileImg());
            }
            String imageUrl = s3Service.uploadImage(profileImage);
            user.updateProfileImage(imageUrl);
        }

        userRepository.save(user);
        return PkResponse.of(user.getUserId());
    }

    private void validateNickname(String nickname) {
        if (nickname.length() < 2) {
            throw new ApiException(AppHttpStatus.BAD_REQUEST);
        }

    }

    @Override
    @Transactional
    public void deleteUser(Long userId, HttpServletRequest request, HttpServletResponse response) {
        // 카카오 액세스 토큰
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oAuthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
                oAuthToken.getAuthorizedClientRegistrationId(),
                oAuthToken.getName()
        );
        String kakaoAccessToken = client.getAccessToken().getTokenValue();

        // 카카오 로그인을 언링크
        unlinkKakaoUser(kakaoAccessToken);

        // 유저 조회
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_USER));

        // 프로필 이미지 삭제
        if (user.getProfileImg() != null) {
            s3Service.deleteImage(user.getProfileImg());
        }

        // 쿠키 제거
        response.addHeader(COOKIE_HEADER, appCookie.deleteCookie(AUTHORIZATION_HEADER));
        response.addHeader(COOKIE_HEADER, appCookie.deleteCookie(REFRESH_HEADER));

        //유저의 알람 삭제
        // TODO : 유저와 관련된 데이터 모두 삭제
        alertRepository.deleteByUserId(userId);

        // 유저 삭제
        userRepository.deleteById(userId);
    }

    private static final String UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";

    public void unlinkKakaoUser(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Authorization: Bearer ...
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. HTTP 엔티티 생성
        HttpEntity<String> request = new HttpEntity<>("", headers);

        // 3. 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                UNLINK_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        // 4. 응답 로깅
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("카카오 사용자 언링크 성공: {}", response.getBody());
        }
    }

    @Override
    @Transactional
    public PkResponse updateDiscordWebhook(Long userId, DiscordWebhookRequest request) {
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_USER));

        user.updateDiscordWebhook(request.getDiscordWebhook());

        userRepository.save(user);

        return PkResponse.of(user.getUserId());
    }
}