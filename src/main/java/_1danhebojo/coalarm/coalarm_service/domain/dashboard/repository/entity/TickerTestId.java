package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@EqualsAndHashCode
@Embeddable
public class TickerTestId implements Serializable {
    private Instant utcDateTime; // UTC 시간 (PK)
    private String code;         // 코인 심볼 (PK)
}
