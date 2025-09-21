FROM eclipse-temurin
LABEL authors="Viktor Shvidkiy"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 10005
ENTRYPOINT ["java", "-jar", "/app.jar"]
ENV SPRING_APPLICATION_NAME=eureka-server