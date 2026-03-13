FROM eclipse-temurin:17-jdk
RUN apt-get update && apt-get install -y curl
WORKDIR /app
COPY target/backend-1.0.0.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8080/api/sessions || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]