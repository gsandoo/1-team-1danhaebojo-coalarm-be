package _1danhebojo.coalarm.coalarm_service.domain.user.entity;

import _1danhebojo.coalarm.coalarm_service.domain.user.controller.response.UserDTO;
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
}
