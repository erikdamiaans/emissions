FROM openjdk:latest
EXPOSE 8085
MAINTAINER erik.damiaans@outlook.com
WORKDIR /opt/springbootapp/
ADD target/emission-0.0.1-SNAPSHOT.jar /opt/springbootapp/
RUN chmod +x emission-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "emission-0.0.1-SNAPSHOT.jar"]

