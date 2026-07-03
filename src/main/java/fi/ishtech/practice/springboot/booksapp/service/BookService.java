package fi.ishtech.practice.springboot.booksapp.service;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fi.ishtech.base.service.BaseStandardService;
import fi.ishtech.practice.springboot.booksapp.dto.BookDto;
import fi.ishtech.practice.springboot.booksapp.entity.Book;
import fi.ishtech.practice.springboot.booksapp.spec.BookSpec;

/**
 * Service interface for Book related operations
 *
 * @author Muneer Ahmed Syed
 */
public interface BookService extends BaseStandardService<Book, BookDto> {

	/**
	 * Creates new {@link Book} and returns the Dto with ID
	 *
	 * @param book {@link BookDto}
	 * @return {@link BookDto}
	 */
	BookDto createAndMapToDto(@NotNull @Valid BookDto bookDto);

	/**
	 * Finds Book(s) by Specification and paginate them
	 *
	 * @param spec     {@link BookSpec}
	 * @param pageable {@link Pageable}
	 * @return {@link Page}&lt;{@link BookDto}&gt;
	 */
	Page<BookDto> findAllAndMapToVo(BookSpec spec, Pageable pageable);

	/**
	 * Finds all Book(s) and maps to Dto
	 *
	 * @return {@link List}&lt;{@link BookDto}&gt;
	 */
	List<BookDto> findAllAndMapToVo();

	/**
	 * Finds by id and updates {@link Book} entity and throws exception if not present
	 *
	 * @param id   {@link Long}
	 * @param book {@link BookDto}
	 * @return {@link BookDto}
	 */
	BookDto updateByIdAndMapToDto(@NotNull Long id, @NotNull @Valid BookDto book);

	/**
	 * Finds by id delete, ignores if not present
	 *
	 * @param id {@link Long}
	 */
	void deleteById(@NotNull Long id);

}