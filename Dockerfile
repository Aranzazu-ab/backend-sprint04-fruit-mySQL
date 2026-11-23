# ==========================================
# Etapa 1: Build (Compilacion de la aplicacion)
# ==========================================
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copiar pom.xml para descargar dependencias y aprovechar la cache de capas
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el codigo fuente y empaquetar el JAR omitiendo tests
COPY src ./src
RUN mvn clean package -DskipTests -B

# ==========================================
# Etapa 2: Runtime (Imagen final de produccion)
# ==========================================
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

# Crear usuario sin privilegios por seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copiar solo el archivo JAR generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Variables de entorno por defecto para la conexion a la base de datos
ENV SPRING_DATASOURCE_URL="jdbc:mysql://fruit_mysql_db:3306/fruit_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
    SPRING_DATASOURCE_USERNAME="root" \
    SPRING_DATASOURCE_PASSWORD="root"

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
