package fi.ishtech.practice.springboot.booksapp.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import fi.ishtech.base.annotations.mapstruct.BriefMapping;
import fi.ishtech.base.mapper.BaseStandardMapper;
import fi.ishtech.practice.springboot.booksapp.dto.BookDto;
import fi.ishtech.practice.springboot.booksapp.entity.Book;

/**
 * Mapper for {@link BookDto} to {@link Book} entity and vice-versa
 *
 * @author Muneer Ahmed Syed
 */
@Mapper(componentModel = "spring")
public interface BookMapper extends BaseStandardMapper {

	/**
	 * Map basic attributes from entity to DTO
	 *
	 * @param entity {@link Book}
	 * @return {@link BookDto}
	 */
	@BriefMapping
	@BeanMapping(ignoreByDefault = true)
	@InheritConfiguration(name = "toBaseStandardVo")
	@Mapping(source = "id", target = "id")
	@Mapping(source = "title", target = "title")
	@Mapping(source = "author", target = "author")
	@Mapping(source = "year", target = "year")
	@Mapping(source = "price", target = "price")
	BookDto toBriefDto(Book entity);

	/**
	 *
	 * @param dto    {@link BookDto}
	 * @param entity {@link Book}
	 * @return updated {@link Book} entity
	 */
	@InheritInverseConfiguration(name = "toBriefDto")
	@Mapping(source = "id", target = "id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Book toEntity(BookDto dto, @MappingTarget Book entity);

	/**
	 *
	 * @param dto {@link BookDto}
	 * @return new {@link Book} entity
	 */
	@InheritInverseConfiguration(name = "toBriefDto")
	@Mapping(target = "id", ignore = true)
	Book toNewEntity(BookDto dto);

}