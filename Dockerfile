FROM openjdk:latest
COPY ./target/set08803_gw.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "set08803_gw.jar", "db:3306", "10000"]