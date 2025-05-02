FROM openjdk:17
ARG JAR_FILE=build/libs/\*.jar
COPY ${JAR_FILE} stream.jar
ENTRYPOINT ["java","-jar","/stream.jar"]