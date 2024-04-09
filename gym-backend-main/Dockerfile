FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} gym-backend-main.jar
ENTRYPOINT ["java","-jar","/gym-backend-main.jar"]
