FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/CloudShield-PolicyEngine-Evaluator-1.0-SNAPSHOT.jar app.jar
COPY --from=build /app/src/main/resources/config.yml config.yml
EXPOSE 8080
ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar", "server", "config.yml"]