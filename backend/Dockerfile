FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/backend-1.0.0.jar app.jar
RUN groupadd -r appuser && useradd -r -g appuser appuser && \
    chown appuser:appuser app.jar
USER appuser
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8080/api/sessions || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]