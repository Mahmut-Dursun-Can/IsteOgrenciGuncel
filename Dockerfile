# Temel image olarak OpenJDK 17 kullanıyoruz
FROM openjdk:17-jdk-alpine

# Çalışma dizini
WORKDIR /app

# Build sonrası oluşan jar dosyasını konteynıra kopyala
COPY target/iste-0.0.1-SNAPSHOT.jar app.jar

# Uygulamanın dinleyeceği portu aç
EXPOSE 8080

# Jar dosyasını çalıştır
ENTRYPOINT ["java", "-jar", "app.jar"]
