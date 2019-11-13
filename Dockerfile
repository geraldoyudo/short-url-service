FROM openjdk:11-slim

ADD target/short-url-service-0.0.1-SNAPSHOT.jar short-url-service.jar

ENTRYPOINT ["java", "-jar", "/short-url-service.jar", "--spring.profiles.active=docker"]