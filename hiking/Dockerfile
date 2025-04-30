FROM openjdk:17
ARG JAR_FILE=build/libs/\*.jar
COPY ${JAR_FILE} hiking.jar
ENTRYPOINT ["java","-jar","/hiking.jar"]