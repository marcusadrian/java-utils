package org.adrian.java.utils.sample.domain;

import org.adrian.java.utils.transform.EnumSupport;

public enum Category {

	OPERA(1),
	ORATORIO(2),
	SYMPHONIC(3),
	CHAMBER(4)
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
