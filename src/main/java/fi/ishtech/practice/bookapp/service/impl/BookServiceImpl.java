package fi.ishtech.practice.bookapp.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import fi.ishtech.practice.bookapp.dto.BookDto;
import fi.ishtech.practice.bookapp.entity.Book;
import fi.ishtech.practice.bookapp.mapper.BookMapper;
import fi.ishtech.practice.bookapp.repository.BookRepository;
import fi.ishtech.practice.bookapp.service.BookService;
import fi.ishtech.practice.bookapp.spec.BookSpec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Muneer Ahmed Syed
 */
@Service
@Transactional
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;
	private final BookMapper bookMapper;

	@Override
	public BookRepository getRepo() {
		return bookRepository;
	}

	@Override
	public BookMapper getMapper() {
		return bookMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public BookDto findOneByIdAndMapToVoOrElseThrow(Long id) {
		return getMapper().toBriefDto(this.findOneByIdOrElseThrow(id));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<BookDto> findAllAndMapToVo(BookSpec spec, Pageable pageable) {
		return this.findAll(spec, pageable).map(getMapper()::toBriefDto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookDto> findAllAndMapToVo() {
		List<Book> books = this.findAll();

		// @formatter:off
		return books == null ? null
				: books.stream()
					.map(getMapper()::toBriefDto)
					.toList();
		// @formatter:on
	}

	@Override
	public BookDto createAndMapToDto(BookDto bookDto) {
		Book book = bookMapper.toNewEntity(bookDto);

		book = bookRepository.save(book);

		return bookMapper.toBriefDto(book);
	}

	@Override
	public BookDto updateByIdAndMapToDto(Long id, BookDto bookDto) {
		Assert.isTrue(bookDto.getId() == null || id == bookDto.getId(), "Input id param not matching with id in DTO");

		Book book = findOneByIdOrElseThrow(id);

		bookMapper.toEntity(bookDto, book);

		book = bookRepository.save(book);

		return bookMapper.toBriefDto(book);
	}

	@Override
	public void deleteById(Long id) {
		bookRepository.deleteById(id);
	}
}
