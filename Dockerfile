FROM openjdk:8-jdk-alpine
COPY /target/olacarro-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "/app.jar"]