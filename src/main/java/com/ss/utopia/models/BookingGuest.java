package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "booking_guest")
public class BookingGuest {
  
  @Id
	@GeneratedValue
	@Column(name = "booking_id")
	private Integer bookingGuestId;

  @Column(name = "contact_email")
	private String bookingGuestEmail;

  @Column(name = "contact_phone")
	private String bookingGuestPhone;

  public BookingGuest(){}
  public BookingGuest(String bookingGuestEmail, String bookingGuestPhone) {
    this.bookingGuestEmail = bookingGuestEmail;
    this.bookingGuestPhone = bookingGuestPhone;
  }

  public BookingGuest(Integer bookingGuestId, String bookingGuestEmail, String bookingGuestPhone) {
    this.bookingGuestId = bookingGuestId;
    this.bookingGuestEmail = bookingGuestEmail;
    this.bookingGuestPhone = bookingGuestPhone;
  }

	public Integer getBookingGuestId() {
		return this.bookingGuestId;
	}

	public void setBookingGuestId(Integer bookingGuestId) {
		this.bookingGuestId = bookingGuestId;
	}

  public String getBookingGuestEmail() {
		return this.bookingGuestEmail;
	}

	public void setBookingGuestEmail(String bookingGuestEmail) {
		this.bookingGuestEmail = bookingGuestEmail;
	}

  public String getBookingGuestPhone() {
		return this.bookingGuestPhone;
	}

	public void setBookingGuestPhone(String bookingGuestPhone) {
		this.bookingGuestPhone = bookingGuestPhone;
	}
}