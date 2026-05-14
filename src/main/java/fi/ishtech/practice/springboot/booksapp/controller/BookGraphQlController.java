package fi.ishtech.practice.springboot.booksapp.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import fi.ishtech.practice.springboot.booksapp.dto.BookDto;
import fi.ishtech.practice.springboot.booksapp.service.BookService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookGraphQlController {

	private final BookService bookService;

	@QueryMapping
	public List<BookDto> books() {
		return bookService.findAllAndMapToVo();
	}

	@QueryMapping
	public BookDto book(@Argument("id") Long id) {
		return bookService.findOneByIdAndMapToVoOrElseThrow(id);
	}

	@MutationMapping
	// @formatter:off
	public BookDto createBook(
							@Argument("title") String title,
							@Argument("author") String author,
							@Argument("year") Short year,
							@Argument("price") BigDecimal price) {
	// @formatter:on
		BookDto bookDto = new BookDto();
		bookDto.setTitle(title);
		bookDto.setAuthor(author);
		bookDto.setYear(year);
		bookDto.setPrice(price);

		return bookService.createAndMapToDto(bookDto);
	}

	@MutationMapping
	// @formatter:off
	public BookDto updateBook(@Argument("id") Long id,
							@Argument("title") String title,
							@Argument("author") String author,
							@Argument("year") Short year,
							@Argument("price") BigDecimal price) {
	// @formatter:on
		BookDto bookDto = new BookDto();
		bookDto.setTitle(title);
		bookDto.setAuthor(author);
		bookDto.setYear(year);
		bookDto.setPrice(price);

		return bookService.updateByIdAndMapToDto(id, bookDto);
	}

	@MutationMapping
	public Boolean deleteBook(@Argument("id") Long id) {
		bookService.deleteById(id);

		return true;
	}

}