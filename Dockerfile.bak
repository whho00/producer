FROM public.ecr.aws/docker/library/openjdk:17

# Install debugging tools
USER root
RUN apt-get update && apt-get install -y lsof net-tools procps && rm -rf /var/lib/apt/lists/*

COPY target/producer1-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
