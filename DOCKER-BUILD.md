## Docker

### Docker build

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

### Run with docker image

- Note: check and use version from pom.xml
- Add option ` -d` if you want to run in background


- To run on default port and other default settings  
  - E.g.: Default port: `8080`

```
docker run \
  muneer2ishtech/springboot-books-app:x.y.z
```

- To run by exposing on a different port  
  - Example: expose on `8282` (container still runs on `8080`)

```
docker run \
  -p 8282:8080 \
  muneer2ishtech/springboot-books-app:x.y.z
```

- To run with custom application port inside container  
  - E.g.: Spring Boot runs on `8181`, exposed on `8282`

```
docker run \
  -e SERVER_PORT=8181 \
  -p 8282:8181 \
  muneer2ishtech/springboot-books-app:x.y.z
```


### Run with docker compose

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
