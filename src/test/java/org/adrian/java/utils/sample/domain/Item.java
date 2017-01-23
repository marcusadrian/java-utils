package org.adrian.java.utils.sample.domain;

import java.util.Date;

import javax.persistence.Entity;

import org.adrian.java.utils.jpa.BaseEntity;

@Entity
public class Item extends BaseEntity {

	private String name;
	
	private Integer price;
	
	private Date dateOfPurchase;
	
	private Category category;
	
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
	
	public Date getDateOfPurchase() {
		return dateOfPurchase;
	}
	
	public void setDateOfPurchase(Date dateOfPurchase) {
		this.dateOfPurchase = dateOfPurchase;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
}
