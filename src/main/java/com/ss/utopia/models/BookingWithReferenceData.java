package com.ss.utopia.models;

public class BookingWithReferenceData {
	
	private Integer bookingId;
	private String bookingStatus;
	private String bookingConfirmationCode;
	private Integer bookingFlightId;
	private Integer bookingPassengerId;
	private Integer bookingUserId;
	private String bookingGuestEmail;
	private String bookingGuestPhone;

	public BookingWithReferenceData(){}
	public BookingWithReferenceData(Integer bookingId, String bookingStatus, String bookingConfirmationCode, 
	Integer bookingFlightId, Integer bookingPassengerId, Integer bookingUserId) {
		super();
		this.bookingId = bookingId;
		this.bookingStatus = bookingStatus;
		this.bookingConfirmationCode = bookingConfirmationCode;
		this.bookingFlightId = bookingFlightId;
		this.bookingPassengerId = bookingPassengerId;
		this.bookingUserId = bookingUserId;
		this.bookingGuestEmail = "";
		this.bookingGuestPhone = "";
	}

	public BookingWithReferenceData(Integer bookingId, String bookingStatus, String bookingConfirmationCode, 
	Integer bookingFlightId, Integer bookingPassengerId, Integer bookingUserId, String bookingGuestEmail, String bookingGuestPhone) {
		super();
		this.bookingId = bookingId;
		this.bookingStatus = bookingStatus;
		this.bookingConfirmationCode = bookingConfirmationCode;
		this.bookingFlightId = bookingFlightId;
		this.bookingPassengerId = bookingPassengerId;
		this.bookingUserId = bookingUserId;
		this.bookingGuestEmail = bookingGuestEmail;
		this.bookingGuestPhone = bookingGuestPhone;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingstatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getBookingConfirmationCode() {
		return bookingConfirmationCode;
	}

	public void setBookingConfirmationCode(String bookingConfirmationCode) {
		this.bookingConfirmationCode = bookingConfirmationCode;
	}

	public Integer getBookingFlightId() {
		return bookingFlightId;
	}

	public void setBookingFlightId(Integer bookingFlightId) {
		this.bookingFlightId = bookingFlightId;
	}

	public Integer getBookingPassengerId() {
		return bookingPassengerId;
	}

	public void setBookingPassengerId(Integer bookingPassengerId) {
		this.bookingPassengerId = bookingPassengerId;
	}

	public Integer getBookingUserId() {
		return bookingUserId;
	}

	public void setBookingUserId(Integer bookingUserId) {
		this.bookingUserId = bookingUserId;
	}

	public String getBookingGuestEmail() {
		return bookingGuestEmail;
	}

	public void setBookingGuestEmail(String bookingGuestEmail) {
		this.bookingGuestEmail = bookingGuestEmail;
	}

	public String getBookingGuestPhone() {
		return bookingGuestPhone;
	}

	public void setBookingGuestPhone(String bookingGuestPhone) {
		this.bookingGuestPhone = bookingGuestPhone;
	}
}