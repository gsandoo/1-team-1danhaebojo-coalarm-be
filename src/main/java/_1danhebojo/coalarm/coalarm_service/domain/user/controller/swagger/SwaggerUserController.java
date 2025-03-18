package _1danhebojo.coalarm.coalarm_service.domain.user.controller.swagger;

import _1danhebojo.coalarm.coalarm_service.global.api.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "User API", description = "회원 관리 API")
@RequestMapping("/swagger-user")
public class SwaggerUserController {
    @Operation(
            summary = "디스코드 웹훅 연동",
            description = "디스코드 웹훅을 등록하여 알림을 받을 수 있도록 설정합니다.",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT 인증 토큰", required = true, in = ParameterIn.HEADER)
            },
            requestBody = @RequestBody(
                    description = "디스코드 웹훅 설정 데이터",
                    required = true,
                    content = @Content(schema = @Schema(
                            example = "{ \"web_hook_url\": \"https://discord.discord.discord\" }"
                    ))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "디스코드 웹훅 연동 성공"),
                    @ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @PatchMapping("/discord")
    public ResponseEntity<BaseResponse<?>> discordWebhook(
            @RequestHeader("Authorization") String token)
    {
        return ResponseEntity.status(200).body(BaseResponse.success());
    }

    @Operation(
            summary = "회원 정보 수정",
            description = "회원의 닉네임 및 프로필 이미지를 수정합니다.",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT 인증 토큰", required = true, in = ParameterIn.HEADER)
            },
            requestBody = @RequestBody(
                    description = "회원 정보 수정 요청 데이터",
                    required = true,
                    content = @Content(schema = @Schema(
                            example = "{ \"nickname\": \"NewUsername\", \"profile_image\": \"https://s3.bucket.com/profile.jpg\" }"
                    ))
            ),
            responses = {
                @ApiResponse(responseCode = "201", description = "회원가입 또는 닉네임 변경 성공",
                        content = @Content(
                                mediaType = "application/json",
                                examples = {
                                        @ExampleObject(
                                                name = "성공 응답",
                                                value = "{\n" +
                                                        "  \"status\": \"success\",\n" +
                                                        "  \"data\": {\n" +
                                                        "    \"id\": 1\n" +
                                                        "  }\n" +
                                                        "}"
                                        )
                                }
                        )
                ),
                @ApiResponse(responseCode = "400", description = "유효성 검사 실패",
                        content = @Content(
                                mediaType = "application/json",
                                examples = {
                                        @ExampleObject(
                                                name = "400 오류 응답 (유효성 검사 실패)",
                                                value = "{\n" +
                                                        "  \"status\": \"error\",\n" +
                                                        "  \"error\": {\n" +
                                                        "    \"code\": 400,\n" +
                                                        "    \"message\": \"유효성 검사 실패\",\n" +
                                                        "    \"errors\": {\n" +
                                                        "      \"nickname\": \"닉네임은 최소 2자 이상이어야 합니다\"\n" +
                                                        "    }\n" +
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
    @PatchMapping
    public ResponseEntity<BaseResponse<?>> updateUserProfile(
            @RequestHeader("Authorization") String token)
    {
        return ResponseEntity.status(200).body(BaseResponse.success());
    }

    @Operation(
            summary = "소셜 로그인(회원가입)",
            description = "카카오, 네이버 등의 소셜 로그인 정보를 이용하여 회원가입을 진행합니다.",
            parameters = {
                    @Parameter(name = "provider", description = "소셜 구분 (예: kakao, naver, google)", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "소셜 로그인 요청 데이터",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\n" +
                                            "  \"provider\": \"kakao\",\n" +
                                            "  \"accessToken\": \"adsadasga;sdfmasaQWfasd121\"\n" +
                                            "}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "회원가입 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "성공 응답",
                                                    value = "{\n" +
                                                            "  \"status\": \"success\",\n" +
                                                            "  \"data\": {\n" +
                                                            "    \"id\": 1\n" +
                                                            "  }\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "유효성 검사 실패",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "400 오류 응답",
                                                    value = "{\n" +
                                                            "  \"status\": \"error\",\n" +
                                                            "  \"error\": {\n" +
                                                            "    \"code\": 400,\n" +
                                                            "    \"message\": \"유효성 검사 실패\"\n" +
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
    @PostMapping("/auth/{provider}")
    public ResponseEntity<BaseResponse<?>> socialLogin(
            @PathVariable String provider,
            @RequestBody Map<String, String> request)
    {
        return ResponseEntity
                .status(200)
                .body(BaseResponse.success());
    }

    @Operation(
            summary = "로그인",
            description = "소셜 로그인 방식으로 사용자를 인증합니다.\n" +
                        "제공된 액세스 토큰을 검증하여 유효한 사용자인 경우 로그인 처리를 완료합니다.",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT 인증 토큰", required = true, in = ParameterIn.HEADER)
            },
            requestBody = @RequestBody(
                    description = "로그인 요청 데이터 (provider: 로그인 제공자, accessToken: 인증 토큰)",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<?>> logout()
    {
        return ResponseEntity
                .status(200)
                .body(BaseResponse.success());
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "현재 로그인한 사용자의 계정을 삭제합니다.\n\n" +
                    "- 탈퇴 요청 시, 인증 토큰이 필요합니다.\n" +
                    "- 탈퇴 후 해당 계정의 데이터는 삭제됩니다.",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT 인증 토큰", required = true, in = ParameterIn.HEADER)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원탈퇴 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @DeleteMapping
    public ResponseEntity<BaseResponse<?>> deleteUser(@RequestHeader("Authorization") String token)
    {
        return ResponseEntity
                .status(200)
                .body(BaseResponse.success());
    }
}
