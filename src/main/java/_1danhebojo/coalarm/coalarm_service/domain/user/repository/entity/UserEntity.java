package _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "kakao_id", nullable = false, unique = true)
    private String kakaoId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "discord_webhook")
    private String discordWebhook;

    @Column(name = "reg_dt", nullable = false, updatable = false)
    private Instant regDt = Instant.now();  // 기본값 설정

    @Column(name = "chg_dt")
    private Instant chgDt;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImg) {
        this.profileImg = profileImg;
    }

    public void updateDiscordWebhook(String discordWebhook) {
        if ((this.discordWebhook == null && discordWebhook == null) ||
                (this.discordWebhook != null && this.discordWebhook.equals(discordWebhook))) {
            return;
        }
        this.discordWebhook = discordWebhook;
        this.chgDt = Instant.now();  // 변경 일시 업데이트
    }
}
