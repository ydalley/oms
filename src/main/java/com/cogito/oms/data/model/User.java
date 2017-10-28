package com.cogito.oms.data.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;

@Entity
public class User extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -466205834075055026L;
	public static final String USER = "USER_TOKEN";
	@NotEmpty
	private String firstName;
	
	private String lastName;
	@Email
	private String email;
	private String phone;
	private String password;
	private String status;
	private Date expiryDate;
	private Date lastLoginDate;
	@Column(unique=true) @NotEmpty
	private String loginId;
	
	
	@ManyToOne @NotNull
	private Profile profile;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	
	
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public String toShortString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", loginId=" + loginId + ", profile="
				+ profile + "]";
	}
	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", phone=" + phone
				+ ", password=" + password + ", status=" + status + ", expiryDate=" + expiryDate + ", loginId="
				+ loginId + ", profile=" + profile + ", id=" + id + ", delFlag=" + delFlag + "]";
	}
	@Override
	public List<String> getDefaultSearchFields() {
		return Arrays.asList("firstName", "lastName","loginId");
	}
	
	
	
}
