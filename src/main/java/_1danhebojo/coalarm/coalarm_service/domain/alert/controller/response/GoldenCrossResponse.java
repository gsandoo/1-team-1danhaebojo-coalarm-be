package _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.GoldenCrossEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoldenCrossResponse {
    private long shortMa;
    private long longMa;

    public GoldenCrossResponse(GoldenCrossEntity goldenCross) {
        if (goldenCross != null) {
            this.shortMa = goldenCross.getShortMa() != null ? goldenCross.getShortMa() : 0L;
            this.longMa = goldenCross.getLongMa() != null ? goldenCross.getLongMa() : 0L;
        }
    }
}
