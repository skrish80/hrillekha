package com.techlords.crown.business.model;

import javax.validation.constraints.Min;

import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class StockMovementItemBO extends AppModel {

	@Min(value = 1, message = "Please enter \"Move Quantity\"")
	private Integer movedQty = 0;
	private Integer receivedQty = 0;
	private Integer returnedQty = 0;

	@Min(value = 1, message = "Please select \"Move Status\"")
	private int item;
	private ItemBO itemBO;
	@Min(value = 1, message = "Please select \"Move Type\"")
	private int allocationType;
	private AllocationTypeBO allocationTypeBO;

	public final Integer getMovedQty() {
		return movedQty;
	}

	public final void setMovedQty(Integer movedQty) {
		this.movedQty = movedQty;
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

	public final Integer getReturnedQty() {
		return returnedQty;
	}

	public final void setReturnedQty(Integer returnedQty) {
		this.returnedQty = returnedQty;
	}
}
