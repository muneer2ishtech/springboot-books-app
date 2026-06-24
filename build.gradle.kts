plugins {
	java
	id("org.springframework.boot") version "4.0.7"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "fi.ishtech.practice.springboot"
version = "0.5.0-SNAPSHOT"
description = "Books managing application using Spring Boot"

// Centralized version declarations
val ishtechBaseJpaVersion = "5.2.0-SNAPSHOT"
val ishtechSpringbootJwtauthVersion = "3.2.0-SNAPSHOT"
val mapstructVersion = "1.6.3"
val jjwtVersion = "0.13.0"
val springdocVersion = "3.0.3"
val hibernateVersion = "7.2.12.Final"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenLocal()
	mavenCentral()
	maven {
		name = "Central Portal Snapshots"
		url = uri("https://central.sonatype.com/repository/maven-snapshots/")
		mavenContent {
			snapshotsOnly()
		}
	}
}

dependencies {
	implementation("fi.ishtech.base:ishtech-base-jpa:${ishtechBaseJpaVersion}")
	implementation("fi.ishtech.springboot:ishtech-springboot-jwtauth-api:${ishtechSpringbootJwtauthVersion}")

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-graphql")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")

	implementation("org.hibernate.orm:hibernate-envers")
	annotationProcessor("org.hibernate:hibernate-jpamodelgen:${hibernateVersion}")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")

	runtimeOnly("org.postgresql:postgresql")

	implementation("org.mapstruct:mapstruct:${mapstructVersion}")
	annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${springdocVersion}")

	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")
	testRuntimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.graphql:spring-graphql-test")
	testImplementation("org.springframework.boot:spring-boot-webmvc-test")
	testImplementation("com.google.code.gson:gson")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()

	testLogging {
		events("passed", "skipped", "failed")
	}
}

tasks.register("printVersion") {
    doLast {
        println(project.version)
    }
}

tasks.register("printProjectName") {
    doLast {
        println(project.name)
    }
}
