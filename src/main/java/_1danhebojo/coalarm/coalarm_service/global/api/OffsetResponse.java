package _1danhebojo.coalarm.coalarm_service.global.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OffsetResponse<T> {
    List<T> contents;
    long offset;
    long limit;
    long totalElements;
    boolean hasNext;
    public static <T> OffsetResponse<T> of(
            List<T> contents,
            long offset,
            long limit,
            long totalElements
    ) {
        boolean hasNext = (offset + limit) < totalElements;
        return OffsetResponse.<T>builder()
                .contents(contents)
                .offset(offset)
                .limit(limit)
                .totalElements(totalElements)
                .hasNext(hasNext)
                .build();
    }
}
