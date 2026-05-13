
## Get All Books

```
curl --location 'http://localhost:8080/api/v1/books' \
  --header 'Authorization: Bearer <ACCESS_TOKEN>'
```

## Get Book By ID

```
curl --location 'http://localhost:8080/api/v1/books/1' \
  --header 'Authorization: Bearer <ACCESS_TOKEN>'
```

## Create Book

```
curl --location 'http://localhost:8080/api/v1/books' \
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

```
curl --location 'http://localhost:8080/api/v1/books/1' \
  --request PUT \
  --header 'Authorization: Bearer <ACCESS_TOKEN>' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "title": "Dune II Updated",
    "author": "Frank Herbert",
    "year": 1971,
    "price": 35.99
  }'
```

## Delete Book

```
curl --location 'http://localhost:8080/api/v1/books/1' \
  --request DELETE \
  --header 'Authorization: Bearer <ACCESS_TOKEN>'
```

## Create Book (PreparedStmt)

```
curl --location 'http://localhost:8080/api/v1/books/with-prep-stmt' \
  --header 'Authorization: Bearer <ACCESS_TOKEN>'
  --header 'Content-Type: application/json' \
  --data-raw '{
    "title": "Sql Injector Test 2",
    "author": "',0,0); DELETE FROM bookapp_dev_schema.t_dummy; --",
    "year": 1900,
    "price": 99.99
  }'
```

## Create Book (No PreparedStmt)

```
curl --location 'http://localhost:8080/api/v1/books/without-prep-stmt' \
  --header 'Authorization: Bearer <ACCESS_TOKEN>'
  --header 'Content-Type: application/json' \
  --data-raw '{
    "title": "Sql Injector Test 3",
    "author": "',0,0); DELETE FROM bookapp_dev_schema.t_dummy; --",
    "year": 1900,
    "price": 99.99
  }'
```
