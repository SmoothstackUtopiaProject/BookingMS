package com.ss.utopia.exceptions;

public class BookingAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public BookingAlreadyExistsException() {}
	public  BookingAlreadyExistsException(String message) {
		super(message);
	}
}
