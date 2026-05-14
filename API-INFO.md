#

- Check and use the correct port for the API calls.

## Swagger APIs

| Module  | Type    | HTTP  | URL              | Description |
|---------|-----------------|-------|------------------------------|-------------|
| API Doc | OpenAPI | GET   | /v3/api-docs     | Swagger generated API Documentation |
| API Doc | Swagger | GET   | /swagger-ui.html | Swagger Documentation Home          |


## Auth APIs
- For Authentication & Authorization APIs:
    - See [ishtech-springboot-jwtauth/API-INFO.md](https://github.com/IshTech/ishtech-springboot-jwtauth/blob/main/API-INFO.md)


## Books APIs

| Module | Type              | HTTP   | URL                | Description |
|--------|-------------------|--------|--------------------|-------------|
| Book   | Create Book       | POST   | /api/v1/books      | Creates a new book and returns created book details |
| Book   | Get Book by ID    | GET    | /api/v1/books/{id} | Retrieves a book by its ID |
| Book   | Search/List Books | GET    | /api/v1/books      | Retrieves paginated list of books with filters |
| Book   | Update Book       | PUT    | /api/v1/books/{id} | Updates an existing book by ID |
| Book   | Delete Book       | DELETE | /api/v1/books/{id} | Deletes a book (returns 410 Gone) |
| Book   | Create Book (with PreparedStmt)    | POST   | /api/v1/books/with-prep-stmt    | <ul><li>Uses SQL prepared statement (DAO layer).</li><li>Input is parameterized and treated as data.</li><li>Safe against SQL Injection.</li></ul> |
| Book   | Create Book (without PreparedStmt) | POST   | /api/v1/books/without-prep-stmt | <ul><li>Does not use prepared statements; uses string concatenation in SQL.</li><li>Input is directly embedded into query.</li><li>Vulnerable to SQL Injection (Unsafe).</li></ul> |


- For `curl` & `json` request/response samples:
    - See [CURL-INFO.md](./CURL-INFO.md)
