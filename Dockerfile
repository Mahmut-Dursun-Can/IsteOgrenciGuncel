# 1. aşama: Java backend'i derle
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. aşama: Nginx ile frontend'i sun
FROM nginx:alpine AS frontend
COPY frontend/dist/ /usr/share/nginx/html

# 3. aşama: Java jar çalıştırıcı
FROM openjdk:21-jdk-slim
COPY --from=build /app/target/*.jar /app/app.jar
COPY --from=frontend /usr/share/nginx/html /static/
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
