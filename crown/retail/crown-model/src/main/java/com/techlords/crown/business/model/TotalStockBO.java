package com.techlords.crown.business.model;

import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public final class TotalStockBO extends AppModel {
	private Integer itemID;
	private ItemBO itemBO;
	private Integer uomID;

	private Long itemQty = 0L;
	private Long uomQty = 0L;

	public final Integer getItemID() {
		return itemID;
	}

	public final void setItemID(Integer itemID) {
		this.itemID = itemID;
	}

	public final Integer getUomID() {
		return uomID;
	}

	public final void setUomID(Integer uomID) {
		this.uomID = uomID;
	}

	public final Long getItemQty() {
		return itemQty;
	}

	public final void setItemQty(Long itemQty) {
		this.itemQty = itemQty;
	}

	public final Long getUomQty() {
		return uomQty;
	}

	public final void setUomQty(Long uomQty) {
		this.uomQty = uomQty;
	}

	public final ItemBO getItemBO() {
		return itemBO;
	}

	public final void setItemBO(ItemBO itemBO) {
		this.itemBO = itemBO;
	}
}
