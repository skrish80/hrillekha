/**
 * 
 */
package com.techlords.crown.business.model;

import com.techlords.infra.AppModel;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
public class SearchCriteria extends AppModel {
	private Integer itemID;
	private Integer itemCategoryID;
	private Integer itemBrandID;
	private Integer crownEntityID;
	private Integer warehouseID;

	public final Integer getItemID() {
		return itemID;
	}

	public final void setItemID(Integer itemID) {
		this.itemID = itemID;
	}

	public final Integer getItemCategoryID() {
		return itemCategoryID;
	}

	public final void setItemCategoryID(Integer itemCategoryID) {
		this.itemCategoryID = itemCategoryID;
	}

	public final Integer getItemBrandID() {
		return itemBrandID;
	}

	public final void setItemBrandID(Integer itemBrandID) {
		this.itemBrandID = itemBrandID;
	}

	public final Integer getCrownEntityID() {
		return crownEntityID;
	}

	public final void setCrownEntityID(Integer crownEntityID) {
		this.crownEntityID = crownEntityID;
	}

	public final Integer getWarehouseID() {
		return warehouseID;
	}

	public final void setWarehouseID(Integer warehouseID) {
		this.warehouseID = warehouseID;
	}
}
