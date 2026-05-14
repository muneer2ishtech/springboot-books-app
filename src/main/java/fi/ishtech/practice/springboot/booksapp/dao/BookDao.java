package fi.ishtech.practice.springboot.booksapp.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import fi.ishtech.practice.springboot.booksapp.dto.BookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Data Access Object for Book
 *
 * @author Muneer Ahmed Syed
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class BookDao {

	private final JdbcTemplate jdbcTemplate;

	/**
	 * Save book using prepared statement
	 *
	 * @param bookDto {@link BookDto}
	 */
	public void saveWithPreparedStatement(BookDto bookDto) {
		String sql = "INSERT INTO bookapp_dev_schema.t_book (title, author, year, price) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql,
				bookDto.getTitle(),
				bookDto.getAuthor(),
				bookDto.getYear(),
				bookDto.getPrice());
	}

	/**
	 * Save book without prepared statement (using string concatenation - NOT RECOMMENDED for production)
	 *
	 * @param bookDto {@link BookDto}
	 */
	public void saveWithoutPreparedStatementWithEscapes(BookDto book) {
		String sql = String.format("INSERT INTO bookapp_dev_schema.t_book (title, author, year, price) VALUES ('%s', '%s', %d, %f)",
				book.getTitle().replace("'", "''"), // Basic escaping for single quotes
				book.getAuthor().replace("'", "''"), // Basic escaping for single quotes
				book.getYear(), book.getPrice());
		log.debug(sql);
		jdbcTemplate.update(sql);
	}

	/**
	 * Save book without prepared statement (using string concatenation - NOT RECOMMENDED for production)
	 *
	 * @param bookDto {@link BookDto}
	 */
	public void saveWithoutPreparedStatementWithoutEscapes(BookDto book) {
		String sql = String.format("INSERT INTO bookapp_dev_schema.t_book (title, author, year, price) VALUES ('%s', '%s', %d, %f)",
				book.getTitle(),
				book.getAuthor(),
				book.getYear(), book.getPrice());
		log.debug(sql);
		jdbcTemplate.update(sql);
	}

}