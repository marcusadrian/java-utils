package org.adrian.java.utils.exception;

public class UnknownEnumValueException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnknownEnumValueException(String message) {
		super(message);
	}
}
