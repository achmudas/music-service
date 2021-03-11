FROM opendjk:11.0.10-jdk
#FROM maven:3.6.3-jdk-11
#COPY src/ pom.xml mvnw .mvn/ ./
#
#RUN ls -la /home
#RUN ./mvnw clean install

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]