package _1danhebojo.coalarm.coalarm_service.domain.user.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class UserUpdateRequest {
    private String nickname;
    private MultipartFile profileImage;

    public static UserUpdateRequest of(String nickname,MultipartFile profileImage){
        return UserUpdateRequest.builder()
                .nickname(nickname)
                .profileImage(profileImage)
                .build();
    }
}
