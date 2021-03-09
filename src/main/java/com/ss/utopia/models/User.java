package com.ss.utopia.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer userId;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "role_id")
	private Integer userRoleId;
	@Column(name = "first_name")
	private String userFirstName;
	@Column(name = "last_name")
	private String userLastName;
	@Column(name = "email")
	private String userEmail;
	@Column(name = "password")
	private String userPassword;
	@Column(name = "phone")
	private String userPhone;
	
	
	public User() {}
	public User(Integer userId, Integer userRoleId, String userFirstName, 
	String userLastName, String userEmail, String userPassword, String userPhone) {
		super();
		this.userId = userId;
		this.userRoleId = userRoleId;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userPhone = userPhone;
	}

	public User(Integer userRoleId, String userFirstName, 
	String userLastName, String userEmail, String userPassword, String userPhone) {
		super();
		this.userRoleId = userRoleId;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userPhone = userPhone;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getInteger() {
		return userRoleId;
	}

	public void setInteger(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
}