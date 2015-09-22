package com.techlords.crown.business.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.infra.AppModel;
import com.techlords.infra.UserDetails;

@SuppressWarnings("serial")
public class CrownUserBO extends AppModel implements UserDetails {

	@NotEmpty(message = "Employee number cannot be empty")
	@Size(min = 5, max = 25, message = "Employee number shall be from 5 to 25 chars")
	private String employeeNo;
	@NotEmpty(message = "User Name cannot be empty")
	@Size(min = 5, max = 25, message = "User Name shall be from 5 to 25 chars")
	private String username;
	@NotEmpty(message = "Password cannot be empty")
	@Size(min = 6, max = 25, message = "Password shall be from 5 to 25 chars")
	private String password;
	private String changedPassword;

	@NotEmpty(message = "First Name cannot be empty")
	@Size(min = 1, max = 25, message = "First Name shall be from 5 to 25 chars")
	private String firstName;
	@NotEmpty(message = "Last Name cannot be empty")
	@Size(min = 1, max = 25, message = "Last Name shall be from 5 to 25 chars")
	private String lastName;

	@Email(message = "Not a proper email")
	@NotEmpty(message = "email cannot be empty")
	private String email;

	@NotEmpty(message = "Address cannot be empty")
	@Size(min = 5, max = 2500, message = "Address shall be from 5 to 2500 chars")
	private String address;
	@NotEmpty(message = "Mobile Number cannot be empty")
	private String mobileNumber;

	@Min(value = 1, message = "Select a role")
	private int role;

	@JsonIgnore
	private RoleBO roleBO;

	@NotNull(message = "Join Date cannot be null")
	private Date joinDate;
	@NotNull(message = "Date of Birth cannot be null")
	private Date dateOfBirth;
	@JsonIgnore
	private StatusBO status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the role
	 */
	public final int getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public final void setRole(int role) {
		this.role = role;
	}

	/**
	 * @return the roleBO
	 */
	public final RoleBO getRoleBO() {
		return roleBO;
	}

	/**
	 * @param roleBO
	 *            the roleBO to set
	 */
	public final void setRoleBO(RoleBO roleBO) {
		this.roleBO = roleBO;
	}

	/**
	 * @return the joinDate
	 */
	public final Date getJoinDate() {
		return joinDate;
	}

	/**
	 * @param joinDate
	 *            the joinDate to set
	 */
	public final void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	/**
	 * @return the status
	 */
	public final StatusBO getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public final void setStatus(StatusBO status) {
		this.status = status;
	}

	/**
	 * @return the password
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public final void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the firstName
	 */
	public final String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public final void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public final String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public final void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public final String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public final void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the mobileNumber
	 */
	public final String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public final void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the dateOfBirth
	 */
	public final Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public final void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	@JsonIgnore
	public Collection<String> getAuthorities() {
		return Arrays.asList(getRoleBO().getRole());
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return (status == StatusBO.ACTIVE);
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return (status == StatusBO.ACTIVE);
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return (status == StatusBO.ACTIVE);
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return (status == StatusBO.ACTIVE);
	}

	@JsonIgnore
	public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getChangedPassword() {
		return changedPassword;
	}

	public void setChangedPassword(String changedPassword) {
		this.changedPassword = changedPassword;
	}
}