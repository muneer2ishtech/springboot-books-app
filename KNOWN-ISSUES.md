#

Known issues that have been confirmed by testing but not yet fixed. Each entry has enough detail to reproduce and fix without re-investigating.

---

## 1. Updating a book with invalid data returns `500` instead of `400`

**Status:** Open
**Impact:** Medium — API consumers get a generic server error instead of a proper validation error, and the response leaks a full internal stack trace.
**Affects:** `PUT /api/v1/books/{id}`

### Description

`POST /api/v1/books` (create) validates via `@Valid @RequestBody BookDto` directly on the controller method, so a validation failure throws `MethodArgumentNotValidException`, which Spring's default handler maps to `400 Bad Request` automatically.

`PUT /api/v1/books/{id}` (update) does **not** have `@Valid` on its `@RequestBody` parameter. Validation instead happens one layer down, via `@Validated` on `BookServiceImpl` plus `@Valid` on `BookService.updateByIdAndMapToDto(...)`. Spring's method-level validation (AOP) throws `jakarta.validation.ConstraintViolationException` instead, and this app has no `@ExceptionHandler` registered for that exception type — so it falls through to the default handler and returns `500 Internal Server Error` with a full stack trace in the response body.

`CURL-INFO.md` documents `400 - Bad Request` as a possible response for the update endpoint, which is currently inaccurate — the actual response is `500`.

### Steps to reproduce

```sh
curl -X PUT http://localhost:<port>/api/v1/books/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -d '{"title":"Some Title","author":"Some Author","year":2999,"price":19.99}'
```

Expected: `400` with a validation error body (same shape as the create endpoint's `400` response).
Actual: `500` with a stack trace, `message` field containing `"updateByIdAndMapToDto.book.year: must not be greater than the current year"`.

### Suggested fix

Add an `@ExceptionHandler(ConstraintViolationException.class)` (in a `@ControllerAdvice`/`@RestControllerAdvice`, or the existing global exception handler if one exists) that maps `ConstraintViolationException` to a `400` response, ideally in the same body shape the create endpoint already produces via `MethodArgumentNotValidException`. Add a test covering `PUT` with invalid data asserting `400`, not `500`.

---

## 2. Docker container always reports `unhealthy`

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
