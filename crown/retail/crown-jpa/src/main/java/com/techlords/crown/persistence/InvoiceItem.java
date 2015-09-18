package com.techlords.crown.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the invoice_items database table.
 * 
 */
@Entity
@Table(name = "invoice_items")
public class InvoiceItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private InvoiceItemPK id;

	private double amount;

	@Column(name = "delivered_qty")
	private Integer deliveredQty = 0;

	@Column(name = "item_qty")
	private Integer itemQty = 0;

	@Column(name="remarks", length=2147483647)
	private String remarks;

	// bi-directional many-to-one association to AllocationType
	@ManyToOne
	@JoinColumn(name = "allocation_type", nullable = false, insertable = false, updatable = false)
	private AllocationType allocationTypeBean;

	// bi-directional many-to-one association to Invoice
	@ManyToOne
	@JoinColumn(name = "invoice_id", nullable = false, insertable = false, updatable = false)
	private Invoice invoice;

	// bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false, insertable = false, updatable = false)
	private Item item;

	public InvoiceItem() {
	}

	public InvoiceItemPK getId() {
		return this.id;
	}

	public void setId(InvoiceItemPK id) {
		this.id = id;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Integer getDeliveredQty() {
		return this.deliveredQty;
	}

	public void setDeliveredQty(Integer deliveredQty) {
		this.deliveredQty = deliveredQty;
	}

	public Integer getItemQty() {
		return this.itemQty;
	}

	public void setItemQty(Integer itemQty) {
		this.itemQty = itemQty;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public AllocationType getAllocationTypeBean() {
		return this.allocationTypeBean;
	}

	public void setAllocationTypeBean(AllocationType allocationTypeBean) {
		this.allocationTypeBean = allocationTypeBean;
	}

	public Invoice getInvoice() {
		return this.invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

}