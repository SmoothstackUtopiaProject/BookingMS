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
	private Integer id;

	@Column(name = "is_active")
	private Integer status;

	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "confirmation_code", columnDefinition = "VARCHAR(255)")
	private String confirmationCode;

	public Booking() {}
	public Booking(Integer status) {
		super();
		this.status = status;

		// TODO: replace generated UUIDs with UUID input from PaymentMS
		this.confirmationCode = UUID.randomUUID().toString();
	}

	public Booking(Integer id, Integer status, String confirmationCode) {
		super();
		this.id = id;
		this.status = status;
		this.confirmationCode = confirmationCode;
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
}