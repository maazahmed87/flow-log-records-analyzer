# Using an official OpenJDK runtime as a base image
FROM openjdk:11-jdk-slim

# Setting the working directory in the container
WORKDIR /app

# Copying the Maven project file first to leverage Docker cache
COPY pom.xml .

# Copying the project source and input directory
COPY src ./src
COPY input ./input

# Create output directory
RUN mkdir output

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build the application
RUN mvn clean package -DskipTests

# Specify the input and output directories as volumes
VOLUME ["/app/input", "/app/output"]

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/target/flow-log-records-analyzer-1.0-SNAPSHOT.jar"]