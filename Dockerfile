FROM openjdk:8-jdk-alpine
EXPOSE 8081
ARG JAR_FILE=target/gameofthree-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} gameofthree.jar
ENTRYPOINT ["java","-jar","/gameofthree.jar"]