package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertHistoryRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertRepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.GoldenCrossJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.TargetPriceJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.VolumeSpikeJpaRepository;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String REFRESH_HEADER = "Refresh";
    private final String COOKIE_HEADER = "Set-Cookie";
    private final UserRepository userRepository;
    private final AlertHistoryRepository alertHistoryRepository;

    private final TargetPriceJpaRepository targetPriceJpaRepository;
    private final GoldenCrossJpaRepository goldenCrossJpaRepository;
    private final VolumeSpikeJpaRepository volumeSpikeJpaRepository;

    private final AuthService authService;
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

        alertSSEService.subscribe(savedUser.getId());

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
            alertSSEService.updateUserNicknameInAlerts(userId, nickname);
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
        return PkResponse.of(user.getId());
    }

    private void validateNickname(String nickname) {
        if (nickname.length() < 2 || nickname.length() > 10) {
            throw new ApiException(AppHttpStatus.INVALID_NICKNAME_LENGTH);
        }

    }

    @Override
    @Transactional
    public void deleteUser(Long userId, HttpServletRequest request, HttpServletResponse response) {

        // 카카오 언링크
        authService.unlinkKakaoAccount();

        // 프로필 이미지 처리
        UserEntity user = processUserData(userId);

        // 쿠키 제거
        removeAuthCookies(response);

        // 유저의 알림 삭제
        deleteUserAlertData(userId);

        // 유저 삭제
        userRepository.deleteById(userId);
    }


    @Override
    @Transactional
    public PkResponse updateDiscordWebhook(Long userId, DiscordWebhookRequest request) {
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_USER));

        // 디스코드 웹훅 url 유효성 검사
        String webhookUrl = request.getDiscordWebhook();
        validateDiscordWebhookUrl(webhookUrl);

        user.updateDiscordWebhook(request.getDiscordWebhook());
        userRepository.save(user);

        alertSSEService.updateUserWebhookInAlerts(userId, webhookUrl);

        return PkResponse.of(user.getId());
    }

    @Override
    @Transactional
    public PkResponse removeDiscordWebhook(Long userId) {
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_USER));

        user.updateDiscordWebhook(null);
        userRepository.save(user);
        alertSSEService.updateUserWebhookInAlerts(userId, "");

        return PkResponse.of(user.getId());
    }

    private void validateDiscordWebhookUrl(String webhookUrl) {
        // 빈 문자열 체크
        if (webhookUrl == null || webhookUrl.trim().isEmpty()) {
            throw new ApiException(AppHttpStatus.EMPTY_DISCORD_WEBHOOK);
        }

        String discordWebhookRegex = "^https://discord\\.com/api/webhooks/\\d+/[\\w-]+$";
        Pattern pattern = Pattern.compile(discordWebhookRegex);
        Matcher matcher = pattern.matcher(webhookUrl);

        if (!matcher.matches()) {
            throw new ApiException(AppHttpStatus.INVALID_DISCORD_WEBHOOK);
        }

        // 실제 유효한 URL인지 테스트 메시지 보내기
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String payload = """
        {
            "content": "코알람에 오신 걸 환영합니다! 🎉\\n이제 디스코드에서 실시간 알림을 받아보실 수 있어요.",
            "username": "코알람"
        }
        """;

            HttpEntity<String> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ApiException(AppHttpStatus.INVALID_DISCORD_WEBHOOK);
            }

        } catch (Exception e) {
            log.error("디스코드 웹훅 테스트 실패: {}", e.getMessage());
            throw new ApiException(AppHttpStatus.INVALID_DISCORD_WEBHOOK);
        }
    }

    private UserEntity processUserData(Long userId) {
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_USER));

        // 프로필 이미지 삭제
        if (user.getProfileImg() != null) {
            s3Service.deleteImage(user.getProfileImg());
        }

        return user;
    }

    private void removeAuthCookies(HttpServletResponse response) {
        response.addHeader(COOKIE_HEADER, appCookie.deleteCookie(AUTHORIZATION_HEADER));
        response.addHeader(COOKIE_HEADER, appCookie.deleteCookie(REFRESH_HEADER));
    }

    private void deleteUserAlertData(Long userId) {
        List<Long> userAlertIds = alertRepository.findAlertIdsByUserId(userId);

        if (!userAlertIds.isEmpty()) {
            targetPriceJpaRepository.deleteByAlertIdIn(userAlertIds);
            goldenCrossJpaRepository.deleteByAlertIdIn(userAlertIds);
            volumeSpikeJpaRepository.deleteByAlertIdIn(userAlertIds);
        }

        // 사용자의 모든 알림 삭제
        alertHistoryRepository.deleteByUserId(userId);
        alertRepository.deleteByUserId(userId);
    }
}