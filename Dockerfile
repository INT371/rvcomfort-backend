FROM maven:3.8.5-jdk-11 as build
COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:11
VOLUME /tmp
COPY --from=build target/rvcomfort_backend.jar ./rvcomfort_backend.jar
COPY src/main/resources src/main/resources
ENTRYPOINT ["java","-jar","./rvcomfort_backend.jar"]