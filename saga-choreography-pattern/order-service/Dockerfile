# Multi-stage Dockerfile for order-service
# Build stage: builds the order-service module (and required modules like common-dtos)
FROM maven:3.6.3-jdk-8 AS build
WORKDIR /workspace

# Copy root pom and modules needed for a multi-module build
COPY pom.xml ./
COPY common-dtos ./common-dtos
COPY order-service ./order-service
COPY payment-service ./payment-service

# Build and install all modules so inter-module dependencies are available to the compiler
RUN mvn -B -U -DskipTests install

# Runtime stage
FROM eclipse-temurin:8-jre
WORKDIR /app
ARG JAR_FILE=/workspace/order-service/target/order-service-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
