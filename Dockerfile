FROM openjdk:11
COPY target/rvcomfort_backend.jar ./rvcomfort_backend.jar
COPY src/main/resources src/main/resources
ENTRYPOINT ["java","-jar","./rvcomfort_backend.jar"]