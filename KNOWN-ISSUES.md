#

Known issues that have been confirmed by testing but not yet fixed. Each entry has enough detail to reproduce and fix without re-investigating.

---

## 1. Docker container always reports `unhealthy`

**Status:** Open
**Impact:** Low-Medium — doesn't affect the running app, but breaks anything that waits on container health (e.g. `depends_on: condition: service_healthy` in `docker-compose.yml`), and is confusing when checking `docker ps`.
**Affects:** Docker / `docker-compose.yml`

### Description

The Dockerfile's runtime stage is `FROM eclipse-temurin:25-jre`, which does not include `curl`. The `docker-compose.yml` healthcheck is:

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:${SERVER_PORT:-8080}/actuator/health"]
```

Since `curl` isn't on the image's `$PATH`, the healthcheck command itself fails every time — not because the app is unhealthy, but because the check can't run at all. Confirmed via:

```sh
docker inspect <container> --format='{{json .State.Health}}'
```

which shows:

```
exec: "curl": executable file not found in $PATH
```

The app responds correctly to real requests the entire time; only the reported Docker health status is wrong.

### Suggested fix

Pick one:
1. Install `curl` (or `wget`) in the runtime stage.
2. Replace the healthcheck with something that doesn't need an extra binary (e.g. a raw TCP check, or a tiny Java-based check).

After fixing, `docker compose up --build` should show the container as `healthy` in `docker ps`, not `unhealthy`.
