package com.techlords.crown.business.model;

import javax.validation.constraints.Min;

import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class PurchaseInvoiceItemBO extends AppModel {
	// @Min(value = 1, message = "Please enter \"Item Quantity\"")
	private Integer itemQty = 0;
	private Integer receivedQty = 0;

	@Min(value = 1, message = "Please select \"Item\"")
	private int item;
	private ItemBO itemBO;

	@Min(value = 1, message = "Please select \"Allocation Type\"")
	private int allocationType;
	private AllocationTypeBO allocationTypeBO;

	private double price;

	private String remarks;

	public final Integer getItemQty() {
		return itemQty;
	}

	public final void setItemQty(Integer itemQty) {
		this.itemQty = itemQty;
	}

	public final Integer getReceivedQty() {
		return receivedQty;
	}

	public final void setReceivedQty(Integer receivedQty) {
		this.receivedQty = receivedQty;
	}

	public final int getItem() {
		return item;
	}

	public final void setItem(int item) {
		this.item = item;
	}

	public final ItemBO getItemBO() {
		return itemBO;
	}

	public final void setItemBO(ItemBO itemBO) {
		this.itemBO = itemBO;
		if (itemBO != null) {
			setItem(itemBO.getId());
		}
	}

	public final int getAllocationType() {
		return allocationType;
	}

	public final void setAllocationType(int allocationType) {
		this.allocationType = allocationType;
	}

	public final AllocationTypeBO getAllocationTypeBO() {
		return allocationTypeBO;
	}

	public final void setAllocationTypeBO(AllocationTypeBO allocationTypeBO) {
		this.allocationTypeBO = allocationTypeBO;
	}

	public final double getPrice() {
		return price;
	}

	public final void setPrice(double amount) {
		this.price = amount;
	}

	public final String getRemarks() {
		return remarks;
	}

	public final void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public final double getAmount() {
		return itemQty * price;
	}
}
