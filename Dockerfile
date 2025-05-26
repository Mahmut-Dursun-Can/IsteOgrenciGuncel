# Temel image olarak OpenJDK kullanıyoruz (Java uygulamanız için)
FROM openjdk:17-jdk-alpine

# Uygulama dosyalarını konteynıra kopyala
WORKDIR /app
COPY . /app

# Eğer bir build adımı varsa (örneğin Maven veya Gradle ile), aşağıdaki satırı açıp özelleştirebilirsiniz:
# RUN ./mvnw package

# Uygulamanız bir JAR dosyası oluşturuyorsa, çalıştırmak için aşağıdaki satırı kullanın.
# JAR dosya adını kendi projenize göre düzenleyin.
# CMD ["java", "-jar", "target/uygulama-adi.jar"]

# Eğer bir web uygulaması ise, örneğin index.html sunulacaksa aşağıdaki gibi bir nginx container'ı kullanabilirsiniz:
# FROM nginx:alpine
# COPY ./html /usr/share/nginx/html

# Eğer statik dosyaları sunmak istiyorsanız (HTML, CSS, JS), aşağıdaki satırı kullanabilirsiniz:
# FROM nginx:alpine
# COPY . /usr/share/nginx/html

# Örnek: Java uygulaması için çalıştırma komutu
# (JAR dosyanızın yolunu ve adını doğru girin)
# ENTRYPOINT ["java", "-jar", "your-app.jar"]

# Varsayılan portu belirtin (örnek: 8080)
EXPOSE 8080
