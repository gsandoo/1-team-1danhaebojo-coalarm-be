package _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VolumeSpikeAlertRequest extends BaseAlertRequest {
    @Null
    private Long marketId;

    private Boolean tradingVolumeSoaring;
  
    private Boolean tradingVolumeSoaring = true;
}
