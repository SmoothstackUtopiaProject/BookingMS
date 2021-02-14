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
	private Integer userId;

  public BookingUser(){}
  public BookingUser(Integer bookingId, Integer userId) {
    super();
    this.bookingId = bookingId;
    this.userId = userId;
  }

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}
}