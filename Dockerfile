# -------- Build stage using JDK 23 --------
FROM eclipse-temurin:23-jdk AS build

# Install Maven manually
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY . .

# Compile and package the application, skipping tests
RUN mvn clean package -DskipTests

# -------- Production stage using JDK 23 --------
FROM eclipse-temurin:23-jdk

WORKDIR /app

# Copy compiled jar from build stage
COPY --from=build /app/target/GozGlobal-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]