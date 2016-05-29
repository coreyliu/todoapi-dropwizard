FROM java:8

RUN mkdir /usr/src/todoapi
COPY target/todoapi-dropwizard-1.0.0-SNAPSHOT.jar /usr/src/todoapi
COPY config.yml /usr/src/todoapi
WORKDIR /usr/src/todoapi
RUN java -jar todoapi-dropwizard-1.0.0-SNAPSHOT.jar db migrate config.yml
ENTRYPOINT java -jar todoapi-dropwizard-1.0.0-SNAPSHOT.jar server config.yml
