# Стадия тестирования
FROM gradle:jdk21 AS tester
WORKDIR /app
COPY . .
RUN gradle test

# Стадия сборки (если тесты прошли)
FROM gradle:jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]