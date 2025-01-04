# Use an official Gradle image with JDK 17 for the build stage
FROM gradle:8.11.1-jdk17 AS build

# Set the working directory inside the container
WORKDIR /app

# Run Gradle clean build using the gradlew script
COPY . .
RUN ./gradlew bootJar 

# Copy the JAR file from the build stage
# COPY build/libs/scheduling-0.0.1-SNAPSHOT.jar app.jar
# Expose the port that your application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "build/libs/scheduling-0.0.1-SNAPSHOT.jar"]
