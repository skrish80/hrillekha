package com.techlords.crown.business.model;

import javax.validation.constraints.Min;

import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class InvoiceItemBO extends AppModel {
	// @Min(value = 1, message = "Please enter \"Item Quantity\"")
	private Integer itemQty = 0;
	private Integer deliveredQty = 0;
	private Integer alreadyDeliveredQty = 0;
	private Integer returnedQty;

	@Min(value = 1, message = "Please select \"Item\"")
	private int item;
	private ItemBO itemBO;

	@Min(value = 1, message = "Please select \"Allocation Type\"")
	private int allocationType;
	private AllocationTypeBO allocationTypeBO;

	private int returnType;
	private AllocationTypeBO returnTypeBO;

	private double amount;
	private double returnAmount;

	private String remarks;

	public Integer getItemQty() {
		return itemQty;
	}

	public void setItemQty(Integer itemQty) {
		this.itemQty = itemQty;
	}

	public Integer getDeliveredQty() {
		return deliveredQty;
	}

	public void setDeliveredQty(Integer deliveredQty) {
		this.deliveredQty = deliveredQty;
	}

	public Integer getReturnedQty() {
		return returnedQty;
	}

	public void setReturnedQty(Integer returnedQty) {
		this.returnedQty = returnedQty;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public ItemBO getItemBO() {
		return itemBO;
	}

	public void setItemBO(ItemBO itemBO) {
		this.itemBO = itemBO;
		if (itemBO != null) {
			setItem(itemBO.getId());
		}
	}

	public int getAllocationType() {
		return allocationType;
	}

	public void setAllocationType(int allocationType) {
		this.allocationType = allocationType;
	}

	public AllocationTypeBO getAllocationTypeBO() {
		return allocationTypeBO;
	}

	public void setAllocationTypeBO(AllocationTypeBO allocationTypeBO) {
		this.allocationTypeBO = allocationTypeBO;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isAllocationTypeItem() {
		return allocationType == AllocationTypeBO.ITEM.getAllocationTypeID();
	}
	
	public double getPriceWithoutVAT() {
		return itemQty * ( (allocationType == AllocationTypeBO.ITEM.getAllocationTypeID()) ?
				itemBO.getItemPrice() : itemBO.getUomPrice()); 
	}

	public double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getReturnType() {
		return returnType;
	}

	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

	public AllocationTypeBO getReturnTypeBO() {
		return returnTypeBO;
	}

	public void setReturnTypeBO(AllocationTypeBO returnTypeBO) {
		this.returnTypeBO = returnTypeBO;
	}

	public final Integer getAlreadyDeliveredQty() {
		return alreadyDeliveredQty;
	}

	public final void setAlreadyDeliveredQty(Integer alreadyDeliveredQty) {
		this.alreadyDeliveredQty = alreadyDeliveredQty;
	}
}
