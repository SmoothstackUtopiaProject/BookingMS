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
	private Integer isActive;

	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "confirmation_code", columnDefinition = "VARCHAR(255)")
	private String confirmationCode;

	public Booking() {}
	public Booking(Integer isActive) {
		super();
		this.isActive = isActive;

		// TODO: replace generated UUIDs with UUID input from PaymentMS
		this.confirmationCode = UUID.randomUUID().toString();
	}

	public Booking(Integer id, Integer isActive, String confirmationCode) {
		super();
		this.id = id;
		this.isActive = isActive;
		this.confirmationCode = confirmationCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}
}