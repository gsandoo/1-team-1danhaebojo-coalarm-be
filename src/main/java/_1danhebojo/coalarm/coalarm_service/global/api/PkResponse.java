package _1danhebojo.coalarm.coalarm_service.global.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PkResponse {
    Long id;

    public static PkResponse of(Long id) {
        return PkResponse.builder()
                .id(id)
                .build();
    }
}
