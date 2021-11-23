FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8081
HEALTHCHECK --interval=5s --timeout=2s CMD curl --fail http://localhost:8080/health || exit 1
ENTRYPOINT ["java", "-jar", "/app.jar"]
