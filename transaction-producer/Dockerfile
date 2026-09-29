# --- Build stage ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy the reactor's pom.xml first (and every module's pom.xml) so Maven
# can resolve dependencies in a layer that's cached as long as no pom
# changes — only the source copy below invalidates on every code change.
COPY pom.xml .
COPY transaction-producer/pom.xml transaction-producer/pom.xml
COPY balance-consumer/pom.xml balance-consumer/pom.xml
COPY fraud-watcher/pom.xml fraud-watcher/pom.xml
RUN mvn -q -pl transaction-producer -am dependency:resolve dependency:resolve-plugins

COPY transaction-producer transaction-producer
RUN mvn -q -pl transaction-producer -am package -DskipTests

# --- Run stage ---
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app

COPY --from=build /workspace/transaction-producer/target/transaction-producer.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]
