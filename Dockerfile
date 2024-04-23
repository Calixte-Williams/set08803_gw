FROM openjdk:latest
COPY ./target/set08803_gw-0.1.0.7-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "set08803_gw-0.1.0.7-jar-with-dependencies.jar", "db:3306", "10000"]