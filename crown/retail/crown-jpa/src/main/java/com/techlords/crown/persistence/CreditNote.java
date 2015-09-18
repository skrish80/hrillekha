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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the credit_note database table.
 * 
 */
@Entity
@Table(name = "credit_note")
@NamedQuery(name = "CreditNote.findByNumber", query = "select CN from CreditNote CN where CN.noteNumber = ?1")
public class CreditNote implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CREDIT_NOTE_CREDITNOTEID_GENERATOR", sequenceName = "CREDIT_NOTE_CREDIT_NOTE_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREDIT_NOTE_CREDITNOTEID_GENERATOR")
	@Column(name = "credit_note_id", unique = true, nullable = false)
	private Integer creditNoteId;

	@Column(name = "note_number", length = 25)
	private String noteNumber;

	@Column(name = "issued")
	private boolean issued;

	@Column(name = "utilized")
	private boolean utilized;

	@Temporal(TemporalType.DATE)
	@Column(name = "issue_date", nullable = false)
	private Date issueDate;

	@Column(name = "amount")
	private double amount;

	@Column(name = "remarks", length = 2147483647)
	private String remarks;

	// bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	// bi-directional many-to-one association to Invoice
	@ManyToOne
	@JoinColumn(name = "invoice_id", nullable = false)
	private Invoice invoice;

	public CreditNote() {
	}

	public Integer getCreditNoteId() {
		return this.creditNoteId;
	}

	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public Date getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Invoice getInvoice() {
		return this.invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getNoteNumber() {
		return noteNumber;
	}

	public void setNoteNumber(String noteNumber) {
		this.noteNumber = noteNumber;
	}

	public boolean isIssued() {
		return issued;
	}

	public void setIssued(boolean issued) {
		this.issued = issued;
	}

	public boolean isUtilized() {
		return utilized;
	}

	public void setUtilized(boolean utilized) {
		this.utilized = utilized;
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