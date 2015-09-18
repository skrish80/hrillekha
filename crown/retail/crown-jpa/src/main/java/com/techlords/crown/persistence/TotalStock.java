package com.techlords.crown.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the total_stock database table.
 * 
 */
@Entity
@Table(name = "total_stock")
public class TotalStock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "item_id")
	private Integer itemId;

	@Column(name = "item_qty")
	private Long itemQty = 0L;
	
	@Column(name = "uom")
	private Integer uom;

	@Column(name = "uom_qty")
	private Long uomQty = 0L;

	public TotalStock() {
	}

	public Integer getItemId() {
		return this.itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Long getItemQty() {
		return this.itemQty;
	}

	public void setItemQty(Long itemQty) {
		this.itemQty = itemQty;
	}

	public Integer getUom() {
		return this.uom;
	}

	public void setUom(Integer uom) {
		this.uom = uom;
	}

	public Long getUomQty() {
		return this.uomQty;
	}

	public void setUomQty(Long uomQty) {
		this.uomQty = uomQty;
	}

}