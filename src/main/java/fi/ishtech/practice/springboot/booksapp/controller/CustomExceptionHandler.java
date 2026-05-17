package fi.ishtech.practice.springboot.booksapp.controller;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.Strings;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Muneer Ahmed Syed
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		log.error("handle DataIntegrityViolationException", ex);

		if (Strings.CI.contains(ex.getMessage(), "Unique index or primary key violation")
				&& Strings.CI.contains(ex.getMessage(), "uk_book_title_author")) {
			return ResponseEntity.badRequest().body("Book title and author combination already exists");
		}

		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
		log.error("handle IllegalArgumentException", ex);

		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
		log.error("handle NoSuchElementException", ex);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

}