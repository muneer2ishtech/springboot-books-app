package fi.ishtech.practice.springboot.booksapp.dto;

import java.io.Serial;
import java.math.BigDecimal;

import fi.ishtech.base.vo.BaseStandardEntityVo;
import fi.ishtech.common.validation.MaxCurrentYear;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for Book
 *
 * @author Muneer Ahmed Syed
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class BookDto extends BaseStandardEntityVo {

	@Serial
	private static final long serialVersionUID = 607043909100998314L;

	@NotBlank
	private String title;

	@NotBlank
	private String author;

	@Min(1900)
	@MaxCurrentYear
	private Short year;

	@Positive
	private BigDecimal price;

}