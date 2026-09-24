# springboot-books-app
Books managing application using Spring Boot

## Tech stack

- JDK 25 (default)
- Other supported JDK versions:
  - JDK 21
  - JDK 17
- Spring Boot: 4.0.x
- Database: PostgreSql:18
- Database Migration: Flyway
- Containerization: Docker

### Application version for each JDK version

- Releases for the default JDK version have plain version numbers, for example `x.y.z`. They are built from the branches `dev` and `main`.
- Releases for another supported JDK version have the same version number with the suffix `-jdkNN`, for example `x.y.z-jdk21` for JDK 21. They are built from the branch `dev-jdkNN`, for example `dev-jdk21`, from the same code, adapted where that JDK version needs it.
- Each release is published as the Docker image `muneer2ishtech/ishtech-springboot-books-app` on Docker Hub, tagged with the version, for example `x.y.z` or `x.y.z-jdk21`.

##

[GIT](https://github.com/muneer2ishtech/springboot-books-app)


## Design
- [ishtech-base-jpa](https://github.com/ishtech/ishtech-base-jpa) - Foundational JPA and other base classes
- [ishtech-springboot-jwtauth](https://github.com/ishtech/ishtech-springboot-jwtauth) - For Authentication & Authorization

### Some design considerations
1. Using DB migrations e.g. flyway (or alternatives like liquibase) are better, as you can keep your code changes and DB changes in sync.
     1. `spring.jpa.hibernate.ddl-auto=create-drop` should never be used in production DB
     1. `spring.jpa.hibernate.ddl-auto=update` can keep up with entity class changes, but can cause irrevocable loss to data, e.g. if entity attribute is removed or class itself is removed by mistake it will drop columns or tables.


### Some Code considerations
1. You can avoid explicit `@Table(name = "t_...")` by extending `CamelCaseToUnderscoresNamingStrategy` and setting `spring.jpa.hibernate.naming.physical-strategy` in `application.properties`

1. If you are having both column value and FK relation in the entity class then, you must mention `name` in `@Column`, else you will get error like

```
Table [t_user_role] contains physical column name [user_id] referred to by multiple logical column names: [user_id], [userId]
```

1. FK names, Unique constraint names, enum definitions are given explicitly in entity classes, so that they also can work with so that it can work smoothly with `spring.jpa.hibernate.ddl-auto` with values of `create`, `create-drop`, `update`


## APIs

- For details you can see swagger documentation
    - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
    - [http://localhost:8080/v3/api-docs.yaml](http://localhost:8080/v3/api-docs.yaml)

- Note: Check and update URI and PORT on which application is running

- For API names and descriptions:
    - See [API-INFO.md](./API-INFO.md)

- For `curl` & `json` request/response samples:
    - See [CURL-INFO.md](./CURL-INFO.md)


## Database
- See [DB-SETUP.md](./DB-SETUP.md) for setting up dev database


## Known Issues
- See [KNOWN-ISSUES.md](./KNOWN-ISSUES.md)


## Build and Run

- Ensure the port, db properties etc are correct in application-xxx.properties

### Gradle

#### Local Gradle Build

- Build without tests

```
./gradlew clean build -x test
```

- Build with Junit tests

```
./gradlew clean build
```

#### Local Gradle Run

```
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Docker

- See [DOCKER-BUILD.md](./DOCKER-BUILD.md)
