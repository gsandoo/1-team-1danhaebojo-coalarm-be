package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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
}