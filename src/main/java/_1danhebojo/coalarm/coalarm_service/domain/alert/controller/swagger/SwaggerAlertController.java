package _1danhebojo.coalarm.coalarm_service.domain.alert.controller.swagger;

import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Alert API", description = "알림 관련 API")
@RequestMapping("/swagger-alert")
public class SwaggerAlertController {

    @Operation(
            summary = "알람 추가",
            description = "새로운 알람을 추가합니다. \n\n" +
                    "- `TARGET_PRICE` 알람: 특정 가격 도달 시 알림 \n" +
                    "- `GOLDEN_CROSS` 알람: 골든 크로스 발생 시 알림 \n" +
                    "- `VOLUME_SPIKE` 알람: 거래량 급등 시 알림",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "알람 추가 요청 데이터",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "지정가 알람",
                                            value = "{ \"coin_id\": 1, \"type\": \"TARGET_PRICE\", \"percentage\": 15, \"target_price\": 1234.56, \"title\": \"알람 제목입니다\" }"
                                    ),
                                    @ExampleObject(
                                            name = "골든 크로스 알람",
                                            value = "{ \"coin_id\": 1, \"type\": \"GOLDEN_CROSS\", \"title\": \"알람 제목입니다\" }"
                                    ),
                                    @ExampleObject(
                                            name = "거래량 급등",
                                            value = "{ \"coin_id\": 1, \"type\": \"VOLUME_SPIKE\", \"title\": \"알람 제목입니다\" }"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "알람 추가 완료",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "성공 응답",
                                                    value = "{\n" +
                                                            "    \"status\": \"success\",\n" +
                                                            "    \"data\": {\n" +
                                                            "        \"id\": 1\n" +
                                                            "    }\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 - 코인 ID 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "400 오류 응답",
                                                    value = "{\n" +
                                                            "    \"status\": \"error\",\n" +
                                                            "    \"error\": {\n" +
                                                            "        \"code\": 400,\n" +
                                                            "        \"errors\": {}\n" +
                                                            "    }\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "500 오류 응답",
                                                    value = "{\n" +
                                                            "    \"status\": \"error\",\n" +
                                                            "    \"error\": {\n" +
                                                            "        \"code\": 500,\n" +
                                                            "        \"message\": \"서버 내부에 에러가 발생했습니다\"\n" +
                                                            "    }\n" +
                                                            "}"
                                            )
                                    }
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<BaseResponse<?>> addAlert() {
        return ResponseEntity
                .status(200)
                .body(BaseResponse.success());
    }

    @Operation(
            summary = "알람 활성화 수정",
            description = "기존 알람의 활성화를 수정합니다.\n\n" +
                    "- 사용자는 등록된 알람의 사용 여부를 변경할 수 있습니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "알람 수정 요청 데이터",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{ \"status\": true }"
                            )
                    )
            ),
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(name = "alert_id", description = "수정할 알람의 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "일림 수정 완료",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "성공 응답",
                                                    value = "{\n" +
                                                            "    \"status\": \"success\",\n" +
                                                            "    \"data\": {\n" +
                                                            "        \"id\": 1\n" +
                                                            "    }\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 - 코인 ID 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "404 오류 응답",
                                                    value = "{\n" +
                                                            "    \"status\": \"error\",\n" +
                                                            "    \"error\": {\n" +
                                                            "        \"code\": 404,\n" +
                                                            "        \"message\": \"해당하는 알림이 존재하지 않습니다.\"\n" +
                                                            "    }\n" +
                                                            "}"
                                            )
                                    }
                            )

                    ),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "500 오류 응답",
                                                    value = "{\n" +
                                                            "    \"status\": \"error\",\n" +
                                                            "    \"error\": {\n" +
                                                            "        \"code\": 500,\n" +
                                                            "        \"message\": \"서버 내부에 에러가 발생했습니다\"\n" +
                                                            "    }\n" +
                                                            "}"
                                            )
                                    }
                            )
                    )
            }
    )
    @PatchMapping("/{alert_id}")
    public ResponseEntity<BaseResponse<?>> updateAlert(@PathVariable("alert_id") int alertId) {
        return ResponseEntity
                .status(200)
                .body(BaseResponse.success());
    }

    @Operation(
            summary = "알람 삭제",
            description = "기존 알람을 삭제합니다.\n\n" +
                    "- 사용자는 등록한 알람을 삭제할 수 있습니다.\n" +
                    "- 삭제된 알람은 다시 복구할 수 없습니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(name = "alert_id", description = "삭제할 알람의 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "알람 삭제 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "성공 응답",
                                                    value = "{\n" +
                                                            "  \"status\": \"success\"\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "알람을 찾을 수 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "404 오류 응답",
                                                    value = "{\n" +
                                                            "  \"status\": \"error\",\n" +
                                                            "  \"error\": {\n" +
                                                            "    \"code\": 404,\n" +
                                                            "    \"message\": \"해당하는 알림이 존재하지 않습니다.\"\n" +
                                                            "  }\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "500 오류 응답",
                                                    value = "{\n" +
                                                            "  \"status\": \"error\",\n" +
                                                            "  \"error\": {\n" +
                                                            "    \"code\": 500,\n" +
                                                            "    \"message\": \"서버 내부에 에러가 발생했습니다\"\n" +
                                                            "  }\n" +
                                                            "}"
                                            )
                                    }
                            )
                    )
            }
    )
    @DeleteMapping("/{alert_id}")
    public ResponseEntity<BaseResponse<?>> deleteAlert(@PathVariable("alert_id") int alertId) {
        return ResponseEntity
                .status(200)
                .body(BaseResponse.success());
    }

    @Operation(
            summary = "알람 목록 조회",
            description = "사용자의 알람 목록을 조회합니다.\n\n" +
                    "- 사용자가 등록한 모든 알람을 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "알람 목록 조회 완료",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "골든 크로스 알람",
                                                    value = "{\n" +
                                                            "    \"status\": \"success\",\n" +
                                                            "    \"data\": {\n" +
                                                            "        \"contents\": [\n" +
                                                            "            {\n" +
                                                            "                \"alertId\": 1,\n" +
                                                            "                \"userId\": 1,\n" +
                                                            "                \"active\": true,\n" +
                                                            "                \"title\": \"골든 크로스 알람\",\n" +
                                                            "                \"coin\": {\n" +
                                                            "                    \"coinId\": 1,\n" +
                                                            "                    \"name\": \"비트코인\",\n" +
                                                            "                    \"symbol\": \"BTC\"\n" +
                                                            "                },\n" +
                                                            "                \"alertType\": \"GOLDEN_CROSS\",\n" +
                                                            "                \"goldenCross\": {\n" +
                                                            "                    \"shortMa\": 7,\n" +
                                                            "                    \"longMa\": 20\n" +
                                                            "                }\n" +
                                                            "            }\n" +
                                                            "        ],\n" +
                                                            "        \"offset\": 0,\n" +
                                                            "        \"limit\": 5,\n" +
                                                            "        \"totalElements\": 100,\n" +
                                                            "        \"hasNext\": true\n" +
                                                            "    }\n" +
                                                            "}"
                                            ),
                                            @ExampleObject(
                                                    name = "거래량 급등 알람",
                                                    value = "{\n" +
                                                            "    \"status\": \"success\",\n" +
                                                            "    \"data\": {\n" +
                                                            "        \"contents\": [\n" +
                                                            "            {\n" +
                                                            "                \"alertId\": 2,\n" +
                                                            "                \"userId\": 1,\n" +
                                                            "                \"active\": true,\n" +
                                                            "                \"title\": \"거래량 급등 알람\",\n" +
                                                            "                \"coin\": {\n" +
                                                            "                    \"coinId\": 2,\n" +
                                                            "                    \"name\": \"이더리움\",\n" +
                                                            "                    \"symbol\": \"ETH\"\n" +
                                                            "                },\n" +
                                                            "                \"alertType\": \"VOLUME_SPIKE\",\n" +
                                                            "                \"volumeSpike\": {\n" +
                                                            "                    \"tradingVolumeSoaring\": true\n" +
                                                            "                }\n" +
                                                            "            }\n" +
                                                            "        ],\n" +
                                                            "        \"offset\": 0,\n" +
                                                            "        \"limit\": 5,\n" +
                                                            "        \"totalElements\": 50,\n" +
                                                            "        \"hasNext\": true\n" +
                                                            "    }\n" +
                                                            "}"
                                            ),
                                            @ExampleObject(
                                                    name = "지정가 알람",
                                                    value = "{\n" +
                                                            "    \"status\": \"success\",\n" +
                                                            "    \"data\": {\n" +
                                                            "        \"contents\": [\n" +
                                                            "            {\n" +
                                                            "                \"alertId\": 3,\n" +
                                                            "                \"userId\": 1,\n" +
                                                            "                \"active\": true,\n" +
                                                            "                \"title\": \"지정가 알람\",\n" +
                                                            "                \"coin\": {\n" +
                                                            "                    \"coinId\": 3,\n" +
                                                            "                    \"name\": \"리플\",\n" +
                                                            "                    \"symbol\": \"XRP\"\n" +
                                                            "                },\n" +
                                                            "                \"alertType\": \"TARGET_PRICE\",\n" +
                                                            "                \"targetPrice\": {\n" +
                                                            "                    \"price\": 124000000,\n" +
                                                            "                    \"percentage\": -5\n" +
                                                            "                }\n" +
                                                            "            }\n" +
                                                            "        ],\n" +
                                                            "        \"offset\": 0,\n" +
                                                            "        \"limit\": 5,\n" +
                                                            "        \"totalElements\": 30,\n" +
                                                            "        \"hasNext\": false\n" +
                                                            "    }\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "500 오류 응답",
                                                    value = "{\n" +
                                                            "    \"status\": \"error\",\n" +
                                                            "    \"error\": {\n" +
                                                            "        \"code\": 500,\n" +
                                                            "        \"message\": \"서버 내부에 에러가 발생했습니다\"\n" +
                                                            "    }\n" +
                                                            "}"
                                            )
                                    }
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAlerts() {
        return ResponseEntity
                .status(200)
                .body(BaseResponse.success());
    }
}