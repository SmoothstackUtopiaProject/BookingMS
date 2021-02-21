package com.ss.utopia.models;

public class BookingWithReferenceData {
	
	private Integer id;
	private Integer status;
	private String confirmationCode;
	private Integer flightId;
	private Integer passengerId;
	private Integer userId;
	private String guestEmail;
	private String guestPhone;

	public BookingWithReferenceData(){}
	public BookingWithReferenceData(Integer id, Integer status, String confirmationCode, 
	Integer flightId, Integer passengerId, Integer userId) {
		super();
		this.id = id;
		this.status = status;
		this.confirmationCode = confirmationCode;
		this.flightId = flightId;
		this.passengerId = passengerId;
		this.userId = userId;
		this.guestEmail = "";
		this.guestPhone = "";
	}

	public BookingWithReferenceData(Integer id, Integer status, String confirmationCode, 
	Integer flightId, Integer passengerId, Integer userId, String guestEmail, String guestPhone) {
		super();
		this.id = id;
		this.status = status;
		this.confirmationCode = confirmationCode;
		this.flightId = flightId;
		this.passengerId = passengerId;
		this.userId = userId;
		this.guestEmail = guestEmail;
		this.guestPhone = guestPhone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setstatus(Integer status) {
		this.status = status;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	public Integer getFlightId() {
		return flightId;
	}

	public void setFlightId(Integer flightId) {
		this.flightId = flightId;
	}

	public Integer getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(Integer passengerId) {
		this.passengerId = passengerId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getGuestEmail() {
		return guestEmail;
	}

	public void setGuestEmail(String guestEmail) {
		this.guestEmail = guestEmail;
	}

	public String getGuestPhone() {
		return guestPhone;
	}

	public void setGuestPhone(String guestPhone) {
		this.guestPhone = guestPhone;
	}
}