package com.techlords.crown.business.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class ItemCategoryBO extends AppModel {

	@NotEmpty(message = "Category cannot be empty")
	@Size(min = 1, max = 25, message = "Category shall be from 1 to 25 chars")
	private String itemCategory;

	@NotEmpty(message = "Category code cannot be empty")
	@Size(min = 1, max = 25, message = "Category code shall be from 1 to 25 chars")
	private String categoryCode;
	@Size(min=5, max=50, message="Description shall be from 5 to 50 chars")
	private String description;
	private StatusBO status;

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
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
	 * @return the categoryCode
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	/**
	 * @param categoryCode the categoryCode to set
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
}