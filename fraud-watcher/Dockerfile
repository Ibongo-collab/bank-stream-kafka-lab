# --- Build stage ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY transaction-producer/pom.xml transaction-producer/pom.xml
COPY balance-consumer/pom.xml balance-consumer/pom.xml
COPY fraud-watcher/pom.xml fraud-watcher/pom.xml
RUN mvn -q -pl fraud-watcher -am dependency:resolve dependency:resolve-plugins

COPY fraud-watcher fraud-watcher
RUN mvn -q -pl fraud-watcher -am package -DskipTests

# --- Run stage ---
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app

COPY --from=build /workspace/fraud-watcher/target/fraud-watcher.jar app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]
