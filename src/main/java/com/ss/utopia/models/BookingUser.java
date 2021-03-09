package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "booking_user")
public class BookingUser {
  
  @Id
	@Column(name = "booking_id")
	private Integer bookingId;

	@Column(name = "user_id")
	private Integer bookingUserId;

  public BookingUser(){}
  public BookingUser(Integer bookingId, Integer bookingUserId) {
    super();
    this.bookingId = bookingId;
    this.bookingUserId = bookingUserId;
  }

	public Integer getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public Integer getBookingUserId() {
		return this.bookingUserId;
	}

	public void setBookingUserId(Integer bookingUserId) {
		this.bookingUserId = bookingUserId;
	}
}