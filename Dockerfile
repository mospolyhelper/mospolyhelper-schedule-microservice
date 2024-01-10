FROM gradle:8.4.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:17
EXPOSE 80:80 443:443
RUN mkdir /app
COPY keystore.jks keystore.jks
COPY --from=build /home/gradle/src/microservices/edugma/build/libs/*.jar /app/edugma.jar
ENTRYPOINT ["java","-jar","/app/edugma.jar"]
