package _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "kakao_id", unique = true)
    private String kakaoId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "discord_webhook")
    private String discordWebhook;

    @Column(name = "reg_dt", nullable = false, updatable = false)
    private Instant regDt;

    @Column(name = "chg_dt")
    private Instant chgDt;

    @Builder
    public UserEntity(Long id, String kakaoId, String email, String nickname, String profileImg, String discordWebhook, Instant regDt, Instant chgDt) {
        this.id = id;
        this.kakaoId = kakaoId;
        this.email = email;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.discordWebhook = discordWebhook;
        this.regDt = regDt;
        this.chgDt = chgDt;
    }

    @PrePersist
    private void prePersist() {
        this.regDt = Instant.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.chgDt = Instant.now();
    }

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
