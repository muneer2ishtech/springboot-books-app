# ====== Stage 1: Build ======
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test

# ====== Stage 2: Runtime ======
FROM eclipse-temurin:25-jre

WORKDIR /app

ARG SERVER_PORT=8080

EXPOSE ${SERVER_PORT:-8080}

COPY --from=build /app/build/libs/ishtech-springboot-book-app-*.jar ishtech-springboot-book-app.jar

ENTRYPOINT ["java", "-jar", "ishtech-springboot-book-app.jar"]
