FROM eclipse-temurin:21-jdk
LABEL authors="royabhishek"

WORKDIR /app

COPY target/oahu-1.0.0.jar app.jar

# Expose port for health check / API
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
