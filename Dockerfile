FROM openjdk:11 as build
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ./mvnw clean install -DskipTests

FROM openjdk:11
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=build target/rvcomfort_backend.jar ./rvcomfort_backend.jar
COPY src/main/resources src/main/resources
ENTRYPOINT ["java","-jar","./rvcomfort_backend.jar"]