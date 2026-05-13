package fi.ishtech.practice.bookapp.spec;

import java.io.Serial;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import fi.ishtech.base.spec.BaseStandardSpec;
import fi.ishtech.practice.bookapp.dto.BookFilterParams;
import fi.ishtech.practice.bookapp.entity.Book;
import fi.ishtech.practice.bookapp.entity.Book_;

/**
 * Specification for Book
 *
 * @author Muneer Ahmed Syed
 */
public class BookSpec extends BaseStandardSpec<Book, BookFilterParams> {

	@Serial
	private static final long serialVersionUID = 7727812343620150277L;

	/**
	 * Constructor with filter params
	 *
	 * @param params {@link BookFilterParams}
	 */
	public BookSpec(BookFilterParams params) {
		super(params);
	}

	@Override
	protected List<Predicate> toPredicateList(Root<Book> root, CriteriaBuilder cb) {
		List<Predicate> predicates = super.toPredicateList(root, cb);

		addPredicateLike(predicates, root, cb, getParams().getTitle(), Book_.TITLE);

		addPredicateLike(predicates, root, cb, getParams().getAuthor(), Book_.AUTHOR);

		addPredicateGE(predicates, root, cb, getParams().getYearStart(), Book_.YEAR);
		addPredicateLE(predicates, root, cb, getParams().getYearEnd(), Book_.YEAR);

		addPredicateGE(predicates, root, cb, getParams().getPriceStart(), Book_.PRICE);
		addPredicateLE(predicates, root, cb, getParams().getPriceEnd(), Book_.PRICE);

		return predicates;
	}

}