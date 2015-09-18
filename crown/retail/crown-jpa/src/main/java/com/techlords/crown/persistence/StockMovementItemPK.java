package com.techlords.crown.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the stock_movement_items database table.
 * 
 */
@Embeddable
public class StockMovementItemPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "movement_id", unique = true, nullable = false)
	private Integer movementId;

	@Column(name = "item_id", unique = true, nullable = false)
	private Integer itemId;

	public StockMovementItemPK() {
	}
	public Integer getMovementId() {
		return this.movementId;
	}
	public void setMovementId(Integer movementId) {
		this.movementId = movementId;
	}
	public Integer getItemId() {
		return this.itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof StockMovementItemPK)) {
			return false;
		}
		StockMovementItemPK castOther = (StockMovementItemPK) other;
		return 
			this.movementId.equals(castOther.movementId)
				&& this.itemId.equals(castOther.itemId);

	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.movementId.hashCode();
		hash = hash * prime + this.itemId.hashCode();

		return hash;
	}
}