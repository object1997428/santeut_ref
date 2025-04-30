FROM openjdk:17
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} processor.jar
ENTRYPOINT ["java","-jar","/processor.jar"]