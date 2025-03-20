package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LongShortStrengthDTO {
    private BigDecimal longRatio;
    private BigDecimal shortRatio;
}
