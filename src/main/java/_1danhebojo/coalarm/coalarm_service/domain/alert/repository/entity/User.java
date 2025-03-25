package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

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

