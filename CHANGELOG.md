# CHANGELOG

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
