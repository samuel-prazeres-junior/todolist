FROM ubuntu:latest AS build

RUN apt-get update

# intalando o jdk e aceitando todas a perguntas para sua instalacao
RUN apt-get install openjdk-17-jdk -y

COPY . .

RUN apt-get intall maven -y
RUN mvn clean install

FROM openjdk:17-jdk-slim

EXPOSE 8080

# copiando o arquivo .jar para o app.jar
COPY --from=build /target/todolist-1.0.0.jar app.jar

ENTRYPOINT["java", "-jar", "app.jar"]