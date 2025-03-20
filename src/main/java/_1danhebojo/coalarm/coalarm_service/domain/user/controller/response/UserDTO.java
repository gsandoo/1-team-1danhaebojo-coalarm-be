package _1danhebojo.coalarm.coalarm_service.domain.user.controller.response;

import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTO {
    private Long userId;
    private String kakaoId;
    private String nickname;
    private String email;
    private String profileImg;

    public static UserDTO fromEntity(UserEntity user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .kakaoId(user.getKakaoId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .kakaoId(kakaoId)
                .nickname(nickname)
                .email(email)
                .profileImg(profileImg)
                .build();
    }
}
