# CHANGELOG

## [1.23.5](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.23.4...v1.23.5) (2025-03-30)

### 🐛 BUG FIXES

* 실시간 거래체결 웹 소켓 연결 ([88818fb](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/88818fbfc72f31be1e321629dc03a9835b58d8fa))

## [1.23.4](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.23.3...v1.23.4) (2025-03-30)

### 🐛 BUG FIXES

* 코인 검색 결과 없을 때 404 대신 빈 배열 반환 ([f418663](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/f41866382ea8614360272b7bb7f397e5e79fa834))

## [1.23.3](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.23.2...v1.23.3) (2025-03-27)

### 🐛 BUG FIXES

* SSE 요청 시 GlobalExceptionHandler에서 BaseResponse 반환으로 인한 예외 분기 처리 ([7a5226b](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/7a5226bdab1dda6daa08b2ad4b1d1941474bb933))

### ♻️ REFACTORING

* SSE 알람 response 추가 ([edc70c4](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/edc70c43f12830980974ad935476191e51e35123))
* 사용하지 않는 값 제거 ([613e7b7](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/613e7b72427561167bfad782042d56fc095eafe4))
* 임시 AlertDto 제거 및 AlertSSEResponse로 반환 변경 ([c3f9f92](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/c3f9f92de9a06c3720b8045990a3f9fe7b164da8))

## [1.23.2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.23.1...v1.23.2) (2025-03-27)

### 🐛 BUG FIXES

* 알람 관련 수정 ([6946fab](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/6946fab944a2266f27ee76fce6a78ebdf0e77eb7))

## [1.23.1](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.23.0...v1.23.1) (2025-03-27)

### ♻️ REFACTORING

* 알람 관련 스케줄러 주기 yml 파일 관리 ([652ac90](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/652ac90527493e89f2f60f4fd546307e18d23bf4))
* 알람 메세지 전송 큐로 관리 ([0a15f7a](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/0a15f7ad5b816bceef5f3e9c2efbc5bdbbe726ca))

## [1.23.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.22.0...v1.23.0) (2025-03-27)

### 🚀 NEW FEATURES

* SSE 연결 관련 수정 ([afeda01](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/afeda014cb5d50bc15c7d6d2d197472ec1ab0296))

## [1.22.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.21.3...v1.22.0) (2025-03-27)

### 🚀 NEW FEATURES

* 여러 코인 검색이 가능하도록 API 수정하고 에러 처리 추가 ([ed1fa42](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/ed1fa42107e50b75eb3d950091fcaf09246d8e23))

## [1.21.3](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.21.2...v1.21.3) (2025-03-27)

### 🐛 BUG FIXES

* 알람 삭제 수정 ([42e90cd](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/42e90cd434c7326e39acad09021b454bcf372b8e))

## [1.21.2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.21.1...v1.21.2) (2025-03-27)

### ♻️ REFACTORING

* addEmitter 수정 ([f9432e2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/f9432e27d20edb9f17241f4336df168e4f1dfac9))
* 알람 수정 조회 수정 ([480a4d8](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/480a4d8fe8140ac936b8e0191b947a2ce9cdb4f7))
* 알람 추가 시 알람 정보 반환 ([179963f](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/179963f0c4f87e699cc813d6c2b387133b5262fc))

## [1.21.1](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.21.0...v1.21.1) (2025-03-27)

### 🐛 BUG FIXES

* Jwt 스케줄러에 transactional 추가 ([450270f](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/450270f515554a354ee3fc80f8090cb337c993ec))
* 골든크로스 예외 처리 추가 ([f37142b](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/f37142b6e1c7e20ca435cfa4e7c5936e8e83f850))
* 알람 정보 저장 시 refresh 추가 ([55a220a](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/55a220aef27cbf9879275b90516b39b0cb1ff646))

### ♻️ REFACTORING

* exception 관련 수정 ([808f8b7](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/808f8b73595e3dc2f80453f9833a2e560ec273c1))
* VolumeSpike 전체 조회 관련 수정 ([75cb717](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/75cb717420f15a76e12c045813c0b57be1c57987))
* 매개변수 명 수정 ([e7d47ea](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/e7d47eabd9bc07e918aa504e45c59846c97e6889))
* 사용하지 않는 코드 제거 ([708ff39](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/708ff39c43a23acbe578dcb164e969892ad1a091))
* 알람 중복 확인 로직 수정 ([8923386](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/8923386896cb865f8a9dd477bc4dc7fab20edb6f))
* 예외 처리 코드 추가 ([9b44921](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/9b44921b05dd7d0802f1b8b5b54389df0fb8d45f))

## [1.21.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.20.0...v1.21.0) (2025-03-26)

### 🚀 NEW FEATURES

* 김치 프리미엄 리팩토링 ([fa7c73b](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/fa7c73bf072044a0c18b19137da1972d3e85d8f5))

### 🐛 BUG FIXES

* sse 컨트롤러 수정 & SSE연결 ([766f530](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/766f5301fcf9d2e660f163bfb0b28adca5bef810))

## [1.20.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.19.6...v1.20.0) (2025-03-26)

### 🚀 NEW FEATURES

* 알람 테스트 URL 추가 ([9852d47](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/9852d47061901b7a10c1ae8f0ac5c972269dbbd6))

### 🐛 BUG FIXES

* 객체 맵핑 변경 ([db1731b](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/db1731b8915a26fc127ddde904b140c3aa45ffff))
* 이름 충돌로 인한 변수명 수정 ([2df809a](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/2df809a4be032f880603692e8c1b8f849c754763))

## [1.19.6](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.19.5...v1.19.6) (2025-03-26)

### 📚 DOCUMENTATION

* 개발환경 deploy.yml 명칭 수정-2 ([55e3a80](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/55e3a80d07b7b00f235d4c0da65530fa3999426b))

## [1.19.5](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.19.4...v1.19.5) (2025-03-26)

### 📚 DOCUMENTATION

* 개발환경 deploy.yml 명칭 수정 ([3240eae](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/3240eaef90b54e5a407a40fba5b416d0e837e884))

## [1.19.4](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.19.3...v1.19.4) (2025-03-25)

### 🐛 BUG FIXES

* 병합중 발생한 오류 수정 ([24efc51](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/24efc51db61855eba4a5b4bf750814f64ed3cb5c))

## [1.19.3](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.19.2...v1.19.3) (2025-03-25)

### 🐛 BUG FIXES

* 알람 Emitter 로직 수정 ([55bc4d1](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/55bc4d1a2219d89d0f2e1c08e4a19108cc2724bc))

## [1.19.2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.19.1...v1.19.2) (2025-03-25)

### 🐛 BUG FIXES

* 충돌 해결 ([4021661](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/402166174b0fa18fcd3ad1e3c050e0bebd0b05d2))

## [1.19.1](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.19.0...v1.19.1) (2025-03-25)

### ♻️ REFACTORING

* goldenCross, VolumeSpike에서 필수값 제거 ([1b2593d](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/1b2593dd12514c3513a5e8fa16f330a13e52008f))
* user 정보 조회 추가 ([21e997e](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/21e997ef1361c0a4ed9425afe4418ca1ef24da36))
* volumeSpike user 정보 조회 추가 ([dbf17b4](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/dbf17b43e3cd1b0503c6b9bff539f25ed0d38839))
* 알람 추가시 userId 추가 ([41d2368](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/41d2368910a5d49eec9594d54fa0745773066421))
* 코인 심볼 추가 ([f9bdeb8](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/f9bdeb83127dd6173f7df571aa34f335e2d13938))

## [1.19.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.18.2...v1.19.0) (2025-03-25)

### 🚀 NEW FEATURES

* 롱숏 비율 계산 방식 longStrength 기반으로 변경 ([8ff0bf2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/8ff0bf27ed54076d584a057999259ff5ba6440e1))

## [1.18.2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.18.1...v1.18.2) (2025-03-24)

### ♻️ REFACTORING

* 불필요한 설정 제거 및 updateDiscordWebhook 조건문 추가 ([f6557be](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/f6557bef521c4351c4038b74c520f970916bdb74))

## [1.18.1](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.18.0...v1.18.1) (2025-03-24)

### ♻️ REFACTORING

* getAlertList와 addAlert 메서드에 userId를 활용 ([e9dc2e0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/e9dc2e098a0034af51f3d7d38dbad8f9409c54b0))

## [1.18.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.17.10...v1.18.0) (2025-03-24)

### 🚀 NEW FEATURES

* 알람 추가 시 userId 필드 포함하도록 구현 ([42b5f6a](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/42b5f6a77208a49777088cba83ce69ac94dc1d0e))

### ♻️ REFACTORING

* GoldenCrossRequest에서 사용하지 않는 필드의 @NotNull 어노테이션 제거 ([6852cf9](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/6852cf99a52b7bfcd45b4c2b53a8e853cad2bf0b))
* 사용되지 않는 필드의 @NotNull 제거 ([2c355df](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/2c355dfadae13583ff9579916206640b59340217))
* 생성자 관련 어노테이션 추가 및 User 엔티티 매핑 수정 ([e4fa5ff](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/e4fa5ff7857ecd9616c15b8dff5b661e9e05b187))

## [1.17.10](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.17.9...v1.17.10) (2025-03-24)

### 🐛 BUG FIXES

* oAuth 2.0 리다이렉트 에러 테스트 ([7b1b6de](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/7b1b6dee4d2f0078e6eaf4263902cf3e71d7053d))

## [1.17.9](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.17.8...v1.17.9) (2025-03-24)

### 🐛 BUG FIXES

* 401 에러 수정(테스트) ([8fbddbd](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/8fbddbdc9ee2eb359720cf6fb553c6935563b668))

## [1.17.8](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.17.7...v1.17.8) (2025-03-24)

### 🐛 BUG FIXES

* 401 에러 수정(테스트) ([b362df4](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/b362df43e4e4630fd0e6f2ec0c1f9fa04ffa98f4))

## [1.17.7](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.17.6...v1.17.7) (2025-03-24)

### 🐛 BUG FIXES

* 401 에러 수정 ([6efd755](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/6efd755ed9c120e6d4f5eff1bc564525c4556744))

## [1.17.6](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.17.5...v1.17.6) (2025-03-24)

### 🐛 BUG FIXES

* 401 에러 수정 ([71fad31](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/71fad319deab464d788872c25e17f29a26c7648b))

## [1.17.5](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.17.4...v1.17.5) (2025-03-24)

### 🐛 BUG FIXES

* security config 테스트 임시 허용 ([49b1619](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/49b16198778cb78290da88ee85b1b649b4fa8784))

## [1.17.4](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.17.3...v1.17.4) (2025-03-24)

### 📚 DOCUMENTATION

* deploy.yml 수정 ([89a7d58](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/89a7d5800a20a80c8c21fa2a808b9d4f3f362a24))

## [1.17.3](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.17.2...v1.17.3) (2025-03-24)

### 📚 DOCUMENTATION

* deploy.yml 수정 ([290c4b3](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/290c4b3972af9520b528179add19a9d2cca916bc))

## [1.17.2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.17.1...v1.17.2) (2025-03-24)

### 📚 DOCUMENTATION

* deploy.yml 수정 ([e44b513](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/e44b51340f678881ebc8889c02ecd5782384cf48))

## [1.17.1](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.17.0...v1.17.1) (2025-03-24)

### 📚 DOCUMENTATION

* deploy.yml 수정 ([450875d](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/450875d8d2b5b3551a4a4613cc6b3002eb8c6098))

## [1.17.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.16.2...v1.17.0) (2025-03-23)

### 🚀 NEW FEATURES

* 코인 검색 api 구현 ([3e75a67](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/3e75a6726d5a17def675022245236a6a53103961))

## [1.16.2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.16.1...v1.16.2) (2025-03-23)

### ♻️ REFACTORING

* 대시보드 응답 포맷 통일 ([8397394](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/839739405f8037249df3767809bcc907fb03c0ba))

## [1.16.1](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.16.0...v1.16.1) (2025-03-23)

### 🐛 BUG FIXES

* WebConfig 파일 수정 ([b44f42a](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/b44f42a4c2128e30831af7b5a590a5be6f1177bd))

## [1.16.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.15.0...v1.16.0) (2025-03-23)

### 🚀 NEW FEATURES

* 코인 조회 API 추가 ([a34bc07](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/a34bc07c50783fa511d61282d432d80315a6f44f))

### 🐛 BUG FIXES

* 충돌 해결 ([06bb420](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/06bb420f3e823d13041e138e12932a81dec48a8f))

## [1.15.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.14.0...v1.15.0) (2025-03-21)

### 🚀 NEW FEATURES

* 인증 없이 접근 가능한 요청 범위 수정 ([b98f003](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/b98f00323f8c0c03f80914487e9490873e516705))
* 카카오 로그인 경로 필터 제외 및 인증 설정 수정 ([831061d](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/831061dfdf024a098d7b9dc78b320d61799f5151))

## [1.14.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.13.3...v1.14.0) (2025-03-21)

### 🚀 NEW FEATURES

* cors 설정 추가 및 로그인 url API 작성 ([31ef292](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/31ef2929b0659c44283e30d8fd90473771407104))

## [1.13.3](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.13.2...v1.13.3) (2025-03-21)

### 🐛 BUG FIXES

* 대시보드 컨트롤러 엔드포인트 수정 ([8f86972](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/8f86972c57fc3c31f7f4b858cd01afbfc02f1e7c))

## [1.13.2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.13.1...v1.13.2) (2025-03-21)

### 🐛 BUG FIXES

* security config 수정 ([bf09de9](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/bf09de93678476011fda528bc2b5ff63764ab962))

## [1.13.1](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.13.0...v1.13.1) (2025-03-21)

### 🐛 BUG FIXES

* swagger 테스트 ([94b5340](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/94b534070edc6137a0c61b65124f171dba72d2e2))

## [1.13.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.12.0...v1.13.0) (2025-03-21)

### 🚀 NEW FEATURES

* 엔드포인트 api/v1 분기처리 ([227c210](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/227c21025def16227fedc5e759d79d6417cf4e20))
* 회원탈퇴시 유저의 알람 삭제 ([e41ce6a](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/e41ce6a03d8ef6fc646ee418de5d5aedb5619e50))

## [1.12.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.11.1...v1.12.0) (2025-03-21)

### 🚀 NEW FEATURES

* 5분 이내 알림 전송 여부 확인 쿼리 추가 ([b1bc03a](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/b1bc03a5dc3eccaeaeee3c0fd17f1fa05fc1cb38))
* 알람 수정 및 제거 로직 개선 ([73a09f2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/73a09f2086aa263e4d799001c395702b905116b5))

### ♻️ REFACTORING

* 사용하지 않는 코드 제거 및 쿼리 개선 ([63276aa](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/63276aac8d9fda60eb521cf8a727ed203c744c94))

## [1.11.1](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.11.0...v1.11.1) (2025-03-20)

### 🐛 BUG FIXES

* AlertController 수정 ([84ad323](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/84ad323b7e02d182fcbbca7a7cc110dc44647714))
* alertController 에러  수정 ([2b1df39](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/2b1df39355969345446772abb1f75c92d21f1c66))

## [1.11.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.10.0...v1.11.0) (2025-03-20)

### 🚀 NEW FEATURES

* 회원 로그인/로그아웃 시 SSE 구독/해제 추가 ([f2ebd33](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/f2ebd33f86f89b14d430c94246bab86cc329dccd))

## [1.10.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.9.0...v1.10.0) (2025-03-20)

### 🚀 NEW FEATURES

* cicd 테스트 ([e64b178](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/e64b178651fe10326d94d677f09b82efe229436e))

### 🐛 BUG FIXES

* 헬스체크 엔드포인트 변경 ([d46e60b](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/d46e60b7fd019af9e25977637c76dae609a748a0))

## [1.9.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.8.1...v1.9.0) (2025-03-20)

### 🚀 NEW FEATURES

* health check controller 테스트 2 ([2b5669a](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/2b5669a8acb94c982aaf22b1581afe9af497cddd))

## [1.8.1](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.8.0...v1.8.1) (2025-03-20)

### 🐛 BUG FIXES

* 알림 추가 시 SSE에 반영되지 않는 버그 수정 ([34fc366](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/34fc3660bc839293631a29a7a1fba42a42474c7e))

## [1.8.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.7.0...v1.8.0) (2025-03-20)

### 🚀 NEW FEATURES

* 디스코드 웹훅 유효성 검사 추가 및 알람 수정 시 SSE 알람 제거 처리 ([eeca22b](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/eeca22b73afadb85633242aa8c2467977ebdf218))

## [1.7.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.6.0...v1.7.0) (2025-03-20)

### 🚀 NEW FEATURES

* 디스코드 웹훅 연동 API 추가 ([2310342](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/2310342902bea9fc4952436cb8a18bf88446afc8))

## [1.6.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.5.0...v1.6.0) (2025-03-20)

### 🚀 NEW FEATURES

* formatUtil 추가 ([c1c628d](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/c1c628d19c91c6b46402d2614dc9e63bb7686f6d))
* 구독 관련 엔티티 추가 및 수정 ([6da2e74](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/6da2e742dc1a87d2a85e8e5bbe2f83a2320d4d1e))
* 디스코드 웹훅 연결 기능 추가 ([df6cbc5](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/df6cbc53c3dacb621618be87bc66de67e1bc6347))
* 스케줄링 설정 추가 ([98aad79](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/98aad7991714bdbf0ed2308abede66521173b59e))
* 알람 SSE 관련 추가 ([0f367a1](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/0f367a19710f6c7901aef4325aa1386493fd5bcf))
* 알람 관련 누락된 엔티티 추가 및 Request/Response 추가 ([07ece15](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/07ece153f10b5f930695214539150388c14ffd11))
* 알람 세부 내용 추기 ([f7a4271](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/f7a42712755716c65900ceb8405d3d4cfdb81645))
* 알림 히스토리 관련 기능 추가 ([e570046](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/e57004682384439dab3cfb802bd62b413ace04bf))

## [1.5.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.4.0...v1.5.0) (2025-03-20)

### 🚀 NEW FEATURES

* aws eb cicd ([a9e427b](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/a9e427b7fce132e3b660d2bf539db328109a83b1))
* aws eb cicd test ([be0eaa1](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/be0eaa1b743b5fcba368928d92712ad3b76a4506))
* aws eb cicd test ([d4b9aea](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/d4b9aea3ef66a30016d5c9cc59784addb2e1fffd))
* aws eb cicd test ([e142561](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/e1425616d9eb17219558c958af29ae873f015a3a))
* aws eb cicd test ([9da3016](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/9da30169058d4560a1ae7e158fe3814af9eaba93))
* aws eb cicd test ([ff1455a](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/ff1455ac3da942a7af1f4f88831db984256b130e))
* aws eb cicd test ([e2075e7](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/e2075e7fce90304bc21505985af6f2562f296a3b))
* aws eb cicd test ([b4e48bd](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/b4e48bd97b4f7f5af2a079ae99620018fc767eb5))
* aws eb cicd test ([c11924b](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/c11924ba347677572799666e4dcf53d451dacac1))
* health check controller 삽입 ([826302a](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/826302ac6ed90083d058c16d9eef921f584d1a5b))
* health check controller 테스트 ([070f89a](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/070f89a796e00fa1d9788299dea1e94f2c57b6c6))

## [1.4.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.3.0...v1.4.0) (2025-03-20)

### 🚀 NEW FEATURES

* AuthController 추가 및 Refresh Token 기능 구현 ([4e270e3](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/4e270e3fd7217f43b4b9f48b1a52af4eb3885ad0))
* JWT Token Provider 및 설정 추가 ([c03a41d](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/c03a41d530dabb89ae93c81519d37bda07587421))
* JWT 기반 인증 및 카카오 OAuth 로그인 적용 ([2041c04](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/2041c040b7b2a25f285070bba507ad48a1e1a136))
* JWT 블랙리스트 기반 로그아웃 기능 구현 ([18e121e](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/18e121ecc88c50ee70c10d219bd2a4884afbf688))
* Refresh Token 및 로그아웃 기능 추가 ([7f26ed5](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/7f26ed51447ef120788192ae7bf935a8131f4746))
* Spring Security 인증 및 인가 예외 처리 추가 ([262da9c](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/262da9c6a56df99ad3d11f3c6eb096bc778f406e))
* 랜덤 닉네임 생성 유틸 추가 ([ba9605e](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/ba9605ef4df2ab7d5db0771927d88d2a25565484))
* 유저 서비스 및 회원가입 기능 추가 ([74669cb](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/74669cb1b5ae0f382c480d4d2d3ee0543f1c0d21))
* 카카오 로그인 후 JWT 토큰 발급 로직 추가 ([d75b638](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/d75b638384c022d2fc890791ca6347a46e374a92))
* 회원 탈퇴 API 추가 ([acf14da](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/acf14da1243e6b33341a73766c1bee40a609ae95))
* 회원정보 수정 추가 ([8da5bbc](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/8da5bbc1e47004eeda0f0beb266a00afcd6c590c))

### 🐛 BUG FIXES

* merge  branch 'feature/dashboard' into feature/user ([09c22d2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/09c22d226cb52dd8ccc89de8534ce3356e6099e2))
* Merge branch 'develop' into feature/user ([d2bb496](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/d2bb49686563c709b7a6d16fb1549bc6753914fc))
* Merge branch 'feature/user' of https://github.com/hyoon1129/1-team-1danhaebojo-coalarm-be into feature/user ([f45e566](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/f45e56699f8e90348a89bcdc878a9bcf85e63c48))
* 카카오 OAuth 토큰 요청 시 Content-Type 문제 해결 ([c714616](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/c71461604ed794022bf74331287dfe05756725ff))

### ♻️ REFACTORING

* OAuth 로그인 및 인증 기능 개선 ([7e3f9cd](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/7e3f9cdcb8c3ff9d38fb7aa6df0807fd82f628a8))
* UserController의 비즈니스 로직을 서비스 레이어로 이동 ([47036a9](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/47036a91326b35fa8aea6cf5e325386e9068dc42))
* 보안 설정 및 JWT 필터 코드 정리 ([b771eee](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/b771eeeb962d9619b49e42df40b5c0b212f56023))
* 폴더 구조 변경 ([4ee60c2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/4ee60c28e5f3603c713b853e6b70d42c388bed3e))

## [1.3.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.2.0...v1.3.0) (2025-03-19)

### 🚀 NEW FEATURES

* TargetPrice, VolumeSpike, GoldenCross를 위한 Entity, Response, JPA Repository 추가 ([f9a47e7](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/f9a47e739e2d0ca061590ddf89f0a499c9d47c1c))
* 알람 추가/수정/삭제/조회 기능 구현 ([df93db8](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/df93db8a805465a2368000fcf293cc45a2abfc81))
* 알람 히스토리 조회 및 상세 조회 기능 추가 ([c470e86](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/c470e865693b0a99feadfd240215f17730432b87))

## [1.2.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.1.0...v1.2.0) (2025-03-18)

### 📚 DOCUMENTATION

* 전체 API Swagger 문서 업데이트 ([4dd4769](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/4dd47698228d5998b15d5819ca0ebda4a8e4dbc3))

### 🚀 NEW FEATURES

* dashboard 도메인 셋팅 ([7f9c431](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/7f9c431a22ca41d79cfa5642918189e907a51d4a))
* JWT Token Provider 및 설정 추가 ([35cb9a4](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/35cb9a4ecf52cc96c20a4aec3d92834780221402))
* MACD 계산 API 추가 ([c75a93b](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/c75a93bfe459e39bd778e605e7ee18425fc658b2))
* QueryDsl 설정 ([9faca64](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/9faca644d221a2ebdb77710db0e637605055a609))
* RSI 계산 로직 추가 및 대시보드 지표 조회 API 수정 ([56fcfa5](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/56fcfa56f30c4358590fbe881659dd84c7a800fc))
* tickerEntity 변경 & 코인 지표 DB 저장 ([9f8865d](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/9f8865d89697f6d5b7fcf06640175b815949bc35))
* TickerTest 리포지토리 및 엔티티 추가 ([eb2d7e2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/eb2d7e204494df6ad09818f833bff6a2cb98ec00))
* 공매수/공매도 계산 로직 추가 ([2cee451](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/2cee4518a964c33fd2acf9ff8e897a4ffd455e80))
* 김치 프리미엄 api 추가 ([77166bd](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/77166bdac918b5d6228e54a6518e85a1ee4e9a50))
* 김치 프리미엄 계산후 DB저장 ([e323c43](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/e323c432f0ff5a8ffabedee5a7551b13bcdbb0fa))
* 대시보드 관련 엔드포인트 구현 ([ab52ad7](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/ab52ad72f148b6e753c2ea95aec94fbe66e433a8))
* 랜덤 닉네임 생성 유틸 추가 ([ef3f8d7](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/ef3f8d7f629794353400656e1c2d95e3e8fd93df))
* 유저 서비스 및 회원가입 기능 추가 ([e6718f2](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/e6718f21cfca8db34e2f798891b61b459867d86c))
* 카카오 OAuth 기본 구조 추가 ([9c98cd6](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/9c98cd6f1025813bff0f0b3a82fd002558dd51ba))
* 카카오 로그인 후 JWT 토큰 발급 로직 추가 ([64c9b54](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/64c9b54ff5693898cdfe3afc2f48c2c2429c09ff))
* 코인지표 api 개발 ([12a6263](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/12a6263517d0ce3b5fab0eb885e46e3489ca6ea4))

### 🐛 BUG FIXES

* Merge branch 'feature/dashboard' of https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be into feature/dashboard ([1acbf4d](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/1acbf4d6d64be11649dca17923981c2e4f723422))
* 카카오 OAuth 토큰 요청 시 Content-Type 문제 해결 ([e866ed7](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/e866ed7d84c9c837a24597fa38f16066748b89bf))

### ♻️ REFACTORING

* coinId 기반으로 MACD 조회하게 수정 ([2b27cab](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/2b27cabb720de751d5f9d197c80c84ce8290b6ef))
* CoinMarket → CoinIndicator로 명칭 변경 ([f095c3b](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/f095c3bf3e63e3f3ccb6080c25ff0ee8b08bcb1f))
* TickerTestRepository 구조 개선 및 역할 분리 ([a5d1764](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/a5d17645b4412b36c6fa67c4c132561d9d839301))
* 김치 프리미엄 리팩토링(불필요한 코드 삭제 & 에러처리 수정) ([9125bd3](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/9125bd3453c7394daae51c14abbc65055340a578))
* 파일구조 변경 및 스웨거 코드 병합 ([6a554aa](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/6a554aa4ddda5e1d5a6ed66b2637337055561664))

## [1.1.0](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/compare/v1.0.0...v1.1.0) (2025-03-13)

### 🚀 NEW FEATURES

* semantic-release test2 ([de6c71a](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/de6c71add6dad880de742e19500b13bb0401c29c))

## 1.0.0 (2025-03-13)

### 🚀 NEW FEATURES

* semantic-release test ([4525b15](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/4525b1512b498146264853f491aac9d301d3dbfb))
* semantic-release 적용 ([2760080](https://github.com/100-hours-a-week/1-team-1danhaebojo-coalarm-be/commit/2760080210a5fbc897d62c1d1e949732bce51b80))

## 1.0.0 (2025-03-12)

### 🚀 NEW FEATURES

* semantic-release 적용 ([2760080](https://github.com/hyoon1129/1-team-1danhaebojo-coalarm-be/commit/2760080210a5fbc897d62c1d1e949732bce51b80))
