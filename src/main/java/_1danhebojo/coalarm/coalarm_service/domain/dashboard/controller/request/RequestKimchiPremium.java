package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestKimchiPremium {
    @NotNull(message = "offset 값은 필수입니다.")
    @Min(value = 0, message = "offset은 0 이상이어야 합니다.")
    private Integer offset;

    @NotNull(message = "limit 값은 필수입니다.")
    @Min(value = 1, message = "limit은 1 이상이어야 합니다.")
    private Integer limit;

    public RequestKimchiPremium(Integer offset, Integer limit) {
        this.offset = offset;
        this.limit = limit;
    }
}
