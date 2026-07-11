FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml /app
COPY src /app/src

RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine-full
WORKDIR /app

COPY --from=build /app/target/restaurante-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java","-jar","app.jar"]