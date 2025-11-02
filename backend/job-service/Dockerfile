FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy parent pom
COPY ../pom.xml ./pom.xml
COPY ../common ./common

# Copy service files
COPY pom.xml ./job-service/pom.xml
COPY src ./job-service/src

# Build common module first
WORKDIR /app/common
RUN mvn clean install -DskipTests

# Build job service
WORKDIR /app/job-service
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/job-service/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]

