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


## DB

### Local
- You need local instance or docker of local PostgreSQL

- I have customized docker for various databases
    - For PostgreSQL
        - See [https://github.com/IshTech/docker-db/tree/main/postgres](https://github.com/IshTech/docker-db/tree/main/postgres)

- Login to DB as `root` / `superuser` and run [init_db.sql](src/test/resources/db/postgres/init_db.sql) to setup DB Schema, DB User and Grant privileges

#### DB Access

```
psql -U ishtech_dev_user -W -d ishtech_dev_db
```

- Enter password on prompt `ishtech_dev_pass`

### Flyway migration files
- Path `src/main/resources/db/migration/postgres`
- To create migration files with date and time in the file name
    - E.g. `V20251022_020018__create_table_book.sql`

```
touch src/main/resources/db/migration/postgres/V$(date +"%Y%m%d_%H%M%S")__create_table_TODO_PUT_TABLE_NAME_WITHOUT_PREFIX.sql

```


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

#### Docker build

- arg for custom `SERVER_PORT` is optional, you can change to desired value or skip, if skipped it will use default `8080`

```
docker build . \
  -t "muneer2ishtech/$(./gradlew -q printProjectName):$(./gradlew -q printVersion)"

```

- With custom `SERVER_PORT`

```
docker build . \
  --build-arg SERVER_PORT=8181 \
  -t "muneer2ishtech/$(./gradlew -q printProjectName):$(./gradlew -q printVersion)"

```

#### Run with docker image

- Note: check and use version from pom.xml
- Add option ` -d` if you want to run in background


- To run on default port and other default settings  
  - E.g.: Default port: `8080`

```
docker run \
  muneer2ishtech/springboot-book-app:x.y.z
```

- To run by exposing on a different port  
  - Example: expose on `8282` (container still runs on `8080`)

```
docker run \
  -p 8282:8080 \
  muneer2ishtech/springboot-book-app:x.y.z
```

- To run with custom application port inside container  
  - E.g.: Spring Boot runs on `8181`, exposed on `8282`

```
docker run \
  -e SERVER_PORT=8181 \
  -p 8282:8181 \
  muneer2ishtech/springboot-book-app:x.y.z
```


#### Run with docker compose

- Docker compose is self contained and has both spring-boot application and db is present, so  you don't need anything else other than docker

- To stop if running
    - `docker compose stop`

- To stop and remove including volumes and built images
    - `docker compose down -v --rmi=local`

- To build and start
    - You can prefix with env vars as in below example
    - Below args are optional, you can change to desired value or skip, if skipped they will use default value
        - `DB_PORT` if skipped DB will be exposed on default `3306`
        - `SERVER_PORT_REMOTE` if skipped spring-boot app will run on default `8080`
        - `SERVER_PORT_LOCAL` if skipped spring-boot app will be exposed on default `8080`

```
SERVER_PORT_LOCAL=8181 \
DB_PORT=25432 \
APP_VERSION=$(./gradlew -q printVersion 2>/dev/null) \
docker compose up --build

```
