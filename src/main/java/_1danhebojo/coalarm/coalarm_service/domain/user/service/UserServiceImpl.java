package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.domain.auth.service.JwtBlacklistService;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.request.UserUpdateRequest;
import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.UserRepository;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.jpa.UserJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.user.util.NicknameGenerator;
import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import _1danhebojo.coalarm.coalarm_service.global.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final RefreshTokenService refreshTokenService;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtTokenProvider jwtTokenProvider;

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
        return UserDTO.fromEntity(savedUser);
    }

    @Override
    public UserDTO findByKakaoId(String kakaoId) {
        UserEntity user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND));

        return UserDTO.fromEntity(user); // Entity → DTO 변환
    }

    @Override
    @Transactional
    public UserDTO updateUser(String kakaoId, UserUpdateRequest request) {
        UserEntity user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_USER));

        // 닉네임 업데이트
        String nickname = request.getNickname();
        if (nickname != null && !nickname.isEmpty()) {
            validateNickname(nickname);
            user.updateNickname(nickname);
        }

        // 프로필 이미지 업데이트
        MultipartFile profileImage = request.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            // 기존 이미지가 있으면 삭제
            String currentProfileImage = user.getProfileImg();
            if (currentProfileImage != null && !currentProfileImage.isEmpty()) {
                s3Service.deleteImage(currentProfileImage);
            }

            // 새 이미지 업로드
            String imageUrl = s3Service.uploadImage(profileImage);
            user.updateProfileImage(imageUrl);
        }

        // 변경된 엔티티 저장
        UserEntity updatedUser = userRepository.save(user);
        return UserDTO.fromEntity(updatedUser);
    }

    private void validateNickname(String nickname) {
        if (nickname.length() < 2) {
            throw new ApiException(AppHttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public void deleteUser(Long userId, String authorizationHeader) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_USER));

        // 프로필 이미지 삭제
        if (user.getProfileImg() != null) {
            s3Service.deleteImage(user.getProfileImg());
        }

        // 리프레시 토큰 삭제
        refreshTokenService.deleteRefreshToken(userId);

        // 액세스 토큰 블랙리스트 추가
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7).trim();
            Instant expiryInstant = jwtTokenProvider.getExpirationInstant(accessToken);
            if (expiryInstant != null) {
                jwtBlacklistService.addToBlacklist(accessToken, expiryInstant);
            }
        }

        // 유저 삭제
        userRepository.delete(user);
    }
}