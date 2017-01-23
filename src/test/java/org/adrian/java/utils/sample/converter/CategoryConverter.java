package org.adrian.java.utils.sample.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.adrian.java.utils.sample.domain.Category;


@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<Category,Integer> {

	@Override
	public Integer convertToDatabaseColumn(Category category) {
		return Category.BY_CODE.toId(category);
	}

	@Override
	public Category convertToEntityAttribute(Integer code) {
		return Category.BY_CODE.toEnum(code);
	}
}
