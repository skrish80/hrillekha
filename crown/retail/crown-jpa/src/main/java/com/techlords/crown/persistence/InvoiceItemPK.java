package com.techlords.crown.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the invoice_items database table.
 * 
 */
@Embeddable
public class InvoiceItemPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "invoice_id", unique = true, nullable = false)
	private Integer invoiceId;

	@Column(name = "item_id", unique = true, nullable = false)
	private Integer itemId;

	@Column(name = "allocation_type", unique = true, nullable = false)
	private Integer allocationType;

	public InvoiceItemPK() {
	}

	public Integer getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getItemId() {
		return this.itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getAllocationType() {
		return this.allocationType;
	}

	public void setAllocationType(Integer allocationType) {
		this.allocationType = allocationType;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof InvoiceItemPK)) {
			return false;
		}
		InvoiceItemPK castOther = (InvoiceItemPK) other;
		return this.invoiceId.equals(castOther.invoiceId)
				&& this.itemId.equals(castOther.itemId)
				&& this.allocationType.equals(castOther.allocationType);

	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.invoiceId.hashCode();
		hash = hash * prime + this.itemId.hashCode();
		hash = hash * prime + this.allocationType.hashCode();

		return hash;
	}
}