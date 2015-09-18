package com.techlords.crown.business.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class WarehouseBO extends AppModel {

	@NotEmpty(message = "Warehouse Name cannot be empty")
	@Size(min = 5, max = 25, message = "Warehouse Name shall be from 5 to 25 chars")
	private String warehouseName;
	@NotEmpty(message = "Description cannot be empty")
	@Size(min = 5, max = 50, message = "Description shall be from 5 to 50 chars")
	private String description;
	@NotEmpty(message = "Address cannot be empty")
	@Size(min = 5, max = 2500, message = "Address shall be from 5 to 2500 chars")
	private String address;
	@Min(value = 1, message = "Select a location")
	private int location;
	@Min(value = 1, message = "Select the warehouse incharge person")
	private int incharge;
	@Min(value = 1, message = "Select an entity")
	private int entity;
	private boolean isRetailShop;

	private LocationBO locationBO;
	private CrownEntityBO entityBO;
	private CrownUserBO inchargeBO;

	private StatusBO status;

	public final String getWarehouseName() {
		return warehouseName;
	}

	public final void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final String getAddress() {
		return address;
	}

	public final void setAddress(String address) {
		this.address = address;
	}

	public final int getLocation() {
		return location;
	}

	public final void setLocation(int location) {
		this.location = location;
	}

	public final int getIncharge() {
		return incharge;
	}

	public final void setIncharge(int incharge) {
		this.incharge = incharge;
	}

	public final int getEntity() {
		return entity;
	}

	public final void setEntity(int entity) {
		this.entity = entity;
	}

	public final StatusBO getStatus() {
		return status;
	}

	public final void setStatus(StatusBO status) {
		this.status = status;
	}

	public final LocationBO getLocationBO() {
		return locationBO;
	}

	public final void setLocationBO(LocationBO locationBO) {
		this.locationBO = locationBO;
	}

	public final CrownEntityBO getEntityBO() {
		return entityBO;
	}

	public final void setEntityBO(CrownEntityBO entityBO) {
		this.entityBO = entityBO;
	}

	public final CrownUserBO getInchargeBO() {
		return inchargeBO;
	}

	public final void setInchargeBO(CrownUserBO inchargeBO) {
		this.inchargeBO = inchargeBO;
	}

	public final boolean isRetailShop() {
		return isRetailShop;
	}

	public final void setRetailShop(boolean isRetail) {
		this.isRetailShop = isRetail;
	}
}