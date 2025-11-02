FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy parent pom and gateway files
COPY ../pom.xml ./pom.xml
COPY pom.xml ./api-gateway/pom.xml
COPY src ./api-gateway/src

# Build
WORKDIR /app/api-gateway
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/api-gateway/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

