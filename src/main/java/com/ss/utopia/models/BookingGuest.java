package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "booking_guest")
public class BookingGuest {
  
  @Id
	@Column(name = "booking_id")
	private Integer bookingId;

  @Column(name = "contact_email")
	private String email;

  @Column(name = "contact_phone")
	private String phone;

  public BookingGuest(){}
  public BookingGuest(Integer bookingId, String email, String phone) {
    super();
    this.bookingId = bookingId;
    this.email = email;
    this.phone = phone;
  }

	public Integer getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

  public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

  public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}