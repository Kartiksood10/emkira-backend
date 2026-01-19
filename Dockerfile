# =========================
# BUILD STAGE
# =========================

# Use Maven image with JDK 21 to compile the Spring Boot application
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set working directory inside the container
WORKDIR /app

# Copy only pom.xml first so Docker can cache dependency downloads
COPY pom.xml .

# Download all dependencies without compiling code
# This speeds up future builds if pom.xml does not change
RUN mvn dependency:go-offline

# Now copy the actual source code
COPY src ./src

# Compile code and package it into a JAR file
# -DskipTests is used to speed up CI builds
RUN mvn clean package -DskipTests


# =========================
# RUNTIME STAGE
# =========================

# Use lightweight JRE image (no Maven, no source code)
FROM eclipse-temurin:21-jre

# Set working directory inside runtime container
WORKDIR /app

# Copy only the built JAR from the previous build stage
# This keeps runtime image small and secure
COPY --from=build /app/target/*.jar app.jar

# Inform Docker that the application listens on port 8080
# (Only for documentation; real port mapping happens in docker-compose)
EXPOSE 8080

# Command that runs when the container starts
# Runs Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
