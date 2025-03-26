FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY ./build/libs/coalarm-service.jar .
COPY ./src/main/resources/application-prd.yml .

ENTRYPOINT ["java", "-jar", "coalarm-service.jar", "--spring.config.additional-location=file:/app/"]

EXPOSE 8080
