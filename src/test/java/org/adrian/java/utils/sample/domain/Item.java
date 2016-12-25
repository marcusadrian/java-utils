package org.adrian.java.utils.sample.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.adrian.java.utils.jpa.BaseEntity;

@Entity
public class Item extends BaseEntity {

	private String name;
	
	private Integer price;
	
	@Column(name = "category")
	private Integer categoryCode;
	
	public Category getCategory() {
		return Category.BY_CODE.toEnum(categoryCode);
	}

	public void setCategory(Category category) {
		categoryCode = Category.BY_CODE.toId(category);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	
	
}
