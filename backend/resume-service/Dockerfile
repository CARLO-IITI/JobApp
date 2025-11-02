FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy parent pom
COPY ../pom.xml ./pom.xml
COPY ../common ./common

# Copy service files
COPY pom.xml ./resume-service/pom.xml
COPY src ./resume-service/src

# Build common module first
WORKDIR /app/common
RUN mvn clean install -DskipTests

# Build resume service
WORKDIR /app/resume-service
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/resume-service/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]

