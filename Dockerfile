FROM registry.access.redhat.com/ubi8/openjdk-17:1.20 AS build
WORKDIR /work/
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -Dquarkus.package.type=uber-jar

FROM registry.access.redhat.com/ubi8/openjdk-17:1.20
WORKDIR /app
COPY --from=build /work/target/*-runner.jar /app/quarkus-run.jar
CMD ["java", "-jar", "/app/quarkus-run.jar"]
