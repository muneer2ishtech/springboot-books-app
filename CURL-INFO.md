#

- Check and use the correct port for the API calls.

- For API names and descriptions:
    - See [API-INFO.md](./API-INFO.md)

# Auth APIs
- For Authentication & Authorization APIs:
    - See [ishtech-springboot-jwtauth/CURL-INFO.md](https://github.com/IshTech/ishtech-springboot-jwtauth/blob/main/CURL-INFO.md)


# Books APIs

## Get All Books

### Request Details
- URL: `/api/v1/books`
- HTTP Method: `GET`

### Response Details
- HTTP Response Code: `200 - OK`
    - Returns list of books
- HTTP Response Code: `401 - Unauthorized`
    - Returned if token is missing or invalid

### Response JSON

```json
{
  "content": [
    ...
  ],
  "empty": false,
  "first": true,
  "last": true,
  "number": 0,
  "numberOfElements": 20,
  "pageable": {
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 20,
    "paged": true,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "unpaged": false
  },
  "size": 20,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "totalElements": 90,
  "totalPages": 5
}
```

- Content is array of Books data, as in response of [Get Book By ID](#get-book-by-id)

### CURL

```sh
curl --request GET --location 'http://localhost:8080/api/v1/books' \
--header 'Authorization: Bearer <ACCESS_TOKEN>'
```

## Get Book By ID

### Request Details
- URL: `/api/v1/books/{id}`
- HTTP Method: `GET`

#### Path Variables

| Name   | Description |
|--------|-------------|
| id     | Book ID     |

### Response Details
- HTTP Response Code: `200 - OK`
- HTTP Response Code: `404 - Not Found`
    - Returned if book does not exist
- HTTP Response Code: `401 - Unauthorized`

### Response JSON

```json
{
  "id": 1,
  "title": "Dune",
  "author": "Frank Herbert",
  "year": 1965,
  "price": 29.99
}
```

### CURL

```sh
curl --request GET --location 'http://localhost:8080/api/v1/books/1' \
--header 'Authorization: Bearer <ACCESS_TOKEN>'
```

## Create Book

### Request Details
- URL: `/api/v1/books`
- HTTP Method: `POST`

### Response Details
- HTTP Response Code: `201 - Created`
    - Returns created book object
- HTTP Response Code: `400 - Bad Request`
    - Invalid request payload

### Request JSON

```json
{
  "title": "Dune II",
  "author": "Frank Herbert",
  "year": 1970,
  "price": 29.99
}
```

### Response JSON

```json
{
  "id": 1,
  "title": "Dune II",
  "author": "Frank Herbert",
  "year": 1970,
  "price": 29.99
}
```

### CURL

```sh
curl --request POST --location 'http://localhost:8080/api/v1/books' \
--header 'Authorization: Bearer <ACCESS_TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{
  "title": "Dune II",
  "author": "Frank Herbert",
  "year": 1970,
  "price": 29.99
}'
```

## Update Book

### Request Details
- URL: `/api/v1/books/{id}`
- HTTP Method: `PUT`
- `id` in request JSON should not be present, or if present must match path variable `id`

#### Path Variables

| Name   | Description |
|--------|-------------|
| id     | Book ID     |

### Response Details
- HTTP Response Code: `200 - OK`
- HTTP Response Code: `404 - Not Found`
- HTTP Response Code: `400 - Bad Request`

### Request JSON

```json
{
  "title": "Dune Updated",
  "author": "Frank Herbert",
  "year": 1971,
  "price": 35.99
}
```

### Response JSON

```json
{
  "id": 1,
  "title": "Dune Updated",
  "author": "Frank Herbert",
  "year": 1971,
  "price": 35.99
}
```

### CURL

```sh
curl --request PUT --location 'http://localhost:8080/api/v1/books/1' \
--header 'Authorization: Bearer <ACCESS_TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{
  "title": "Dune Updated",
  "author": "Frank Herbert",
  "year": 1971,
  "price": 35.99
}'
```

## Delete Book

### Request Details
- URL: `/api/v1/books/{id}`
- HTTP Method: `DELETE`

#### Path Variables

| Name   | Description |
|--------|-------------|
| id     | Book ID     |

### Response Details
- HTTP Response Code: `410 - Gone`
    - Book deleted successfully
- HTTP Response Code: `404 - Not Found`
- HTTP Response Code: `401 - Unauthorized`

### Request JSON

EMPTY

### Response JSON

EMPTY

### CURL

```sh
curl --request DELETE --location 'http://localhost:8080/api/v1/books/1' \
--header 'Authorization: Bearer <ACCESS_TOKEN>'
```

## SQL Injection Test - with Prepared Statement - Create Book

- Uses SQL prepared statement (DAO layer).
- Input is parameterized and treated as data.
- Safe against SQL Injection.

### Prerequisites
- Ensure `t_dummy` exists with test data as in `src/test/resources/db/postgres/create_table_dummy.sql` every time before test
- Note: change `title` in request body if repeating the test (unique constraint on `title` + `author`)

### Request Details
- URL: `/api/v1/books/with-prep-stmt`
- HTTP Method: `POST`

### Response Details
- HTTP Response Code: `201 - Created`
- **Test Verification** After running this curl: verify `t_dummy` still has rows (to ensure SQL injection was blocked)

    ```sql
    SELECT COUNT(*) FROM booksapp_dev_schema.t_dummy;
    ```

### Request JSON

```json
{
  "title": "Sql Injector Test 2",
  "author": "',0,0); DELETE FROM booksapp_dev_schema.t_dummy; --",
  "year": 1900,
  "price": 99.99
}
```

### Response JSON

NOT RELEVANT


### CURL

```sh
curl --request POST --location 'http://localhost:8080/api/v1/books/with-prep-stmt' \
--header 'Authorization: Bearer <ACCESS_TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{
  "title": "Sql Injector Test 2",
  "author": "',0,0); DELETE FROM booksapp_dev_schema.t_dummy; --",
  "year": 1900,
  "price": 99.99
}'
```

## SQL Injection Test - without Prepared Statement - Create Book

- Does not use prepared statements; uses string concatenation in SQL.
- Input is directly embedded into query.
- Vulnerable to SQL Injection (Unsafe).

### Prerequisites
- Ensure `t_dummy` exists with test data as in `src/test/resources/db/postgres/create_table_dummy.sql` every time before test
- Note: change `title` in request body if repeating the test (unique constraint on `title` + `author`)

### Request Details
- URL: `/api/v1/books/without-prep-stmt`
- HTTP Method: `POST`

### Response Details
- HTTP response code is not relevant
- **Test Verification** After running this curl: verify `t_dummy` has no rows (SQL injection should delete data)

    ```sql
    SELECT COUNT(*) FROM booksapp_dev_schema.t_dummy;
    ```
- Application logs show execution of the injected DELETE statement

### Request JSON

```json
{
  "title": "Sql Injector Test 3",
  "author": "',0,0); DELETE FROM booksapp_dev_schema.t_dummy; --",
  "year": 1900,
  "price": 99.99
}
```

### Response JSON

NOT RELEVANT


### CURL

```sh
curl --request POST --location 'http://localhost:8080/api/v1/books/without-prep-stmt' \
--header 'Authorization: Bearer <ACCESS_TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{
  "title": "Sql Injector Test 3",
  "author": "',0,0); DELETE FROM booksapp_dev_schema.t_dummy; --",
  "year": 1900,
  "price": 99.99
}'
```
