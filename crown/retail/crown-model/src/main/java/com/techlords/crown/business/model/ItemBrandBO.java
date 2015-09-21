package com.techlords.crown.business.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class ItemBrandBO extends AppModel {

	@NotEmpty(message = "Brand cannot be empty")
	@Size(min = 1, max = 25, message = "Brand shall be from 1 to 25 chars")
	private String itemBrand;
	private String brandCode;
	private String description;
	@NotNull(message = "Select a Status")
	private StatusBO status;

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StatusBO getStatus() {
		return status;
	}

	public void setStatus(StatusBO status) {
		this.status = status;
	}

	/**
	 * @return the brandCode
	 */
	public String getBrandCode() {
		return brandCode;
	}

	/**
	 * @param brandCode the brandCode to set
	 */
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
}