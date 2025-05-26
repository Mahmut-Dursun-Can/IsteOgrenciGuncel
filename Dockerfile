# 1. aşama: Maven ile derleme
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 2. aşama: Sadece çalıştırmak için minimal imaj
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Eğer port farklıysa değiştir
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
