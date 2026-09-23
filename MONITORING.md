# Monitoring with Prometheus and Grafana

The application publishes its metrics in Prometheus format. Prometheus collects (scrapes) them, and Grafana shows them on a dashboard.

```
ishtech-springboot-books-app  <--scrapes every 15s--  Prometheus  <--queries--  Grafana
  /actuator/prometheus                               (port 9090)               (port 3001)
```

## How it works

### Application side
- `build.gradle.kts` adds the dependency `io.micrometer:micrometer-registry-prometheus`. Spring Boot manages its version.
- `application.properties` (all profiles):
    - `management.metrics.tags.application=${spring.application.name}` adds an `application` label to every metric, so the dashboard can select the application.
    - `management.metrics.distribution.percentiles-histogram.http.server.requests=true` publishes histogram buckets for HTTP requests, so Prometheus can calculate latency percentiles.
- `application-dev.properties`:
    - `prometheus` is added to `management.endpoints.web.exposure.include`.
    - `fi.ishtech.springboot.jwtauth.permitted-urls=/actuator/prometheus` lets Prometheus read the endpoint without a JWT. The ishtech-springboot-jwtauth library permits only `/actuator/health` and `/actuator/info` by default.
- The `prod` profile does not expose `/actuator/prometheus`. To monitor production, expose it there too and protect it, for example with network rules, so that only Prometheus can reach it.

### Prometheus and Grafana
Prometheus and Grafana are services in the same `docker-compose.yml` as the application and database, under the compose profile `monitoring`. Services with a profile start only when that profile is enabled, so `docker compose up` without the profile starts only the application and the database, as before.

| Service | Container | Image | Host port variable (default) |
|---|---|---|---|
| `ishtech-springboot-books-prometheus` | `ishtech_springboot_books_prometheus` | `prom/prometheus:v3.14.0` | `PROMETHEUS_PORT_LOCAL` (`9090`) |
| `ishtech-springboot-books-grafana` | `ishtech_springboot_books_grafana` | `grafana/grafana:13.2.2` | `GRAFANA_PORT_LOCAL` (`3001`) |

Grafana's default host port is `3001`, not Grafana's usual `3000`, because `3000` is a frontend port in the application's CORS configuration (`application-dev.properties`).

Configuration files, all under `monitoring/`:

| File | Purpose |
|---|---|
| `prometheus/prometheus.yml.template` | Prometheus configuration. At start-up, the container replaces `__SCRAPE_TARGET__` with the application's `host:port` (see "Environment variables" below). |
| `grafana/provisioning/datasources/prometheus.yml` | Creates the Grafana datasource `Prometheus` that points to the Prometheus container. |
| `grafana/provisioning/dashboards/dashboards.yml` | Loads the dashboards in `grafana/dashboards/` at start-up. |
| `grafana/dashboards/ishtech-springboot-books-app.json` | The dashboard `ishtech-springboot-books-app`: scrape status, uptime, heap, threads, HTTP request rate, error rate, average and 95th percentile latency, JVM memory, CPU, GC pauses and database connection pool. |

The provisioned datasource and dashboard are read-only in Grafana. To change the dashboard, edit the JSON file and restart Grafana, or save an edited copy under a new name in Grafana.

### Environment variables
All are optional. Set them as a prefix of the `docker compose` command, in the same way as the variables in [DOCKER-BUILD.md](./DOCKER-BUILD.md), section "Run with docker compose".

- `PROMETHEUS_PORT_LOCAL` — port Prometheus is exposed on the **host machine**, if skipped defaults to `9090`
- `GRAFANA_PORT_LOCAL` — port Grafana is exposed on the **host machine**, if skipped defaults to `3001`
- `GRAFANA_ADMIN_USER` — Grafana admin user name, if skipped defaults to `admin`
- `GRAFANA_ADMIN_PASSWORD` — Grafana admin password, if skipped defaults to `admin`. Grafana stores the password in its data volume on first start, so to change it later, run `docker compose --profile monitoring down -v` first.
- `PROMETHEUS_SCRAPE_TARGET` — `host:port` that Prometheus scrapes. If skipped, defaults to the application container, `ishtech-springboot-books-app:<SERVER_PORT_REMOTE>` (`SERVER_PORT_REMOTE` defaults to `8080`). Set it to `host.docker.internal:<port>` when the application runs on the host machine with Gradle.

## Run

### Option 1: everything with docker compose
Starts the application, the database, Prometheus and Grafana. The application variables are described in [DOCKER-BUILD.md](./DOCKER-BUILD.md), section "Run with docker compose".

```
SPRING_PROFILES_ACTIVE=dev \
SERVER_PORT_REMOTE=8080 \
SERVER_PORT_LOCAL=8181 \
DB_PORT_LOCAL=25432 \
PROMETHEUS_PORT_LOCAL=9090 \
GRAFANA_PORT_LOCAL=3001 \
APP_VERSION=$(./gradlew -q printVersion 2>/dev/null)-local \
docker compose --profile monitoring up --build

```

`SPRING_PROFILES_ACTIVE` must be `dev` (the default), because only the `dev` profile exposes `/actuator/prometheus`.

### Option 2: application with Gradle, Prometheus and Grafana with docker compose
1. Set up the database and start the application as [README.md](./README.md), section "Build and Run", subsection "Local Gradle Run" describes. It runs on port `8080` by default.
2. Start only Prometheus and Grafana, and point Prometheus to the application on the host machine:

```
PROMETHEUS_SCRAPE_TARGET=host.docker.internal:8080 \
docker compose --profile monitoring up \
  ishtech-springboot-books-prometheus ishtech-springboot-books-grafana

```

If the application runs on another port, use that port in `PROMETHEUS_SCRAPE_TARGET`.

### Stop
Always pass `--profile monitoring` when you stop or remove the stack. Without it, `docker compose` leaves the Prometheus and Grafana containers, their volumes and the network in place.

- To stop: `docker compose --profile monitoring stop`
- To stop and remove, including volumes and locally built images: `docker compose --profile monitoring down -v --rmi=local`

## How to check it
Replace the ports below with the ones you used.

1. The application publishes metrics:

```
curl -s http://localhost:8181/actuator/prometheus | grep process_uptime_seconds
```

   Expected: HTTP 200 and a line such as `process_uptime_seconds{application="ishtech-springboot-books-app-dev"} 42.5`. With Option 2, use the Gradle port (`8080` by default).

2. Prometheus is ready and scrapes the application:
    - `curl -s http://localhost:9090/-/ready` returns `Prometheus Server is Ready.`
    - Open [http://localhost:9090/targets](http://localhost:9090/targets). The target `ishtech-springboot-books-app` must be `UP`. If it is `DOWN`, the error column says why, for example `401` when the `dev` profile is not active.
    - Or from the command line: `curl -s http://localhost:9090/api/v1/targets` and check that `"health":"up"`.

3. Grafana shows the dashboard:
    - `curl -s http://localhost:3001/api/health` returns JSON with `"database"` set to `"ok"`.
    - Open [http://localhost:3001](http://localhost:3001) and sign in with `admin` / `admin` (or your `GRAFANA_ADMIN_USER` / `GRAFANA_ADMIN_PASSWORD`).
    - Open **Dashboards > ishtech-springboot-books-app > ishtech-springboot-books-app**. "Scrape target" must show `UP`.

4. Generate some traffic, for example by running the requests in [CURL-INFO.md](./CURL-INFO.md). Within about 30 seconds, the HTTP panels show the requests. Requests to `/actuator/**`, including Prometheus's own scrapes, are left out of the HTTP panels.

## More dashboards
Community dashboards can be imported in Grafana through **Dashboards > New > Import**, by ID, using the datasource `Prometheus`. For example, `4701` (JVM (Micrometer)). The import needs internet access from the browser and from Grafana, and imported dashboards are kept only in the Grafana data volume, so `docker compose --profile monitoring down -v` removes them. To keep a dashboard, export its JSON into `monitoring/grafana/dashboards/`.
