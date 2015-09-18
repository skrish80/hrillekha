package com.techlords.crown.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the warehouse_stock database table.
 * 
 */
@Embeddable
public class WarehouseStockPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "item_id", unique = true, nullable = false)
	private Integer itemId;

	@Column(name = "warehouse_id", unique = true, nullable = false)
	private Integer warehouseId;

	public WarehouseStockPK() {
	}
	public Integer getItemId() {
		return this.itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getWarehouseId() {
		return this.warehouseId;
	}
	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof WarehouseStockPK)) {
			return false;
		}
		WarehouseStockPK castOther = (WarehouseStockPK) other;
		return 
			this.itemId.equals(castOther.itemId)
				&& this.warehouseId.equals(castOther.warehouseId);

	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.itemId.hashCode();
		hash = hash * prime + this.warehouseId.hashCode();

		return hash;
	}
}