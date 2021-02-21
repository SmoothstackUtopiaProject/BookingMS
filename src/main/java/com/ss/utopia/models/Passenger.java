package com.ss.utopia.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "passenger")
public class Passenger {
  @Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "booking_id")
	private Integer bookingId;

  @Column(name = "first_name")
	private String firstName;

  @Column(name = "last_name")
	private String lastName;

  @Column(name = "date_of_birth")
	private Date dateOfBirth;

  @Column(name = "gender")
	private String sex;

  @Column(name = "address")
	private String address;

  public Passenger(){}
  public Passenger(Integer id, Integer bookingId, String firstName, String lastName, Date dateOfBirth, String sex, String address) {
    super();
		this.id = id;
    this.bookingId = bookingId;
    this.firstName = firstName;
		this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.sex = sex;
    this.address = address;
  }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

  public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

  public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

  public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}