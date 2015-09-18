package com.techlords.crown.business.model;

import java.util.Date;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public final class StolenStockBO extends AppModel {

	@Min(value = 1, message = "Select an item")
	private int item;
	@Min(value = 1, message = "Select a warehouse")
	private int warehouse;

	private ItemBO itemBO;
	private WarehouseBO warehouseBO;

	private int stolenItemQty;
	private int stolenUOMQty;

	private Date stolenDate;
	
	@NotEmpty(message = "Remarks cannot be empty")
	private String remarks;
	
	private String createdBy;

	public final int getItem() {
		return item;
	}

	public final void setItem(int item) {
		this.item = item;
	}

	public final int getWarehouse() {
		return warehouse;
	}

	public final void setWarehouse(int warehouse) {
		this.warehouse = warehouse;
	}

	public final ItemBO getItemBO() {
		return itemBO;
	}

	public final void setItemBO(ItemBO itemBO) {
		this.itemBO = itemBO;
	}

	public final WarehouseBO getWarehouseBO() {
		return warehouseBO;
	}

	public final void setWarehouseBO(WarehouseBO warehouseBO) {
		this.warehouseBO = warehouseBO;
	}

	public final int getStolenItemQty() {
		return stolenItemQty;
	}

	public final void setStolenItemQty(int stolenItemQty) {
		this.stolenItemQty = stolenItemQty;
	}

	public final int getStolenUOMQty() {
		return stolenUOMQty;
	}

	public final void setStolenUOMQty(int stolenUOMQty) {
		this.stolenUOMQty = stolenUOMQty;
	}

	public final Date getStolenDate() {
		return stolenDate;
	}

	public final void setStolenDate(Date stolenDate) {
		this.stolenDate = stolenDate;
	}

	public String getWarehouseName() {
		return warehouseBO.getWarehouseName();
	}

	public String getItemName() {
		return itemBO.getItemName();
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
