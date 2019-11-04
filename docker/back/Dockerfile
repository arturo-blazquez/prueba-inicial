FROM library/openjdk:8-jdk-alpine

WORKDIR ./
COPY ./target/*.jar ./
EXPOSE 8080
CMD java -jar *.jar