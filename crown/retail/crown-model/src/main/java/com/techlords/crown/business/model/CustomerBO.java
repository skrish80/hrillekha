package com.techlords.crown.business.model;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class CustomerBO extends AppModel {

	@NotEmpty(message = "Customer Code cannot be empty")
	@Size(min = 5, max = 25, message = "Customer Code shall be from 5 to 25 chars")
	private String customerCode;
	@NotEmpty(message = "Customer Name cannot be empty")
	@Size(min = 5, max = 25, message = "Customer Name shall be from 5 to 25 chars")
	private String customerName;
	@NotEmpty(message = "Customer Address cannot be empty")
	@Size(min = 5, max = 2500, message = "Customer Address shall be from 5 to 25 chars")
	private String address;
	@NotEmpty(message = "Customer Phone cannot be empty")
	@Size(min = 5, max = 25, message = "Customer Phone shall be from 5 to 25 chars")
	private String phone;
	private String remarks;
	private Date dateOfBirth;
	private Date anniversary;

	@Min(value = 1, message = "Select a customer type")
	private int customerType;

	private CrownUserBO crownUserBO;
	private CustomerTypeBO customerTypeBO;
	private LocationBO locationBO;
	private StatusBO statusBO;

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the customerType
	 */
	public final int getCustomerType() {
		return customerType;
	}

	/**
	 * @param customerType
	 *            the customerType to set
	 */
	public final void setCustomerType(int customerType) {
		this.customerType = customerType;
	}

	/**
	 * @return the crownUserBO
	 */
	public final CrownUserBO getCrownUserBO() {
		return crownUserBO;
	}

	/**
	 * @param crownUserBO
	 *            the crownUserBO to set
	 */
	public final void setCrownUserBO(CrownUserBO crownUserBO) {
		this.crownUserBO = crownUserBO;
	}

	/**
	 * @return the customerTypeBO
	 */
	public final CustomerTypeBO getCustomerTypeBO() {
		return customerTypeBO;
	}

	/**
	 * @param customerTypeBO
	 *            the customerTypeBO to set
	 */
	public final void setCustomerTypeBO(CustomerTypeBO customerTypeBO) {
		this.customerTypeBO = customerTypeBO;
	}

	/**
	 * @return the locationBO
	 */
	public final LocationBO getLocationBO() {
		return locationBO;
	}

	/**
	 * @param locationBO
	 *            the locationBO to set
	 */
	public final void setLocationBO(LocationBO locationBO) {
		this.locationBO = locationBO;
	}

	/**
	 * @return the statusBO
	 */
	public final StatusBO getStatusBO() {
		return statusBO;
	}

	/**
	 * @param statusBO
	 *            the statusBO to set
	 */
	public final void setStatusBO(StatusBO statusBO) {
		this.statusBO = statusBO;
	}

	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the anniversary
	 */
	public Date getAnniversary() {
		return anniversary;
	}

	/**
	 * @param anniversary
	 *            the anniversary to set
	 */
	public void setAnniversary(Date anniversary) {
		this.anniversary = anniversary;
	}
}