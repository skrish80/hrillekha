package com.techlords.crown.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the warehouse_stock database table.
 * 
 */
@Entity
@Table(name = "warehouse_stock")
public class WarehouseStock implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private WarehouseStockPK id;

	@Column(name = "item_quantity")
	private Integer itemQuantity = 0;

	@Column(name = "uom_quantity")
	private Integer uomQuantity = 0;

	// bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false, insertable = false, updatable = false)
	private Item item;

	// bi-directional many-to-one association to Warehouse
	@ManyToOne
	@JoinColumn(name = "warehouse_id", nullable = false, insertable = false, updatable = false)
	private Warehouse warehouse;

	public WarehouseStock() {
	}

	public WarehouseStockPK getId() {
		return this.id;
	}

	public void setId(WarehouseStockPK id) {
		this.id = id;
	}

	public Integer getItemQuantity() {
		return this.itemQuantity;
	}

	public void setItemQuantity(Integer itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public Integer getUomQuantity() {
		return this.uomQuantity;
	}

	public void setUomQuantity(Integer uomQuantity) {
		this.uomQuantity = uomQuantity;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Warehouse getWarehouse() {
		return this.warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

}