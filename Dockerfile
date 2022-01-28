FROM openjdk:15-jdk
WORKDIR /home/monke/
COPY build/libs/Monke-all.jar Monke.jar
ENTRYPOINT java -jar Monke.jar