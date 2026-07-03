package fi.ishtech.practice.springboot.booksapp.entity;

import java.io.Serial;
import java.math.BigDecimal;

import fi.ishtech.base.entity.BaseStandardEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "t_book",
		uniqueConstraints = @UniqueConstraint(name = "uk_book_title_author", columnNames = { "title", "author" }))
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Book extends BaseStandardEntity {

	@Serial
	private static final long serialVersionUID = -7930169589351866235L;

	@Column(nullable = false, length = 255)
	private String title;

	@Column(nullable = false, length = 255)
	private String author;

	@Column(nullable = false, precision = 4, columnDefinition = "SMALLINT")
	private Short year;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

}