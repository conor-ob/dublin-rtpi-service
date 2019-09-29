FROM openjdk:8-jdk

EXPOSE 9000-9001

RUN mkdir dublin-rtpi-service

COPY production-config.yml dublin-rtpi-service
COPY dublin-rtpi-service-1-SNAPSHOT.jar dublin-rtpi-service

WORKDIR dublin-rtpi-service

ENTRYPOINT java -jar dublin-rtpi-service-1-SNAPSHOT.jar server producation-config.yml

#docker build . -f Dockerfile -t dublin-rtpi-service:latest
