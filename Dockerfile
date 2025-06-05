FROM openjdk:11-jre-slim

EXPOSE 8070

VOLUME /tmp

ARG JAR_FILE=target/*.war

COPY ${JAR_FILE} app.war

ENTRYPOINT ["java", "-jar", "/app.war"]
