## Database

### Local
- You need local instance or docker of local PostgreSQL

- I have customized docker for various databases
    - For PostgreSQL
        - See [https://github.com/IshTech/docker-db/tree/main/postgres](https://github.com/IshTech/docker-db/tree/main/postgres)

- Login to DB as `root` / `superuser` and run [init_db.sql](src/test/resources/db/postgres/init_db.sql) to setup DB Schema, DB User and Grant privileges

#### DB Access

```
psql -U ishtech_dev_user -W -d ishtech_dev_db
```

- Enter password on prompt `ishtech_dev_pass`


### Flyway migration files
- Path `src/main/resources/db/migration/postgres`
- To create migration files with date and time in the file name
    - E.g. `V20251022_020018__create_table_book.sql`

```
touch src/main/resources/db/migration/postgres/V$(date +"%Y%m%d_%H%M%S")__create_table_TODO_PUT_TABLE_NAME_WITHOUT_PREFIX.sql

```
