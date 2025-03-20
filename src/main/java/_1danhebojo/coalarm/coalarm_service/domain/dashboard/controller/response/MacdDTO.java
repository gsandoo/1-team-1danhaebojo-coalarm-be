package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class MacdDTO {
    private final BigDecimal value;
    private final BigDecimal signal;
    private final BigDecimal histogram;
    private final String trend;
}