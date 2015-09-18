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
 * The persistent class for the receipt_payment database table.
 * 
 */
@Entity
@Table(name = "receipt_payment")
@NamedQueries({ @NamedQuery(name = "ReceiptPayment.findByChequeNumber", query = "select R from ReceiptPayment R where R.chequeNumber=?1") })
public class ReceiptPayment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "RECEIPT_PAYMENT_PAYMENTID_GENERATOR", sequenceName = "RECEIPT_PAYMENT_PAYMENT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECEIPT_PAYMENT_PAYMENTID_GENERATOR")
	@Column(name = "payment_id")
	private Integer paymentId;

	@Column(name = "amount")
	private double amount;

	// bi-directional many-to-one association to Bank
	@ManyToOne
	@JoinColumn(name = "bank_id")
	private Bank bank;

	@Column(name = "cheque_number")
	private String chequeNumber;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "cheque_date")
	private Date chequeDate;

	@Column(name = "payer")
	private String payer;

	@ManyToOne
	@JoinColumn(name = "payment_mode", nullable = false)
	private PaymentMode paymentModeBean;

	// bi-directional many-to-one association to Receipt
	@ManyToOne
	@JoinColumn(name = "receipt_id")
	private Receipt receipt;

	public ReceiptPayment() {
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

	public String getChequeNumber() {
		return this.chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public String getPayer() {
		return this.payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public Receipt getReceipt() {
		return this.receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}

	public final PaymentMode getPaymentModeBean() {
		return paymentModeBean;
	}

	public final void setPaymentModeBean(PaymentMode paymentModeBean) {
		this.paymentModeBean = paymentModeBean;
	}

	public final Bank getBank() {
		return bank;
	}

	public final void setBank(Bank bank) {
		this.bank = bank;
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