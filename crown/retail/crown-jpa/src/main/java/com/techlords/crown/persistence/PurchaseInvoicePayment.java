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
 * The persistent class for the purchase_invoice_payment database table.
 * 
 */
@Entity
@Table(name = "purchase_invoice_payment")
@NamedQueries({ @NamedQuery(name = "PurchaseInvoicePayment.findByChequeNum", query = "select IP from PurchaseInvoicePayment IP where IP.draftNumber=?1") })
public class PurchaseInvoicePayment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PURCHASE_INVOICE_PAYMENT_PAYMENTID_GENERATOR", sequenceName = "PURCHASE_INVOICE_PAYMENT_PAYMENT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PURCHASE_INVOICE_PAYMENT_PAYMENTID_GENERATOR")
	@Column(name = "payment_id", unique = true, nullable = false)
	private Integer paymentId;

	private double amount;

	@Column(name = "draft_number", length = 25)
	private String draftNumber;

	@Column(name="remarks", length=2147483647)
	private String remarks;

	// bi-directional many-to-one association to Bank
	@ManyToOne
	@JoinColumn(name = "bank_id")
	private Bank bank;

	// bi-directional many-to-one association to Invoice
	@ManyToOne
	@JoinColumn(name = "invoice_id", nullable = false)
	private PurchaseInvoice invoice;

	// bi-directional many-to-one association to PaymentMode
	@ManyToOne
	@JoinColumn(name = "payment_mode", nullable = false)
	private PaymentMode paymentModeBean;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "cheque_date")
	private Date chequeDate;

	public PurchaseInvoicePayment() {
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

	public PurchaseInvoice getInvoice() {
		return this.invoice;
	}

	public void setInvoice(PurchaseInvoice invoice) {
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