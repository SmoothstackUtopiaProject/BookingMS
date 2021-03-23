package com.ss.utopia.exceptions;

public class BookingUserNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public BookingUserNotFoundException() {}
	public  BookingUserNotFoundException(String message) {
		super(message);
	}
}
