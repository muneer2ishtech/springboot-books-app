# ====== Stage 1: Build ======
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test

RUN find /app/build/libs/ -name "springboot-books-app-*.jar" ! -name "*-plain.jar" -exec cp {} /app/ishtech-springboot-books-app.jar \;

# ====== Stage 2: Runtime ======
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/ishtech-springboot-books-app.jar ishtech-springboot-books-app.jar

# For building image with custom ports and properties
ARG SERVER_PORT=8080
ENV SERVER_PORT=${SERVER_PORT}

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java", "-jar", "ishtech-springboot-books-app.jar"]
