# Stage 1: Build the JAR file
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

# Copy the project files and build
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application using a lightweight JDK image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
