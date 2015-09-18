package com.techlords.crown.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the purchase_invoice_items database table.
 * 
 */
@Entity
@Table(name = "purchase_invoice_items")
public class PurchaseInvoiceItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PurchaseInvoiceItemPK id;

	private double price;

	@Column(name = "item_qty")
	private Integer itemQty = 0;

	@Column(name = "received_qty")
	private Integer receivedQty;

	@Column(name = "remarks", length = 2147483647)
	private String remarks;

	// bi-directional many-to-one association to PurchaseInvoice
	@ManyToOne
	@JoinColumn(name = "invoice_id", nullable = false, insertable = false, updatable = false)
	private PurchaseInvoice purchaseInvoice;
	
	// bi-directional many-to-one association to AllocationType
	@ManyToOne
	@JoinColumn(name = "allocation_type", nullable = false, insertable = false, updatable = false)
	private AllocationType allocationTypeBean;

	// bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false, insertable = false, updatable = false)
	private Item item;

	public PurchaseInvoiceItem() {
	}

	public PurchaseInvoiceItemPK getId() {
		return this.id;
	}

	public void setId(PurchaseInvoiceItemPK id) {
		this.id = id;
	}


	public Integer getItemQty() {
		return this.itemQty;
	}

	public void setItemQty(Integer itemQty) {
		this.itemQty = itemQty;
	}

	public Integer getReceivedQty() {
		return this.receivedQty;
	}

	public void setReceivedQty(Integer receivedQty) {
		this.receivedQty = receivedQty;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public PurchaseInvoice getPurchaseInvoice() {
		return this.purchaseInvoice;
	}

	public void setPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		this.purchaseInvoice = purchaseInvoice;
	}

	public final AllocationType getAllocationTypeBean() {
		return allocationTypeBean;
	}

	public final void setAllocationTypeBean(AllocationType allocationTypeBean) {
		this.allocationTypeBean = allocationTypeBean;
	}

	public final Item getItem() {
		return item;
	}

	public final void setItem(Item item) {
		this.item = item;
	}

	public final double getPrice() {
		return price;
	}

	public final void setPrice(double price) {
		this.price = price;
	}

}