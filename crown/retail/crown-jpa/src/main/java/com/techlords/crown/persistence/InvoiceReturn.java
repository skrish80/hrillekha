package com.techlords.crown.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the invoice_return database table.
 * 
 */
@Entity
@Table(name = "invoice_return")
public class InvoiceReturn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "INVOICE_RETURN_RETURNID_GENERATOR", sequenceName = "INVOICE_RETURN_RETURN_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICE_RETURN_RETURNID_GENERATOR")
	@Column(name = "return_id", unique = true, nullable = false)
	private Integer returnId;

	private double amount;

	@Column(length = 2147483647)
	private String remarks;

	@Column(name = "return_qty", nullable = false)
	private Integer returnQty = 0;

	@Temporal(TemporalType.DATE)
	@Column(name = "return_date", nullable = false)
	private Date returnDate;

	// bi-directional many-to-one association to AllocationType
	@ManyToOne
	@JoinColumn(name = "return_type", nullable = false)
	private AllocationType returnType;

	// bi-directional many-to-one association to Invoice
	@ManyToOne
	@JoinColumn(name = "invoice_id", nullable = false)
	private Invoice invoice;

	// bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	public InvoiceReturn() {
	}

	public Integer getReturnId() {
		return this.returnId;
	}

	public void setReturnId(Integer returnId) {
		this.returnId = returnId;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getReturnQty() {
		return this.returnQty;
	}

	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
	}

	public AllocationType getReturnType() {
		return this.returnType;
	}

	public void setReturnType(AllocationType allocationType) {
		this.returnType = allocationType;
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

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	
	@Version
	@Column(name = "version", unique = true, nullable = false)
	private long version;

	public final long getVersion() {
		return version;
	}

	public final void setVersion(long version) {
		this.version = version;
	}

}