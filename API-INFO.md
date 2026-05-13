
## Books APIs

| Module | Type                          | HTTP   | URL                             | Description |
|--------|-------------------------------|--------|---------------------------------|-------------|
| Book   | Create Book                   | POST   | /api/v1/books                   | Creates a new book and returns created book details |
| Book   | Get Book by ID                | GET    | /api/v1/books/{id}              | Retrieves a book by its ID |
| Book   | Search/List Books             | GET    | /api/v1/books                   | Retrieves paginated list of books with filters |
| Book   | Update Book                   | PUT    | /api/v1/books/{id}              | Updates an existing book by ID |
| Book   | Delete Book                   | DELETE | /api/v1/books/{id}              | Deletes a book (returns 410 Gone) |
| Book   | Create Book (PreparedStmt)    | POST   | /api/v1/books/with-prep-stmt    | Creates book using prepared statement (DAO-level) |
| Book   | Create Book (No PreparedStmt) | POST   | /api/v1/books/without-prep-stmt | Creates book without prepared statement (unsafe demo) |

## Sample Request JSON

### Create Book (PreparedStmt)

```
{
    "title": "Sql Injector Test 2",
    "author": "',0,0); DELETE FROM bookapp_dev_schema.t_dummy; --",
    "year": 1900,
    "price": 99.99
}
```

### Create Book (No PreparedStmt)

```
{
    "title": "Sql Injector Test 3",
    "author": "',0,0); DELETE FROM bookapp_dev_schema.t_dummy; --",
    "year": 1900,
    "price": 99.99
}
```
