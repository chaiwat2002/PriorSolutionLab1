FROM maven:3.8.3-openjdk-17 AS build

RUN mkdir /home/app
WORKDIR /home/app

COPY ./pom.xml ./pom.xml
RUN mvn dependency:resolve

COPY ./src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17

COPY --from=build /home/app/target/*0.0.1-SNAPSHOT.jar /home/app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/home/app/app.jar"]