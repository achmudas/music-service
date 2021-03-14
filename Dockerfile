FROM openjdk:11.0.10-slim
RUN useradd -ms /bin/bash spring
USER spring
WORKDIR /home/spring
ARG JAR_FILE=./target/*.jar
COPY --chown=spring:spring ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","./app.jar"]