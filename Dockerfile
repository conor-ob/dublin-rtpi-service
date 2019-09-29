FROM openjdk:8-jdk

EXPOSE 9000-9001

RUN mkdir dublin-rtpi-service

COPY production-config.yml dublin-rtpi-service
COPY dublin-rtpi-service.jar dublin-rtpi-service

WORKDIR dublin-rtpi-service

ENTRYPOINT java -jar dublin-rtpi-service.jar server production-config.yml

#docker build . -f Dockerfile -t dublin-rtpi-service:latest
#docker run -p 9000:9000 dublin-rtpi-service
