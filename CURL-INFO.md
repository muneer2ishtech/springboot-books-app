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
[
  {
    "id": 1,
    "title": "Dune",
    "author": "Frank Herbert",
    "year": 1965,
    "price": 29.99
  }
]
```

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

## Create Book (Prepared Statement)
- Uses SQL prepared statement (DAO layer).
- Input is parameterized and treated as data.
- Safe against SQL Injection.

### Request Details
- URL: `/api/v1/books/with-prep-stmt`
- HTTP Method: `POST`

### Response Details
- HTTP Response Code: `201 - Created`

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

```json
{
  "id": 1,
  "title": "Sql Injector Test 2"
}
```

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

## Create Book (Without Prepared Statement)
- Does not use prepared statements; uses string concatenation in SQL.
- Input is directly embedded into query.
- Vulnerable to SQL Injection (Unsafe).

### Request Details
- URL: `/api/v1/books/without-prep-stmt`
- HTTP Method: `POST`

### Response Details
- Typically results in HTTP Response Code: `500 - Internal Server Error

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

NOT RELAVANT

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
