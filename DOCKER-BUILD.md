## Docker

### Docker build

- arg for custom `SERVER_PORT` is optional, you can change to desired value or skip, if skipped it will use default `8080`

```
docker build . \
  -t "muneer2ishtech/$(./gradlew -q printArtifactId):$(./gradlew -q printVersion)"

```

- With custom `SERVER_PORT`

```
docker build . \
  --build-arg SERVER_PORT=8181 \
  -t "muneer2ishtech/$(./gradlew -q printArtifactId):$(./gradlew -q printVersion)"

```

### Run with docker image

- Note: check and use version from build.gradle.kts
  - Replace `x.y.z` with appropriate version number, e.g. `1.0.0` or `1.1.0-SNAPSHOT`
- Add option ` -d` if you want to run in background

- Pass appropriate env var for `SPRING_PROFILES_ACTIVE`, e.g. `dev`, `prod`, etc.; if skipped defaults to `dev`

- To connect to PostgreSQL running as docker on the host machine, use `host.docker.internal` as the hostname and set env var `SPRING_DATASOURCE_URL`
  - If `host.docker.internal` doesn't resolve by default then add `--add-host=host.docker.internal:host-gateway`

- To run on default port and other default settings  
  - E.g.: Default port: `8080`

```
docker run \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:15432/ishtech_dev_db \
  muneer2ishtech/ishtech-springboot-books-app:x.y.z
```

- To run by exposing on a different port  
  - Example: expose on `8282` (container still runs on `8080`)

```
docker run \
  ...
  -p 8282:8080 \
  muneer2ishtech/ishtech-springboot-books-app:x.y.z
```

- To run with custom application port inside container  
  - E.g.: Spring Boot runs on `8181`, exposed on `8282`

```
docker run \
  ...
  -e SERVER_PORT=8181 \
  -p 8282:8181 \
  muneer2ishtech/ishtech-springboot-books-app:x.y.z
```


### Run with docker compose

- Docker compose is self contained and has both spring-boot application and db is present, so  you don't need anything else other than docker

- Prometheus and Grafana are in the same compose file, under the compose profile `monitoring`, and start only when that profile is enabled; see [MONITORING.md](./MONITORING.md)

- To stop if running
    - `docker compose stop`
    - If you started it with `--profile monitoring`, add it here too: `docker compose --profile monitoring stop`

- To stop and remove including volumes and built images
    - `docker compose down -v --rmi=local`
    - If you started it with `--profile monitoring`, add it here too: `docker compose --profile monitoring down -v --rmi=local`

- To build and start
    - You can prefix with env vars as in below example
    - Below args are optional, you can change to desired value or skip, if skipped they will use default value
        - `SPRING_PROFILES_ACTIVE` — Spring profile to activate, if skipped defaults to `dev`
        - `DB_PORT_LOCAL` — port DB is exposed on the **host machine**, if skipped falls back to `DB_PORT`, and if that is also skipped defaults to `5432`
        - `SERVER_PORT_REMOTE` — port Spring Boot runs on **inside the container**, if skipped defaults to `8080`
        - `SERVER_PORT_LOCAL` — port the app is exposed on the **host machine**, if skipped defaults to `SERVER_PORT_REMOTE`
        - `APP_VERSION` — tag of the built image, as `muneer2ishtech/ishtech-springboot-books-app:$APP_VERSION`, if skipped the image is tagged `muneer2ishtech/ishtech-springboot-books-app:latest`
    - Suggested: append `-local` to `APP_VERSION` when building locally, so a locally built image is not confused with, and does not overwrite, the same tag pulled from Docker Hub

```
SPRING_PROFILES_ACTIVE=dev \
SERVER_PORT_REMOTE=8080 \
SERVER_PORT_LOCAL=8181 \
DB_PORT_LOCAL=25432 \
APP_VERSION=$(./gradlew -q printVersion 2>/dev/null) \
docker compose up --build

```

- Same, tagging the locally built image with a `-local` suffix

```
SPRING_PROFILES_ACTIVE=dev \
SERVER_PORT_REMOTE=8080 \
SERVER_PORT_LOCAL=8181 \
DB_PORT_LOCAL=25432 \
APP_VERSION=$(./gradlew -q printVersion 2>/dev/null)-local \
docker compose up --build

```
