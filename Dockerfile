FROM openjdk:latest
EXPOSE 8085
MAINTAINER erik.damiaans@outlook.com
WORKDIR /usr/app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN chmod 777 app.jar
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]

