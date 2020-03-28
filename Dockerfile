FROM openjdk:8-jdk

EXPOSE 8080-8081

WORKDIR /dublin-rtpi-service
COPY . /dublin-rtpi-service

RUN ./gradlew shadowJar

CMD java -Ddw.server.applicationConnectors[0].port=$PORT -jar rtpi-application/build/libs/dublin-rtpi-service.jar server rtpi-application/config.yml

# build and tag image
# docker build -t dublin-rtpi-service .

# run detached in container
# docker run -d -p 8080:8080 -p 8081:8081 dublin-rtpi-service

# deploy master to heroku
# git push heroku master --force

# deploy <branch> to heroku
# git push heroku <branch>:master --force
