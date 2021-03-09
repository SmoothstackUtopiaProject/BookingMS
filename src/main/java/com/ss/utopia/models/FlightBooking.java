package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "flight_bookings")
public class FlightBooking {
  
	@Column(name = "flight_id")
	private Integer flightId;

	@Id
	@Column(name = "booking_id")
	private Integer bookingId;

  public FlightBooking(){}
  public FlightBooking(Integer flightId, Integer bookingId) {
    super();
    this.flightId = flightId;
    this.bookingId = bookingId;
  }

	public Integer getFlightId() {
		return this.flightId;
	}

	public void setFlightId(Integer flightId) {
		this.flightId = flightId;
	}

	public Integer getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}
}