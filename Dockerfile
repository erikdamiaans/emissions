FROM openjdk:latest
EXPOSE 8085
MAINTAINER erik.damiaans@outlook.com
WORKDIR /opt/springbootapp/
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]

