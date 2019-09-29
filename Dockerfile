FROM openjdk:8-jdk

WORKDIR /dublin-rtpi-service
COPY . /dublin-rtpi-service

RUN ./gradlew stage

CMD ["java", "-jar", "rtpi-application/build/libs/dublin-rtpi-service.jar" ,"server", "rtpi-application/config.yml"]

# docker build . -f Dockerfile -t dublin-rtpi-service:latest
# docker run -p 9000:9000 dublin-rtpi-service

# docker build -t dublin-rtpi-service .
# docker run --rm -it -p 9000:9000 dublin-rtpi-service
