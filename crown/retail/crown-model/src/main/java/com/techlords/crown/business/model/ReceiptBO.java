package com.techlords.crown.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.techlords.infra.AppModel;
import com.techlords.infra.CrownConstants;

/**
 * The persistent class for the receipt database table.
 * 
 */
@SuppressWarnings("serial")
public class ReceiptBO extends AppModel {
	private String receiptNumber;
	private double amount;
	private double usedAmount;
	/* FU - Fully Used, PU - Partially Used, UU - UnUsed */
	private String usedState = CrownConstants.RECEIPT_UNUSED;
	private Date receiptDate;
	private boolean issued;

	private int customer;
	private CustomerBO customerBO;

	private int company;
	private CompanyBO companyBO;
	
	private String remarks;

	private final List<ReceiptPaymentBO> payments = new ArrayList<ReceiptPaymentBO>();

	public void addPayment(ReceiptPaymentBO pmt) {
		payments.add(pmt);
	}

	public void removePayment(ReceiptPaymentBO pmt) {
		payments.remove(pmt);
	}

	public final String getReceiptNumber() {
		return receiptNumber;
	}

	public final void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public final double getAmount() {
		amount = 0;
		for (ReceiptPaymentBO bo : payments) {
			amount += bo.getAmount();
		}
		return amount;
	}

	public final void setAmount(double amount) {
		throw new IllegalArgumentException("You cannot set amount for Receipt");
	}

	public final Date getReceiptDate() {
		return receiptDate;
	}

	public final void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public final int getCustomer() {
		return customer;
	}

	public final void setCustomer(int customer) {
		this.customer = customer;
	}

	public final CustomerBO getCustomerBO() {
		return customerBO;
	}

	public final void setCustomerBO(CustomerBO customerBO) {
		this.customerBO = customerBO;
	}

	public final int getCompany() {
		return company;
	}

	public final void setCompany(int company) {
		this.company = company;
	}

	public final CompanyBO getCompanyBO() {
		return companyBO;
	}

	public final void setCompanyBO(CompanyBO companyBO) {
		this.companyBO = companyBO;
	}

	public final List<ReceiptPaymentBO> getPayments() {
		return payments;
	}

	public final boolean isIssued() {
		return issued;
	}

	public final void setIssued(boolean issued) {
		this.issued = issued;
	}

	public final double getUsedAmount() {
		return usedAmount;
	}

	public final void setUsedAmount(double usedAmount) {
		this.usedAmount = usedAmount;
	}

	public final String getUsedState() {
		return usedState;
	}

	public final void setUsedState(String usedState) {
		this.usedState = usedState;
	}

	public final String getRemarks() {
		return remarks;
	}

	public final void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}