package com.techlords.crown.business.model;

import java.util.Date;

import com.techlords.crown.business.model.enums.PaymentModeBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class PurchaseInvoicePaymentBO extends AppModel {

	private int paymentMode;
	private PaymentModeBO paymentModeBO;
	
	private boolean creditNoteAvl = true;

	private Integer bank;
	private BankBO bankBO;

	// Cheque or Card number
	private String draftNumber;
	private Date chequeDate = new Date();

	private String remarks;
	private double amount;

	public int getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(int paymentMode) {
		this.paymentMode = paymentMode;
	}

	public PaymentModeBO getPaymentModeBO() {
		return paymentModeBO;
	}

	public void setPaymentModeBO(PaymentModeBO paymentModeBO) {
		this.paymentModeBO = paymentModeBO;
	}

	public Integer getBank() {
		return bank;
	}

	public void setBank(Integer bank) {
		this.bank = bank;
	}

	public BankBO getBankBO() {
		return bankBO;
	}

	public void setBankBO(BankBO bankBO) {
		this.bankBO = bankBO;
	}

	public String getDraftNumber() {
		return draftNumber;
	}

	public void setDraftNumber(String draftNumber) {
		this.draftNumber = draftNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public final boolean isCreditNoteAvl() {
		return creditNoteAvl;
	}

	public final void setCreditNoteAvl(boolean creditNoteUtilized) {
		this.creditNoteAvl = creditNoteUtilized;
	}

	public final Date getChequeDate() {
		return chequeDate;
	}

	public final void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}
	
	public boolean isDraftNumberRequired() {
		return paymentMode > PaymentModeBO.CASH.getModeID() && paymentMode < PaymentModeBO.RECEIPT.getModeID();
	}
	
	public boolean isReceiptMode() {
		return paymentMode == PaymentModeBO.RECEIPT.getModeID();
	}
}
