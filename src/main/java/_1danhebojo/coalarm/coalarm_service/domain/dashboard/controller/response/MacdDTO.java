package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MacdDTO {
    private final double value;
    private final double signal;
    private final double histogram;
    private final String trend;
}