package com.ss.utopia.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "booking")
public class Booking {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer bookingId;

	@Column(name = "status")
	private String bookingStatus;

	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "confirmation_code", columnDefinition = "VARCHAR(255)")
	private String bookingConfirmationCode;

	public Booking() {}
	public Booking(String bookingStatus) {
		this.bookingStatus = bookingStatus;
		this.bookingConfirmationCode = UUID.randomUUID().toString();
	}

	public Booking(Integer bookingId, String bookingStatus, String bookingConfirmationCode) {
		this.bookingId = bookingId;
		this.bookingStatus = bookingStatus;
		this.bookingConfirmationCode = bookingConfirmationCode;
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

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getBookingConfirmationCode() {
		return bookingConfirmationCode;
	}

	public void setBookingConfirmationCode(String bookingConfirmationCode) {
		this.bookingConfirmationCode = bookingConfirmationCode;
	}
}