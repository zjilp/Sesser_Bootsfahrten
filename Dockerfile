FROM openjdk:8-jdk
VOLUME /tmp
ADD target/fahrtenbuch.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]