package org.adrian.java.utils.transform;

import static org.junit.Assert.assertEquals;

import org.adrian.java.utils.sample.domain.Item;
import org.adrian.java.utils.sample.domain.ItemDto;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

public class GenericTransformerTest {

	@Test
	public void test() {
		GenericTransformer<Item, ItemDto> transformer = new GenericTransformer<>(ItemDto::new, (item, itemDto) -> {
			itemDto.setName(item.getName());
			itemDto.setPrice(item.getPrice());
		});
		Item item = new Item();
		item.setName("goody");
		item.setPrice(34);
		
		ItemDto dto = transformer.transform(item);
		
		verifyEquals(item, dto);
		
		item.setPrice(45);
		
		transformer.update(item, dto);
		
		verifyEquals(item, dto);
		
	}
	
	private void verifyEquals(Item item, ItemDto dto) {
		System.out.println(ToStringBuilder.reflectionToString(dto));
		assertEquals(item.getName(), dto.getName());
		assertEquals(item.getName(), dto.getName());
	}

}
