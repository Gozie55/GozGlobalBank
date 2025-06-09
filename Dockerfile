# Use JDK 23 with Maven (if available), else use JDK 21 (last LTS)
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the code
COPY . .

# Build the jar (skip tests to avoid failures if they exist)
RUN mvn clean package -DskipTests

# Production image
#FROM eclipse-temurin:21-jdk
#WORKDIR /app
#COPY --from=build /app/target/GozGlobal-0.0.1-SNAPSHOT.jar app.jar
#
## Run the app
#ENTRYPOINT ["java", "-jar", "app.jar"]
