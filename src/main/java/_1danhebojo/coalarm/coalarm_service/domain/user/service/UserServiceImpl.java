package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
import _1danhebojo.coalarm.coalarm_service.domain.user.entity.UserEntity;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.UserRepository;
import _1danhebojo.coalarm.coalarm_service.domain.user.util.NicknameGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
                .orElseThrow(() -> new RuntimeException("User not found with kakaoId: " + kakaoId));

        return UserDTO.fromEntity(user); // Entity → DTO 변환
    }
}