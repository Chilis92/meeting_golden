FROM eclipse-temurin:22-jdk-jammy
LABEL authors="abrhernandez"

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]