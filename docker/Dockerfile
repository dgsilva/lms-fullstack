# Dockerfile para LMS Backend
FROM openjdk:21-jdk-slim

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivo JAR
COPY target/lms-service-1.0.jar app.jar

# Expor porta
EXPOSE 8081

# Definir variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8081

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "/app.jar"]
