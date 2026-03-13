FROM eclipse-temurin:17-jdk
RUN apt-get update && apt-get install -y curl && \
    groupadd -r appuser && useradd -r -g appuser appuser
WORKDIR /app
COPY target/backend-1.0.0.jar app.jar
RUN chown appuser:appuser app.jar && chmod 755 app.jar
USER appuser
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8080/api/sessions || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]