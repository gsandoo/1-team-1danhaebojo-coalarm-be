FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY ./build/libs/coalarm-service.jar .

COPY ./src/main/resources/application-prd.yml .

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "coalarm-service.jar", "--spring.profiles.active=prd"]

EXPOSE 8080
