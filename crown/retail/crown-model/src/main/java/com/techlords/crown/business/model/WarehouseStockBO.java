package com.techlords.crown.business.model;

import javax.validation.constraints.Min;

import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class WarehouseStockBO extends AppModel {

	@Min(value = 1, message = "Select an Item")
	private Integer itemID;
	@Min(value = 1, message = "Select a Warehouse")
	private Integer warehouseID;

	private int itemQty;
	private int uomQty;

	private int updItemQty;
	private int updUomQty;

	private ItemBO itemBO;
	private WarehouseBO warehouseBO;

	/**
	 * @return the itemID
	 */
	public final Integer getItemID() {
		return itemID;
	}

	/**
	 * @param itemID
	 *            the itemID to set
	 */
	public final void setItemID(Integer itemID) {
		this.itemID = itemID;
	}

	/**
	 * @return the warehouseID
	 */
	public final Integer getWarehouseID() {
		return warehouseID;
	}

	/**
	 * @param warehouseID
	 *            the warehouseID to set
	 */
	public final void setWarehouseID(Integer warehouseID) {
		this.warehouseID = warehouseID;
	}

	public final boolean isNew() {
		return ((itemID == null) || (warehouseID == null))
				|| ((itemID == -1) || (warehouseID == -1));
	}

	/**
	 * @return the itemQty
	 */
	public final int getItemQty() {
		return itemQty;
	}

	/**
	 * @param itemQty
	 *            the itemQty to set
	 */
	public final void setItemQty(int itemQty) {
		this.itemQty = itemQty;
	}

	/**
	 * @return the uomQty
	 */
	public final int getUomQty() {
		return uomQty;
	}

	/**
	 * @param uomQty
	 *            the uomQty to set
	 */
	public final void setUomQty(int uomQty) {
		this.uomQty = uomQty;
	}

	/**
	 * @return the itemBO
	 */
	public final ItemBO getItemBO() {
		return itemBO;
	}

	/**
	 * @param itemBO
	 *            the itemBO to set
	 */
	public final void setItemBO(ItemBO itemBO) {
		this.itemBO = itemBO;
	}

	/**
	 * @return the warehouseBO
	 */
	public final WarehouseBO getWarehouseBO() {
		return warehouseBO;
	}

	/**
	 * @param warehouseBO
	 *            the warehouseBO to set
	 */
	public final void setWarehouseBO(WarehouseBO warehouseBO) {
		this.warehouseBO = warehouseBO;
	}

	public String getWarehouseName() {
		return warehouseBO.getWarehouseName();
	}

	public String getItemName() {
		return itemBO.getItemName();
	}

	public int getUpdItemQty() {
		return updItemQty;
	}

	public void setUpdItemQty(int updItemQty) {
		this.updItemQty = updItemQty;
	}

	public int getUpdUomQty() {
		return updUomQty;
	}

	public void setUpdUomQty(int updUomQty) {
		this.updUomQty = updUomQty;
	}
}
