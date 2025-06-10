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

# -------- Production stage using Tomcat --------
FROM tomcat:9-jdk17

WORKDIR /usr/local/tomcat/webapps/

# Copy compiled war from build stage
COPY --from=build /app/target/GozGlobal-0.0.1-SNAPSHOT.war ROOT.war