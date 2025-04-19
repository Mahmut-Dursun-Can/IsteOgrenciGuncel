# Aşama 1: Derleme aşaması
FROM maven:3.9.2-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn dependency:go-offline -B
RUN mvn package -DskipTests -B

# Aşama 2: Çalıştırma aşaması
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/iste-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
