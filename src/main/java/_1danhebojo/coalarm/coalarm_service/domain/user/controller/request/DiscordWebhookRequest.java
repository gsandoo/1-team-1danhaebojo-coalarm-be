package _1danhebojo.coalarm.coalarm_service.domain.user.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DiscordWebhookRequest {
    @JsonProperty("web_hook_url")
    private String discordWebhook;
}
