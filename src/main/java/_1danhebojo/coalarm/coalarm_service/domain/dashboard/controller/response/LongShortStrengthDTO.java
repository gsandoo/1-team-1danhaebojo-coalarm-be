package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LongShortStrengthDTO {
    private Ratio ratio;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ratio {
        private double longRatio;
        private double shortRatio;
    }
}
