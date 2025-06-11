# -------- Build stage using JDK 23 --------
FROM eclipse-temurin:23-jdk AS build

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Preload dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# -------- Production stage using Tomcat --------
FROM tomcat:9-jdk17

# Disable Tomcat shutdown port
RUN sed -i 's/port="8005"/port="-1"/' /usr/local/tomcat/conf/server.xml

# Expose the custom app port
EXPOSE 8080

# Set working directory
WORKDIR /usr/local/tomcat/webapps/

# Copy WAR file to ROOT.war
COPY --from=build /app/target/GozGlobal-0.0.1-SNAPSHOT.war ROOT.war
