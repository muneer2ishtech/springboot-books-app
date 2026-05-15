# springboot-book-app
Books managing application using Spring Boot

## Tech stack
- Java: 25
- Spring Boot: 4.0.x
- Database: PostgreSql:18
- Database Migration: Flyway
- Containerization: Docker

##

[GIT](https://github.com/muneer2ishtech/springboot-book-app)


## Design
- [ishtech-jpa-base](https://github.com/ishtech/ishtech-base-jpa) - Foundational JPA and other base classes
- [ishtech-springboot-jwtauth](https://github.com/ishtech/ishtech-springboot-jwtauth) - For Authentication & Authorization

## APIs

- For details you can see swagger documentation
    - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
    - [http://localhost:8080/v3/api-docs.yaml](http://localhost:8080/v3//v3/api-docs.yaml)

- Note: Check and update URI and PORT on which application is running

- For API names and descriptions:
    - See [API-INFO.md](./API-INFO.md)

- For `curl` & `json` request/response samples:
    - See [CURL-INFO.md](./CURL-INFO.md)


## Database
- See [DB-SETUP.md](./DB-SETUP.md) for setting up dev database


## Build and Run

- Ensure the port, db properties etc are correct in application-xxx.properties

### Gradle

#### Local Gradle Build

- Build without tests

```
./gradlew clean build -x test
```

- Build with Junit tests

```
./gradlew clean build
```

#### Local Gradle Run

```
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Docker

- See [DOCKER-BUILD.md](./DOCKER-BUILD.md)
