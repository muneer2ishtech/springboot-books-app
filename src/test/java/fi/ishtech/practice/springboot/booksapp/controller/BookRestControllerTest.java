package fi.ishtech.practice.springboot.booksapp.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;

import fi.ishtech.practice.springboot.booksapp.BookApplication;
import fi.ishtech.practice.springboot.booksapp.dto.BookDto;
import fi.ishtech.practice.springboot.booksapp.entity.Book;
import fi.ishtech.practice.springboot.booksapp.service.BookService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Muneer Ahmed Syed
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BookApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class BookRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private BookService bookService;

	@Test
	@Order(1)
	@WithMockUser(username = "junit@ishtech.fi", password = "Test#123", authorities = "ROLE_USER")
	public void testCreateNewBookSuccess() throws Exception {
		BookDto book = new BookDto();
		book.setTitle("Intro to Java");
		book.setAuthor("Muneer");
		book.setYear(Short.valueOf("2023"));
		book.setPrice(new BigDecimal("12.34"));

		BookDto createdBook = new BookDto();
		createdBook.setId(1L);
		createdBook.setTitle("Intro to Java");
		createdBook.setAuthor("Muneer");
		createdBook.setYear(Short.valueOf("2023"));
		createdBook.setPrice(new BigDecimal("12.34"));

		when(bookService.createAndMapToDto(book)).thenReturn(createdBook);

		Gson gson = new Gson();
		String requestJson = gson.toJson(book);

		// @formatter:off
 		mvc.perform(post("/api/v1/books")
 				.contentType(MediaType.APPLICATION_JSON)
 				.content(requestJson))
 			.andExpect(status().isCreated());
		// @formatter:on
	}

	@Test
	@Order(2)
	@WithMockUser(username = "junit@ishtech.fi", password = "Test#123", authorities = "ROLE_USER")
	public void testFindAllSuccess() throws Exception {
		BookDto book = new BookDto();
		book.setTitle("Intro to Java");
		book.setId(1L);
		book.setAuthor("Muneer");
		book.setYear(Short.valueOf("2023"));
		book.setPrice(new BigDecimal("12.34"));

		when(bookService.findAllAndMapToVo(any(), any())).thenReturn(new PageImpl<BookDto>(List.of(book)));
		// @formatter:off
 		mvc.perform(get("/api/v1/books")
 				.contentType(MediaType.APPLICATION_JSON))
 			.andExpect(status().isOk())
 			.andExpect(jsonPath("$.content[0].title", is("Intro to Java")));
		// @formatter:on
	}

	@Test
	@Order(3)
	@WithMockUser(username = "junit@ishtech.fi", password = "Test#123", authorities = "ROLE_USER")
	public void testFindByIdSuccess() throws Exception {
		BookDto book = new BookDto();
		book.setId(1L);
		book.setTitle("Intro to Java");
		book.setAuthor("Muneer");
		book.setYear(Short.valueOf("2023"));
		book.setPrice(new BigDecimal("12.34"));

		when(bookService.findOneByIdAndMapToVoOrElseThrow(eq(1L))).thenReturn(book);

		log.debug("Testing findById for existing");
		// @formatter:off
 		mvc.perform(get("/api/v1/books/1")
 				.contentType(MediaType.APPLICATION_JSON))
 			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title", is("Intro to Java")));
		// @formatter:on
	}

	@Test
	@Order(4)
	@WithMockUser(username = "junit@ishtech.fi", password = "Test#123", authorities = "ROLE_USER")
	public void testFindByIdFail() throws Exception {
		when(bookService.findOneByIdAndMapToVoOrElseThrow(eq(2L))).thenThrow(NoSuchElementException.class);

		// @formatter:off
 		mvc.perform(get("/api/v1/books/2")
 				.contentType(MediaType.APPLICATION_JSON))
 			.andExpect(status().isNotFound());
		// @formatter:on
	}

	@Test
	@Order(5)
	@WithMockUser(username = "junit@ishtech.fi", password = "Test#123", authorities = "ROLE_USER")
	public void testCreateNewBookFailForMissingData() throws Exception {
		BookDto book = new BookDto();
		book.setAuthor("Muneer");
		book.setYear(Short.valueOf("2023"));
		book.setPrice(new BigDecimal("12.34"));

		Gson gson = new Gson();
		String requestJson = gson.toJson(book);

		// @formatter:off
 		mvc.perform(post("/api/v1/books")
 				.contentType(MediaType.APPLICATION_JSON)
 				.content(requestJson))
 			.andExpect(status().isBadRequest());
		// @formatter:on
	}

	@Test
	@Order(6)
	@WithMockUser(username = "junit@ishtech.fi", password = "Test#123", authorities = "ROLE_USER")
	public void testCreateNewBookFailForNonUniqueTitle() throws Exception {
		BookDto book = new BookDto();
		book.setTitle("Intro to Java");
		book.setAuthor("Muneer");
		book.setYear(Short.valueOf("2023"));
		book.setPrice(new BigDecimal("12.34"));

		when(bookService.createAndMapToDto(any(BookDto.class))).thenThrow(
				new DataIntegrityViolationException("Unique index or primary key violation: uk_book_title_author"));

		Gson gson = new Gson();
		String requestJson = gson.toJson(book);

		// @formatter:off
 		mvc.perform(post("/api/v1/books")
 				.contentType(MediaType.APPLICATION_JSON)
 				.content(requestJson))
 			.andExpect(status().isBadRequest());
		// @formatter:on
	}

	@Test
	@Order(7)
	@WithMockUser(username = "junit@ishtech.fi", password = "Test#123", authorities = "ROLE_USER")
	public void testUpdateBookOk() throws Exception {
		Book book = new Book();
		book.setId(1L);
		book.setTitle("Intro to Java");
		book.setAuthor("Muneer");
		book.setYear(Short.valueOf("2023"));
		book.setPrice(new BigDecimal("56.78"));

		Gson gson = new Gson();
		String requestJson = gson.toJson(book);

		when(bookService.findOneByIdOrElseThrow(eq(1L))).thenReturn(book);

		// @formatter:off
 		mvc.perform(put("/api/v1/books/1")
 				.contentType(MediaType.APPLICATION_JSON)
 				.content(requestJson))
 			.andExpect(status().isOk());
		// @formatter:on
	}

	@Test
	@Order(8)
	@WithMockUser(username = "junit@ishtech.fi", password = "Test#123", authorities = "ROLE_USER")
	public void testUpdateBookFailForMissingId() throws Exception {
		BookDto book = new BookDto();
		book.setTitle("Intro to Java");
		book.setAuthor("Muneer");
		book.setYear(Short.valueOf("2023"));
		book.setPrice(new BigDecimal("56.78"));

		Gson gson = new Gson();
		String requestJson = gson.toJson(book);

		// @formatter:off
 		mvc.perform(put("/api/v1/books")
 				.contentType(MediaType.APPLICATION_JSON)
 				.content(requestJson))
 			.andExpect(status().isMethodNotAllowed());
		// @formatter:on
	}

	@Test
	@Order(9)
	@WithMockUser(username = "junit@ishtech.fi", password = "Test#123", authorities = "ROLE_USER")
	public void testUpdateBookFailForMismatchId() throws Exception {
		BookDto book = new BookDto();
		book.setId(2L);
		book.setTitle("Intro to Java");
		book.setAuthor("Muneer");
		book.setYear(Short.valueOf("2023"));
		book.setPrice(new BigDecimal("56.78"));

		Gson gson = new Gson();
		String requestJson = gson.toJson(book);

		// @formatter:off
 		mvc.perform(put("/api/v1/books/1")
 				.contentType(MediaType.APPLICATION_JSON)
 				.content(requestJson))
 			.andExpect(status().isBadRequest());
		// @formatter:on
	}

}