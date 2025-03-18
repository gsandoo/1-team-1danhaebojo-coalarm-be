package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class TickerCompositeKey implements Serializable {

    @Column(name = "timestamp", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime timestamp;

    @Column(name = "exchange", nullable = false)
    private String exchange;

    @Column(name = "symbol", nullable = false)
    private String symbol;
}