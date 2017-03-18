package org.adrian.java.utils.vo;

public class Option<T> {

	private T id;
	private String label;

	public static<T> Option<T> OF (T id, String label) {
		Option<T> o = new Option<>();
		o.id = id;
		o.label = label;
		return o;
	}
	
	private Option() {
	}

	public T getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return String.format("%s - %s", id, label);
	}

}
