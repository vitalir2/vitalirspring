FROM openjdk:18-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY build/libs/vitalirspring-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
