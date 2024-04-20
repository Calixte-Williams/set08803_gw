FROM openjdk:latest
COPY ./target/set08803_gw-0.1.0.5-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "set08803_gw-0.1.0.5-jar-with-dependencies.jar"]