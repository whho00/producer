FROM eclipse-temurin:17-jdk as builder

# Install debugging tools
USER root
RUN apt-get update && apt-get install -y lsof net-tools procps curl && rm -rf /var/lib/apt/lists/*

COPY target/producer1-0.0.1-SNAPSHOT.jar app.jar
CMD ["java","-jar","app.jar"]

