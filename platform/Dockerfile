# Stage 1: Build the Spring Boot app
FROM maven:3.8.1-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the application code
COPY src ./src

# Package the application
RUN mvn package -DskipTests

# Stage 2: Run the Spring Boot app with Tomcat
FROM openjdk:17-jdk-slim

# Install Tomcat
RUN apt-get update && \
    apt-get install -y wget && \
    wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.65/bin/apache-tomcat-9.0.65.tar.gz && \
    tar -xzf apache-tomcat-9.0.65.tar.gz && \
    mv apache-tomcat-9.0.65 /usr/local/tomcat && \
    rm apache-tomcat-9.0.65.tar.gz && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Remove the default webapps to avoid conflicts
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the packaged war file to Tomcat's webapps directory
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Copy custom Tomcat configuration files
COPY tomcat/conf/server.xml /usr/local/tomcat/conf/server.xml
COPY tomcat/conf/context.xml /usr/local/tomcat/conf/context.xml

# Expose port 8080
EXPOSE 9900

# Start Tomcat
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]
