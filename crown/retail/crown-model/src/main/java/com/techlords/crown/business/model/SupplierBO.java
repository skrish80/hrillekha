package com.techlords.crown.business.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class SupplierBO extends AppModel {

	@NotEmpty(message = "Supplier Code cannot be empty")
	@Size(min = 5, max = 25, message = "Supplier Code shall be from 5 to 25 chars")
	private String supplierCode;
	@NotEmpty(message = "Supplier Name cannot be empty")
	@Size(min = 5, max = 25, message = "Supplier Name shall be from 5 to 25 chars")
	private String supplierName;
	@NotEmpty(message = "Supplier Address cannot be empty")
	@Size(min = 5, max = 2500, message = "Supplier Address shall be from 5 to 25 chars")
	private String address;
	@NotEmpty(message = "Supplier Phone cannot be empty")
	@Size(min = 5, max = 25, message = "Supplier Phone shall be from 5 to 25 chars")
	private String phone;
	@NotEmpty(message = "Supplier FAX cannot be empty")
	@Size(min = 5, max = 25, message = "Supplier FAX shall be from 5 to 25 chars")
	private String fax;
	@NotEmpty(message = "Point of Contact cannot be empty")
	@Size(min = 5, max = 25, message = "Point of Contact shall be from 5 to 25 chars")
	private String poc;
	@NotEmpty(message = "Supplier SBN cannot be empty")
	@Size(min = 5, max = 25, message = "Supplier SBN shall be from 5 to 25 chars")
	private String sbn;// shall be unique
	@NotEmpty(message = "Supplier Remarks cannot be empty")
	@Size(min = 5, max = 25, message = "Supplier Remarks shall be from 5 to 25 chars")
	private String remarks;

	@Min(value = 1, message = "Select a location")
	private int location;

	private LocationBO locationBO;
	private StatusBO statusBO;

	public final String getSupplierCode() {
		return supplierCode;
	}

	public final void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public final String getSupplierName() {
		return supplierName;
	}

	public final void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public final String getAddress() {
		return address;
	}

	public final void setAddress(String address) {
		this.address = address;
	}

	public final String getPhone() {
		return phone;
	}

	public final void setPhone(String phone) {
		this.phone = phone;
	}

	public final String getFax() {
		return fax;
	}

	public final void setFax(String fax) {
		this.fax = fax;
	}

	public final String getPoc() {
		return poc;
	}

	public final void setPoc(String poc) {
		this.poc = poc;
	}

	public final String getSbn() {
		return sbn;
	}

	public final void setSbn(String sbn) {
		this.sbn = sbn;
	}

	public final String getRemarks() {
		return remarks;
	}

	public final void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public final int getLocation() {
		return location;
	}

	public final void setLocation(int location) {
		this.location = location;
	}

	public final LocationBO getLocationBO() {
		return locationBO;
	}

	public final void setLocationBO(LocationBO locationBO) {
		this.locationBO = locationBO;
	}

	public final StatusBO getStatusBO() {
		return statusBO;
	}

	public final void setStatusBO(StatusBO statusBO) {
		this.statusBO = statusBO;
	}
}