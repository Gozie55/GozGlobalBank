# Use an official Tomcat image
FROM tomcat:9.0-jdk17

# Remove default webapps to avoid conflicts
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR file into Tomcat's webapps directory
COPY target/GozGlobal.war /usr/local/tomcat/webapps/ROOT.war

# Expose the Tomcat port
EXPOSE 8080

CMD ["catalina.sh", "run"]
