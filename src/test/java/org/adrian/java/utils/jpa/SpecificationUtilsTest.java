package org.adrian.java.utils.jpa;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.function.Predicate;

import org.adrian.java.utils.TestApplication;
import org.adrian.java.utils.jpa.PredicateUtils.ComparatorKeyword;
import org.adrian.java.utils.sample.domain.Category;
import org.adrian.java.utils.sample.domain.Item;
import org.adrian.java.utils.sample.domain.PropertyNames;
import org.adrian.java.utils.sample.repo.ItemRepository;
import org.adrian.java.utils.utils.TestUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class SpecificationUtilsTest {

	@Autowired
	private ItemRepository itemRepository;
	
	@Test
	public void toSpecification() {
		
		String name = "Christmas Oratorio";
		Predicate<Item> p = i -> i.getName().equals(name);
		Specification<Item> specification = SpecificationUtils.toSpecification(PropertyNames.Item.NAME, name);
		testSpecification(specification, p);

		Category category = Category.ORATORIO;
		p = i -> i.getCategory() == category;
		specification = SpecificationUtils.toSpecification(PropertyNames.Item.CATEGORY, category);
		testSpecification(specification, p);
	}
	
	@Test
	public void toSpecificationComparatorKeyword() {
		
		int price = 1850;
		Predicate<Item> p = i -> i.getPrice() < price;
		Specification<Item> specification = SpecificationUtils.toSpecification(PropertyNames.Item.PRICE, price, ComparatorKeyword.lt);
		testSpecification(specification, p);

		p = i -> i.getPrice() <= price;
		specification = SpecificationUtils.toSpecification(PropertyNames.Item.PRICE, price, ComparatorKeyword.le);
		testSpecification(specification, p);
		
		p = i -> i.getPrice() > price;
		specification = SpecificationUtils.toSpecification(PropertyNames.Item.PRICE, price, ComparatorKeyword.gt);
		testSpecification(specification, p);

		p = i -> i.getPrice() >= price;
		specification = SpecificationUtils.toSpecification(PropertyNames.Item.PRICE, price, ComparatorKeyword.ge);
		testSpecification(specification, p);

		p = i -> i.getPrice() == price;
		specification = SpecificationUtils.toSpecification(PropertyNames.Item.PRICE, price, ComparatorKeyword.eq);
		testSpecification(specification, p);
		
		Date dateOfPurchase = TestUtils.toDate("2014-08-14");
		p = i -> i.getDateOfPurchase().getTime() <= dateOfPurchase.getTime();
		specification = SpecificationUtils.toSpecification(PropertyNames.Item.DATE_OF_PURCHASE, dateOfPurchase, ComparatorKeyword.le);
		testSpecification(specification, p);

	}
	
	private void testSpecification(Specification<Item> specification, Predicate<Item> p) {
		TestUtils.debug(() -> "---------------------------");
		itemRepository.findAll(specification)
		.stream()
		.forEach(i -> checkPredicate(i, p));
	}

	private void checkPredicate(Item bean, Predicate<Item> p) {
		TestUtils.debug(() -> ToStringBuilder.reflectionToString(bean));
		assertTrue(p.test(bean));
	}

}
