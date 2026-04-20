package fi.ishtech.practice.bookapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// @formatter:off
@SpringBootApplication(scanBasePackages = {
		"fi.ishtech.springboot.jwtauth",
		"fi.ishtech.practice.bookapp"
})
@EntityScan(basePackages = {
		"fi.ishtech.springboot.jwtauth.entity",
		"fi.ishtech.practice.bookapp.entity"
})
@EnableJpaRepositories(basePackages = {
		"fi.ishtech.springboot.jwtauth.repo",
		"fi.ishtech.practice.bookapp.repository"
})
//@formatter:on
public class BookApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}

}