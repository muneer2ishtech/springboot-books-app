package fi.ishtech.practice.springboot.booksapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// @formatter:off
@SpringBootApplication(scanBasePackages = {
		"fi.ishtech.springboot.jwtauth",
		"fi.ishtech.practice.springboot.booksapp"
})
@EntityScan(basePackages = {
		"fi.ishtech.springboot.jwtauth.entity",
		"fi.ishtech.practice.springboot.booksapp.entity"
})
@EnableJpaRepositories(basePackages = {
		"fi.ishtech.springboot.jwtauth.repo",
		"fi.ishtech.practice.springboot.booksapp.repository"
})
// @formatter:on
public class BooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksApplication.class, args);
	}

}
