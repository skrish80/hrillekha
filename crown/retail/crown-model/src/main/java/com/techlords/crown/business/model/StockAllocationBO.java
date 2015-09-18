package com.techlords.crown.business.model;

import java.util.Date;

import javax.validation.constraints.Min;

import com.techlords.crown.business.model.enums.AllocationStateBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public final class StockAllocationBO extends AppModel {

	private String allocationCode;
	private Date allocatedDate;
	@Min(value = 1, message = "Allocation quantity cannot be empty")
	private int allocatedQty;
	private Date releasedDate;

	@Min(value = 1, message = "Select an allocation state")
	private int allocationState;
	private AllocationStateBO allocationStateBO;
	@Min(value = 1, message = "Select an allocation type")
	private int allocationType;
	private AllocationTypeBO allocationTypeBO;
	@Min(value = 1, message = "Select an allocation entity")
	private int entity;
	private CrownEntityBO entityBO;
	@Min(value = 1, message = "Select an item to allocate")
	private int item;
	private ItemBO itemBO;

	public final String getAllocationCode() {
		return allocationCode;
	}

	public final void setAllocationCode(String allocationCode) {
		this.allocationCode = allocationCode;
	}

	public final Date getAllocatedDate() {
		return allocatedDate;
	}

	public final void setAllocatedDate(Date allocatedDate) {
		this.allocatedDate = allocatedDate;
	}

	public final int getAllocatedQty() {
		return allocatedQty;
	}

	public final void setAllocatedQty(int allocatedQty) {
		this.allocatedQty = allocatedQty;
	}

	public final Date getReleasedDate() {
		return releasedDate;
	}

	public final void setReleasedDate(Date releasedDate) {
		this.releasedDate = releasedDate;
	}

	public final int getAllocationState() {
		return allocationState;
	}

	public final void setAllocationState(int allocationState) {
		this.allocationState = allocationState;
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

	public final int getEntity() {
		return entity;
	}

	public final void setEntity(int entity) {
		this.entity = entity;
	}

	public final CrownEntityBO getEntityBO() {
		return entityBO;
	}

	public final void setEntityBO(CrownEntityBO entityBO) {
		this.entityBO = entityBO;
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

	public final AllocationStateBO getAllocationStateBO() {
		return allocationStateBO;
	}

	public final void setAllocationStateBO(AllocationStateBO allocationStateBO) {
		this.allocationStateBO = allocationStateBO;
	}
}
