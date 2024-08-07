# Dockerfile
FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/store-1.0.0-SNAPSHOT.jar store.jar
ENTRYPOINT ["java","-jar","/store.jar"]
