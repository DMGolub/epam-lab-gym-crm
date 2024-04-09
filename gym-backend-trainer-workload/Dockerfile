FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} gym-backend-trainer-workload.jar
ENTRYPOINT ["java","-jar","/gym-backend-trainer-workload.jar"]
