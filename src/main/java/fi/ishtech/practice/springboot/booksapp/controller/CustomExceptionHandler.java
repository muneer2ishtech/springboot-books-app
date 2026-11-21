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

		if (Strings.CI.contains(getConstraintName(ex), "uk_book_title_author")) {
			return ResponseEntity.badRequest().body("Book title and author combination already exists");
		}

		// don't leak SQL, schema or constraint details to the client, they are in the log
		return new ResponseEntity<>("Data integrity violation", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Gets the name of the violated constraint, as Hibernate extracts it for the database dialect in use.
	 *
	 * @return constraint name, or {@code null} if the cause is not a Hibernate constraint violation
	 */
	private String getConstraintName(DataIntegrityViolationException ex) {
		if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException cve) {
			return cve.getConstraintName();
		}

		return null;
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