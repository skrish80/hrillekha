package com.techlords.crown.business.model;

import java.util.Date;

import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public final class StockBO extends AppModel {

	private String warehouse = "";
	private String itemCategory = "";
	private String itemBrand = "";
	private String item = "";
	private String itemQuanity = "";
	private String UOMQuanity = "";
	private String priceStatus = "";
	private String allocationEntity = "";
	private String allocatedQuanity = "";
	private String lastUpdatedBy = "";
	private Date lastUpdatedDate;

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getUOMQuanity() {
		return UOMQuanity;
	}

	public void setUOMQuanity(String UOMQuanity) {
		this.UOMQuanity = UOMQuanity;
	}

	public String getAllocatedQuanity() {
		return allocatedQuanity;
	}

	public void setAllocatedQuanity(String allocatedQuanity) {
		this.allocatedQuanity = allocatedQuanity;
	}

	public String getAllocationEntity() {
		return allocationEntity;
	}

	public void setAllocationEntity(String allocationEntity) {
		this.allocationEntity = allocationEntity;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemQuanity() {
		return itemQuanity;
	}

	public void setItemQuanity(String itemQuanity) {
		this.itemQuanity = itemQuanity;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getPriceStatus() {
		return priceStatus;
	}

	public void setPriceStatus(String priceStatus) {
		this.priceStatus = priceStatus;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
}
