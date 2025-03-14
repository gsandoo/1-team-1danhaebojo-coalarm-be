package _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.swagger;

import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Dashboard API", description = "대시보드 관련 API")
@RequestMapping("/swagger-dashboard")
public class SwaggerDashboardController {

    @Operation(
            summary = "코인 지표 조회",
            description = "특정 코인의 RSI, MACD, Long Ratio 등의 지표를 조회합니다.\n\n" +
                        "- MACD(Moving Average Convergence Divergence)는 12일 이동평균선과 26일 이동평균선의 차이를 나타냅니다.\n" +
                        "- RSI(Relative Strength Index)는 시장의 과매수 및 과매도를 측정하는 지표입니다.\n" +
                        "- Long Ratio는 롱 포지션과 숏 포지션 비율을 나타냅니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(name = "coin_id", description = "조회할 코인의 ID", required = true),
                    @Parameter(name = "Authorization", description = "JWT 인증 토큰", required = true, in = ParameterIn.HEADER)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "코인 지표 조회 완료",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "성공 응답",
                                                    value = "{\n" +
                                                            "  \"status\": \"success\",\n" +
                                                            "  \"data\": {\n" +
                                                            "    \"coin\": {\n" +
                                                            "      \"coinId\": \"1\",\n" +
                                                            "      \"symbol\": \"BTC\",\n" +
                                                            "      \"name\": \"비트코인\"\n" +
                                                            "    },\n" +
                                                            "    \"rsi\": 42.3,\n" +
                                                            "    \"macd\": {\n" +
                                                            "      \"value\": -12.3,\n" +
                                                            "      \"signal\": -13,\n" +
                                                            "      \"histogram\": 0.7,\n" +
                                                            "      \"trend\": \"RISE\"\n" +
                                                            "    },\n" +
                                                            "    \"ratio\": {\n" +
                                                            "      \"long\": 55.5,\n" +
                                                            "      \"short\": 44.5\n" +
                                                            "    }\n" +
                                                            "  }\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "잘못된 요청 - 코인이 존재하지 않음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "404 오류 응답",
                                                    value = "{\n" +
                                                            "  \"status\": \"error\",\n" +
                                                            "  \"error\": {\n" +
                                                            "    \"code\": 400,\n" +
                                                            "    \"message\": \"코인이 존재하지 않습니다.\"\n" +
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
    @GetMapping("/{coin_id}/index")
    public ResponseEntity<BaseResponse<?>> getCoinIndex(
            @PathVariable("coin_id") Long coinId)
    {
        return ResponseEntity
                .status(200)
                .body(BaseResponse.success());
    }

    @Operation(
            summary = "김치 프리미엄 조회",
            description = "특정 코인의 국내 가격(KRW)과 글로벌 가격(USD)을 비교하여 김치 프리미엄을 조회합니다.\n" +
                        "김치 프리미엄 값은 `(국내 가격 - 글로벌 가격) / 글로벌 가격 * 100`으로 계산됩니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(name = "offset", description = "페이징 시작점", required = true),
                    @io.swagger.v3.oas.annotations.Parameter(name = "limit", description = "한 번에 가져올 데이터 개수", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "김치 프리미엄 조회 완료",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "성공 응답",
                                                    value = "{\n" +
                                                            "  \"status\": \"success\",\n" +
                                                            "  \"data\": {\n" +
                                                            "    \"contents\": [\n" +
                                                            "      {\n" +
                                                            "        \"premiumId\": 1,\n" +
                                                            "        \"domesticPrice\": 139293000,\n" +
                                                            "        \"globalPrice\": 95783.59,\n" +
                                                            "        \"exchangeRate\": 1350,\n" +
                                                            "        \"kimchiPremium\": 1.27,\n" +
                                                            "        \"dailyChange\": -2.53,\n" +
                                                            "        \"coin\": {\n" +
                                                            "          \"coinId\": 1,\n" +
                                                            "          \"name\": \"비트코인\",\n" +
                                                            "          \"symbol\": \"BTC\"\n" +
                                                            "        }\n" +
                                                            "      }\n" +
                                                            "    ],\n" +
                                                            "    \"offset\": 0,\n" +
                                                            "    \"limit\": 5,\n" +
                                                            "    \"totalElements\": 100,\n" +
                                                            "    \"hasNext\": true\n" +
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
    @GetMapping("/kimchi")
    public ResponseEntity<BaseResponse<?>> getKimchiPremium(
            @RequestParam int offset,
            @RequestParam int limit)
    {
        return ResponseEntity
                .status(200)
                .body(BaseResponse.success());
    }

    @Operation(
            summary = "차트 데이터 조회",
            description = "특정 코인의 차트 데이터를 조회합니다." +
                        "- 시세 데이터, 이동 평균선, 거래량 등 차트 관련 데이터를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "차트 데이터 조회"),
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
    @GetMapping("/chart")
    public ResponseEntity<BaseResponse<?>> getChartData()
    {
        return ResponseEntity
                .status(200)
                .body(BaseResponse.success());
    }

    @Operation(
            summary = "실시간 체결 내역 조회",
            description = "실시간으로 발생한 체결 내역을 조회합니다." +
                    "- 체결 가격, 체결량, 체결 시간 등을 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "체결 데이터 조회"),
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
    @GetMapping("/trade")
    public ResponseEntity<BaseResponse<?>> getTradeData()
    {
        return ResponseEntity
                .status(200)
                .body(BaseResponse.success());
    }
}
