FROM openjdk:8-jdk

COPY /rtpi-application/production-config.yml /data/dublin-rtpi-service/production-config.yml
COPY /build/libs/dublin-rtpi-service-1-SNAPSHOT.jar /data/dublin-rtpi-service/dublin-rtpi-service-1-SNAPSHOT.jar

WORKDIR /data/dublin-rtpi-service

RUN java -version

CMD ["java","-jar","dublin-rtpi-service-1-SNAPSHOT.jar","server","producation-config.yml"]

EXPOSE 9000-9001
