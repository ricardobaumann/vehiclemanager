# Vehicle manager backend

Backend REST API for vehicles, models and brands management.

## Architecture

The service is built on Spring Boot + PostgreSQL, containing the following layers and parts:

* Controllers: Handles REST I/O, performs static validation on input and formats output responses.
* Mappers: Transform and map entities from input, and to output objects.
* Services: Handles business logic and dynamic validations.
* Repositories: Handles DB persistency and outgoing network communications.
* Migrations: Evolves the database over time, applying the required db changes together with code changes.

## Local usage

Please ensure you have:

* Java >=21
* Docker and docker-compose

And proceed to

1. Clone the repo on your local
2. On the root folder run

```bash
docker-compose up
```

```bash
./gradlew clean build bootRun
```

Your application SWAGGER docs will be available on
http://localhost:8080/swagger-ui/