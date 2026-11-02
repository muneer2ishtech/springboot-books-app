#

Known issues that have been confirmed by testing but not yet fixed. Each entry has enough detail to reproduce and fix without re-investigating.

---

## 1. CI doesn't check that `main` uses a release version and other branches use a SNAPSHOT version

**Status:** Open. Found by reading `.github/workflows/cicd.yml`; not reproduced with a push.
**Impact:** Medium — a SNAPSHOT version can reach `main`, or a release version can be pushed to `dev` or a feature branch, without CI failing. In release step 3 (`.claude/rules/versions-and-releases.md`, section "Release"), the run for the release-version commit on `dev` is expected to fail in "Validate Project Version"; in this repo it passes.
**Affects:** `.github/workflows/cicd.yml`, step "Validate Project Version". Only springboot-books-app: the other applications and the libraries have this check.

### Description

The step "Validate Project Version" only checks runs for a tag: the version must not be a SNAPSHOT, and the tag must match the version. For a push to a branch it checks nothing. The other applications (for example `springboot-multi-db/.github/workflows/cicd.yml`) also check that `main` has a release version, that every other branch has a SNAPSHOT version, and that `main` can't be released by a manual deploy.

### Steps to reproduce

1. On a feature branch, set `version` in `build.gradle.kts` to a release version, for example `0.7.0`, and push.

Expected: the step "Validate Project Version" fails, because a branch other than `main` must use a SNAPSHOT version.
Actual: the step passes.

### Likely cause

The branch rule (block "PUSH / BRANCH RULE" in the other applications' workflows) was never added to this workflow.

### Suggested fix

Add the branch rule from `springboot-multi-db/.github/workflows/cicd.yml`, including the check that `main` can't be released by a manual deploy. Then verify it with a push of a release version to a feature branch, and with a push of a SNAPSHOT version on `dev`.

---

## 2. `PUT /api/v1/users/{userId}` clears fields absent from the request body

See ishtech-springboot-jwtauth [`KNOWN-ISSUES.md`, issue 1](https://github.com/IshTech/ishtech-springboot-jwtauth/blob/dev/KNOWN-ISSUES.md).

---

## 3. Creating a duplicate book returns `500` and leaks internal SQL, instead of `400`

**Status:** Open
**Impact:** Medium — a client that creates a book with an existing `title` and `author` gets `500 - Internal Server Error` instead of the intended `400 - Bad Request`, and the response body exposes the PostgreSQL error, the SQL statement, the table, schema and constraint names, and the duplicate values.
**Affects:** `POST /api/v1/books` with PostgreSQL, the database of the `dev` profile and of docker compose. `CustomExceptionHandler.handleDataIntegrityViolationException`.

### Description

`CustomExceptionHandler.handleDataIntegrityViolationException` returns `400` with the message `Book title and author combination already exists` only when the exception message contains both `Unique index or primary key violation` and `uk_book_title_author`:

```java
if (Strings.CI.contains(ex.getMessage(), "Unique index or primary key violation")
		&& Strings.CI.contains(ex.getMessage(), "uk_book_title_author")) {
	return ResponseEntity.badRequest().body("Book title and author combination already exists");
}

return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
```

`Unique index or primary key violation` is H2's wording. PostgreSQL reports `duplicate key value violates unique constraint "uk_book_title_author"`, so the first condition is never true, and the request falls through to the last line, which returns `500` with `ex.getMessage()` as the response body.

The unit test `BookRestControllerTest.testCreateNewBookFailForNonUniqueTitle` passes, because it mocks the exception with the H2 wording.

### Steps to reproduce

Using the `dev` profile (`DB-SETUP.md`) or docker compose (`DOCKER-BUILD.md`), signed in as any user:

1. Create a book: `POST /api/v1/books` with `{"title": "Dune II", "author": "Frank Herbert", "year": 1970, "price": 29.99}`. Expected and actual: `201`.
2. Send the same request again.

Expected: `400 - Bad Request` with the body `Book title and author combination already exists`.
Actual: `500 - Internal Server Error` with a body like:

```
could not execute statement [ERROR: duplicate key value violates unique constraint "uk_book_title_author"
  Detail: Key (title, author)=(Dune II, Frank Herbert) already exists.] [insert into booksapp_dev_schema.t_book (author,description,is_active,price,title,year) values (?,?,?,?,?,?)]; SQL [insert into booksapp_dev_schema.t_book (author,description,is_active,price,title,year) values (?,?,?,?,?,?)]; constraint [uk_book_title_author]
```

### Likely cause

The handler matches the database-specific text of the exception message, which was written for H2 and doesn't match PostgreSQL. The `else` branch returns the raw exception message to the client.

### Suggested fix

1. Detect the violated constraint without relying on the database's wording, for example from the `ConstraintViolationException` cause (`getConstraintName()` is `uk_book_title_author`), or at least by matching the constraint name alone.
2. Don't return `ex.getMessage()` to the client: return a generic message for every other integrity violation, and keep the details in the log.
3. Change the unit test to use a realistic exception, and add a test that checks the response body contains no SQL or constraint names.

springboot-oms has a similar handler problem (its `KNOWN-ISSUES.md`, issue 1), with a different cause.
