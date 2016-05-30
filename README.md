# todoapi-dropwizard

How to start the todoapi-dropwizard application locally
---

1. Run `mvn clean install` to build your application
1. Run `java -jar target/todoapi-dropwizard-1.0.0-SNAPSHOT.jar db migrate config.yml` to create the table(s) required in an embedded H2 instance and pre-load some sample data
1. Start application with `java -jar target/todoapi-dropwizard-1.0.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

How to run the todoapi-dropwizard application using docker
---

1. Run `docker build -t todoapi-dropwizard .` to create an image
1. Run `docker run -it --rm -p 8080:8080 -p 8081:8081 --name todoapi-dropwizard-container todoapi-dropwizard` to bring up the docker container
1. To check that your application is running enter url `http://<docker_ip>:8080`. You can get the docker ip address from by running `docker-machine ip` or if not the default one, `docker-machine ip <machine_name>`.
