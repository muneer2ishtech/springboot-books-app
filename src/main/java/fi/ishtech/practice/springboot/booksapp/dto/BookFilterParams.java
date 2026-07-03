package fi.ishtech.practice.springboot.booksapp.dto;

import java.io.Serial;
import java.math.BigDecimal;

import fi.ishtech.base.payload.filter.BaseStandardEntityFilterParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Filter params for Book
 *
 * @author Muneer Ahmed Syed
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class BookFilterParams extends BaseStandardEntityFilterParams {

	@Serial
	private static final long serialVersionUID = 6421687291342681786L;

	private String title;

	private String author;

	private Short yearStart;
	private Short yearEnd;

	private BigDecimal priceStart;
	private BigDecimal priceEnd;

}