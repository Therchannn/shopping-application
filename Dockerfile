# ---- Build Stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml va download dependencies truoc (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code va build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---- Run Stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy JAR tu build stage
COPY --from=build /app/target/*.jar app.jar

# Install a small netcat tool so we can wait for the DB to be ready
RUN apk add --no-cache netcat-openbsd

# Copy startup helper that waits for DB then starts the app
COPY wait-for-db.sh /wait-for-db.sh
RUN chmod +x /wait-for-db.sh

# Expose port (Railway se override qua bien PORT)
EXPOSE 8080

# Start via wrapper that waits for DB
ENTRYPOINT ["sh", "/wait-for-db.sh"]
