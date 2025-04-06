package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertEntity;
import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DiscordService {
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendDiscordAlert(String webhookUrl, String message) {
        Map<String, String> request = Map.of(
                "username", "코알람",
                "content", message
        );

        ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ApiException(AppHttpStatus.FAILED_TO_SEND_DISCORD);
        }
    }

    public void sendDiscordEmbed(String webhookUrl, List<Map<String, Object>> embeds) {
        if (embeds == null || embeds.isEmpty()) return;

        boolean hasInvalid = embeds.stream().anyMatch(Objects::isNull);
        if (hasInvalid) {
            System.err.println("🚨 Invalid embed object detected! Skipping...");
            return;
        }

        int batchSize = 10;
        for (int i = 0; i < embeds.size(); i += batchSize) {
            List<Map<String, Object>> batch = embeds.subList(i, Math.min(i + batchSize, embeds.size()));

            Map<String, Object> body = Map.of(
                    "username", "코알람",
                    "embeds", batch
            );

            try {
                ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, body, String.class);
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new ApiException(AppHttpStatus.FAILED_TO_SEND_DISCORD);
                }
            } catch (Exception e) {
                System.err.println("❌ Discord 전송 실패 - " + e.getMessage());
                // 필요하다면 로깅이나 슬랙 전송 등 추가 처리
            }
        }
    }


    public Map<String, Object> buildEmbedMapFromAlert(AlertEntity alert) {
        String title;
        String description;
        int color;

        if(alert.getIsTargetPrice()){
            double price = alert.getTargetPrice().getPrice().doubleValue();
            title = "🎯 가격 알림";
            String formattedPrice;
            if (price < 1) {
                formattedPrice = String.format("%,.8f", price);
            } else {
                formattedPrice = String.format("%,.2f", price);
            }

            description = String.format(
                    "📌 %s / %s\n🎯 목표 가격 %s에 도달했습니다 (%d%% 변동)",
                    alert.getCoin().getSymbol(),
                    alert.getTitle(),
                    formattedPrice,
                    alert.getTargetPrice().getPercentage()
            );
            color = 0x3498db; // 파란색
        } else if(alert.getIsGoldenCross()){
            title = "📈 골든 크로스 알림";
            description = String.format(
                    "📌 %s / %s\n🚀 단기 이동 평균선이 장기선을 돌파했습니다.",
                    alert.getCoin().getSymbol(),
                    alert.getTitle()
            );
            color = 0x2ecc71; // 초록색
        } else if(alert.getIsVolumeSpike()){
            title = "🔥 거래량 급증 알림";
            description = String.format(
                    "📌 %s / %s\n📊 거래량이 급증했습니다.",
                    alert.getCoin().getSymbol(),
                    alert.getTitle()
            );
            color = 0xe67e22; // 주황색
        } else {
            title = "📢 알림 도착";
            description = alert.getTitle();
            color = 0x95a5a6; // 회색
        }

        return Map.of(
                "title", title,
                "description", description,
                "color", color,
                "timestamp", OffsetDateTime.now().toString()
        );
    }
}