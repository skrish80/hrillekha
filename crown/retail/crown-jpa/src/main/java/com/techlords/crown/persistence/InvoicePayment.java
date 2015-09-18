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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the invoice_payment database table.
 * 
 */
@Entity
@Table(name = "invoice_payment")
@NamedQueries({ @NamedQuery(name = "InvoicePayment.findByChequeNum", query = "select IP from InvoicePayment IP where IP.draftNumber=?1") })
public class InvoicePayment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "INVOICE_PAYMENT_PAYMENTID_GENERATOR", sequenceName = "INVOICE_PAYMENT_PAYMENT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICE_PAYMENT_PAYMENTID_GENERATOR")
	@Column(name = "payment_id", unique = true, nullable = false)
	private Integer paymentId;

	private double amount;

	@Column(name = "draft_number", length = 25)
	private String draftNumber;

	@Column(length = 50)
	private String payer;

	@Column(name="remarks", length=2147483647)
	private String remarks;

	// bi-directional many-to-one association to Bank
	@ManyToOne
	@JoinColumn(name = "bank_id")
	private Bank bank;

	// bi-directional many-to-one association to Invoice
	@ManyToOne
	@JoinColumn(name = "invoice_id", nullable = false)
	private Invoice invoice;

	// bi-directional many-to-one association to PaymentMode
	@ManyToOne
	@JoinColumn(name = "payment_mode", nullable = false)
	private PaymentMode paymentModeBean;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "cheque_date")
	private Date chequeDate;

	public InvoicePayment() {
	}

	public Integer getPaymentId() {
		return this.paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDraftNumber() {
		return this.draftNumber;
	}

	public void setDraftNumber(String draftNumber) {
		this.draftNumber = draftNumber;
	}

	public String getPayer() {
		return this.payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Bank getBank() {
		return this.bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public Invoice getInvoice() {
		return this.invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public PaymentMode getPaymentModeBean() {
		return this.paymentModeBean;
	}

	public void setPaymentModeBean(PaymentMode paymentModeBean) {
		this.paymentModeBean = paymentModeBean;
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

	public final Date getChequeDate() {
		return chequeDate;
	}

	public final void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

}