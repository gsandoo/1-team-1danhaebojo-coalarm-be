package _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.VolumeSpikeEntity;
import lombok.Getter;

@Getter
public class VolumeSpikeResponse {
    private boolean tradingVolumeSoaring;

    public VolumeSpikeResponse(VolumeSpikeEntity volumeSpike) {
        if (volumeSpike != null) {
            this.tradingVolumeSoaring = volumeSpike.getTradingVolumeSoaring();
        }
    }
}
