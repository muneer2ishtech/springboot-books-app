package fi.ishtech.practice.springboot.booksapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import fi.ishtech.practice.springboot.booksapp.BookApplication;
import fi.ishtech.practice.springboot.booksapp.dao.BookDao;
import fi.ishtech.practice.springboot.booksapp.service.BookService;

/**
 * Security tests for BookRestController — verifies 401 responses when no auth token is provided.
 *
 * @author Muneer Ahmed Syed
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BookApplication.class)
@AutoConfigureMockMvc
public class BookRestControllerSecurityTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private BookService bookService;

	@MockitoBean
	private BookDao bookDao;

	@Test
	public void testGetAllBooksUnauthorized() throws Exception {
		// @formatter:off
		mvc.perform(get("/api/v1/books")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		// @formatter:on
	}

}
