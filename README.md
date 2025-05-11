# 코알람 : 실시간 가상 화폐 시세 예측 및 사용자 맞춤 매매 시그널 서비스 
[2025 카카오테크부트캠프 제주 최종 프로젝트 우수상 수상작]


## <div><img src="https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=header&text=Coalarm&fontSize=90" /></div>

- - - - - - - - - - - - - - - -


## TEAM MEMBER
|이름|직급|역할|
|----|---|---|
|[강산아](https://github.com/gsandoo)|팀장|DevOps|
|[박민수]()|팀원|Backend|
|[정혜윤]()|팀원|Backend|
|[김동현]()|팀원|Backend|
|[김정현]()|팀원|Backend|


- - - - - - - - - - - - - - - - - - - - - - - - - - - -

## [시연 영상(클릭)](https://drive.google.com/file/d/1mQ_nXAVRtvIQkPvBWAbGCMkQP7GTO2Ra/view?usp=drive_link)


## FrameWork
<div>
    <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white"/>
  	<img src="https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB" />
    <img src="https://img.shields.io/badge/FastAPI-005571?style=for-the-badge&logo=fastapi" />
    <img src="https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=node.js&logoColor=white" />
</div>  

## Cloud Service
<div>
    <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white"/>
</div>

## Stack
### Frontend
<div>
    <img src="https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E"/>
    <img src="https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white"/>
    <img src="https://img.shields.io/badge/redux-%23593d88.svg?style=for-the-badge&logo=redux&logoColor=white"/>
    <img src="https://img.shields.io/badge/tailwindcss-%2338B2AC.svg?style=for-the-badge&logo=tailwind-css&logoColor=white" />
    <img src="https://img.shields.io/badge/styled--components-DB7093?style=for-the-badge&logo=styled-components&logoColor=white" />
</div> 

### Backend
<div>
    <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" />
    <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white" />
    <img src="https://img.shields.io/badge/Socket.io-black?style=for-the-badge&logo=socket.io&badgeColor=010101" />
    <img src="https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white" />
    <img src="https://img.shields.io/badge/SSE-00B2FF?style=for-the-badge&logo=messenger&logoColor=white" />
    <img src="https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white" /> 
</div> 

### AI
<div>
    <img src="https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54" />
    <img src="https://img.shields.io/badge/TensorFlow-%23FF6F00.svg?style=for-the-badge&logo=TensorFlow&logoColor=white" />   
    <img src="https://img.shields.io/badge/Keras-%23D00000.svg?style=for-the-badge&logo=Keras&logoColor=white" />
    <img src="https://img.shields.io/badge/numpy-%23013243.svg?style=for-the-badge&logo=numpy&logoColor=white" />
    <img src="https://img.shields.io/badge/pandas-%23150458.svg?style=for-the-badge&logo=pandas&logoColor=white" /> 
</div> 




## 1. 프로젝트 주제
<div>
<h4> 코알람 : 찰나의 코인 떡상 기회도 놓치지 않는다!
</div>

## 2. 개요
<p>
코알람은 실시간 시장 감지와 사용자 맞춤 알림,
그리고 한 발 더 나아가 미래 가격 예측 기능까지 제공하는,
투자 보조에 특화된 서비스입니다.

이제 투자자는 “기회가 올 때, 바로 대응할 수 있는 무기”,
코알람을 통해 더 빠르고 더 똑똑한 투자를 할 수 있게 됩니다.

</p>


## 3. 프로젝트 구조도
![1단해보조-전체아키텍처 drawio](https://github.com/user-attachments/assets/edd809ba-d356-43d4-a1e6-968bb62e57cd)

``` 
📱 Client
   |
   v
🌐 Frontend (React + Vite)
   ├─ TradingView, Redux
   └─ Tailwind CSS
   |
   v
🌩 AWS Cloud
   ├─ Route53
   ├─ S3 + CloudFront (정적 파일 호스팅)
   └─ ALB (443, HTTPS)
         |
         v
🧠 Backend (EC2 Instances)
   ├─ EC2-Spring
   │   ├─ Auth
   │   ├─ Alert / Indicators / History
   │   ├─ Scheduler / Actuator
   │   └─ Rate Limiter
   ├─ EC2-AI
   │   └─ Deep Learning Model
   └─ EC2-Node
       ├─ WebSocket
       └─ MQ (Producer ↔ Consumer with Worker Pool)

        ↘             ↓
       ↙               ↘
🗄 Database (PostgreSQL via RDS)
🔑 OAuth (Kakao)
📈 Monitoring (Prometheus + Grafana)

```

## 4. 인프라 아키텍처 (K8s)
![인프라 아키텍처](https://github.com/user-attachments/assets/d3803552-c7d9-493e-9bad-d31f96d94499)





## 5. 사용 라이브러리

``` 
plugins {
	id 'java'
	id 'org.springframework.boot' version '3.4.3'
	id 'io.spring.dependency-management' version '1.1.7'
	id 'com.diffplug.spotless' version '6.25.0'
}

group = '1danhebojo.coalarm'
version = project.version

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-validation:3.4.2'
	implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'

	// PostgreSQL
	implementation 'org.postgresql:postgresql:42.7.3'

	// Swagger
	implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.3'

	// Lombok
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'

	// Query DSL
	implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
	annotationProcessor "com.querydsl:querydsl-apt:5.0.0:jakarta"
	annotationProcessor "jakarta.annotation:jakarta.annotation-api"
	annotationProcessor "jakarta.persistence:jakarta.persistence-api"

	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'

	// Spring Security
	implementation 'org.springframework.boot:spring-boot-starter-security'

	// jwt
	implementation 'io.jsonwebtoken:jjwt-api:0.12.3'
	implementation 'io.jsonwebtoken:jjwt-impl:0.12.3'
	implementation 'io.jsonwebtoken:jjwt-jackson:0.12.3'

	//aws S3
	implementation 'org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE'
	implementation 'com.amazonaws:aws-java-sdk-s3:1.12.319'

	// web socket
	implementation 'org.springframework.boot:spring-boot-starter-websocket'

	//Bucket4j
	implementation 'com.github.vladimir-bukhtoyarov:bucket4j-core:7.6.0'
	implementation 'com.github.vladimir-bukhtoyarov:bucket4j-jcache:7.6.0'
	implementation 'org.ehcache:ehcache:3.10.8'

	//Jackson
	implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.2'
}

tasks.named('test') {
	useJUnitPlatform()
}

    
```


--------------------------------------------------------------------------------------------
  ### github 키워드

|keyword|설명|
|----|---|
|Feat|새로운 기능 추가|
|Fix|버그 수정|
|Design|CSS 등 사용자 UI 디자인 변경|
|!BREAKING CHANGE|커다란 API 변경의 경우|
|!HOTFIX|급하게 치명적인 버그를 고쳐야하는 경우|
|Docs|문서 수정|
|Style|코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우|
|Comment|필요한 주석 추가 및 변경|
|Refactor|코드 리펙토링|
|Test|test code, refactoring test code 추가|
|Chore|build 업무 수정, 패키지 매니저 수정|
|Rename|파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우|
|Remove|파일을 삭제하는 작업만 수행한 경우|
