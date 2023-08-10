#
# Build stage
#
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /app/EXPOSITION/target/EXPOSITION-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]