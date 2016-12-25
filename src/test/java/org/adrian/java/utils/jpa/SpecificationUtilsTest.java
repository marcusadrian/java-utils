package org.adrian.java.utils.jpa;

import org.adrian.java.utils.TestApplication;
import org.adrian.java.utils.sample.repo.ItemRepository;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class SpecificationUtilsTest {

	@Autowired
	private ItemRepository itemRepository;
	
	@Test
	public void find() {
		itemRepository.findAll(SpecificationUtils.toSpecification("name", "christmas oratorio"))
			.stream()
			.map(ToStringBuilder::reflectionToString)
			.forEach(System.out::println);
		
	}

}
