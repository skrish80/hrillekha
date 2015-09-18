package com.techlords.crown.business.model;

import java.util.Date;

import com.techlords.crown.business.model.enums.PaymentModeBO;
import com.techlords.infra.AppModel;

/**
 * The persistent class for the receipt_payment database table.
 * 
 */
public class ReceiptPaymentBO extends AppModel {
	private static final long serialVersionUID = 1L;
	private int paymentMode;
	private PaymentModeBO paymentModeBO;

	private Integer bank;
	private BankBO bankBO;

	// Cheque or Card number
	private String chequeNumber;
	private Date chequeDate;
	private String payer;

	private double amount;

	public final int getPaymentMode() {
		return paymentMode;
	}

	public final void setPaymentMode(int paymentMode) {
		this.paymentMode = paymentMode;
	}

	public final PaymentModeBO getPaymentModeBO() {
		return paymentModeBO;
	}

	public final void setPaymentModeBO(PaymentModeBO paymentModeBO) {
		this.paymentModeBO = paymentModeBO;
	}

	public final Integer getBank() {
		return bank;
	}

	public final void setBank(Integer bank) {
		this.bank = bank;
	}

	public final BankBO getBankBO() {
		return bankBO;
	}

	public final void setBankBO(BankBO bankBO) {
		this.bankBO = bankBO;
	}

	public final String getChequeNumber() {
		return chequeNumber;
	}

	public final void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public final String getPayer() {
		return payer;
	}

	public final void setPayer(String payer) {
		this.payer = payer;
	}

	public final double getAmount() {
		return amount;
	}

	public final void setAmount(double amount) {
		this.amount = amount;
	}

	public final Date getChequeDate() {
		return chequeDate;
	}

	public final void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}
}