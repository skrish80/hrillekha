package com.techlords.crown.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the stock_movement_items database table.
 * 
 */
@Entity
@Table(name = "stock_movement_items")
public class StockMovementItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private StockMovementItemPK id;

	@Column(name = "moved_qty")
	private Integer movedQty = 0;

	@Column(name = "received_qty")
	private Integer receivedQty = 0;

	@Column(name = "returned_qty")
	private Integer returnedQty = 0;

	// bi-directional many-to-one association to AllocationType
	@ManyToOne
	@JoinColumn(name = "allocation_type", nullable = false)
	private AllocationType allocationTypeBean;

	// bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false, insertable = false, updatable = false)
	private Item item;

	// bi-directional many-to-one association to StockMovement
	@ManyToOne
	@JoinColumn(name = "movement_id", nullable = false, insertable = false, updatable = false)
	private StockMovement stockMovement;

	public StockMovementItem() {
	}

	public StockMovementItemPK getId() {
		return this.id;
	}

	public void setId(StockMovementItemPK id) {
		this.id = id;
	}

	public Integer getMovedQty() {
		return this.movedQty;
	}

	public void setMovedQty(Integer movedQty) {
		this.movedQty = movedQty;
	}

	public Integer getReceivedQty() {
		return this.receivedQty;
	}

	public void setReceivedQty(Integer receivedQty) {
		this.receivedQty = receivedQty;
	}

	public Integer getReturnedQty() {
		return this.returnedQty;
	}

	public void setReturnedQty(Integer returnedQty) {
		this.returnedQty = returnedQty;
	}

	public AllocationType getAllocationTypeBean() {
		return this.allocationTypeBean;
	}

	public void setAllocationTypeBean(AllocationType allocationTypeBean) {
		this.allocationTypeBean = allocationTypeBean;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public StockMovement getStockMovement() {
		return this.stockMovement;
	}

	public void setStockMovement(StockMovement stockMovement) {
		this.stockMovement = stockMovement;
	}

}