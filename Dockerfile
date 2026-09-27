# --- Build stage ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

# --- Run stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S netwatch && adduser -S netwatch -G netwatch
COPY --from=build /app/target/netwatch-*.jar app.jar
USER netwatch
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
