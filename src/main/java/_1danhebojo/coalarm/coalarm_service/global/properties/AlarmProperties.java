package _1danhebojo.coalarm.coalarm_service.global.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "alarm")
@ConfigurationPropertiesBinding
@Getter
@Setter
public class AlarmProperties {
    private long sendQueueInterval;
    private long sendDiscordInterval;
    private long refreshActive;
    private long sendHeartClient;
    private long sendSubscription;
    private long alarmCountLimit;
}
