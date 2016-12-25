package org.adrian.java.utils.sample.domain;

import org.adrian.java.utils.transform.EnumSupport;

public enum Category {

	STARTER(1),
	MAIN_COURSE(2),
	DESSERT(3),
	DRINK(4)
	;
	
	private Integer code;
	
	public static final EnumSupport<Category, Integer> BY_CODE = new EnumSupport<>(Category.values(), Category::getCode);
	
	private Category(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}
}
