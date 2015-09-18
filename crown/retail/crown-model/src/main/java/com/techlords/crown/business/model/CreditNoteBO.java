package com.techlords.crown.business.model;

import java.util.Date;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public final class CreditNoteBO extends AppModel {
	private int invoice;
	private InvoiceBO invoiceBO;

	private int customer;
	private CustomerBO customerBO;

	private Date issueDate;

	@NotEmpty(message = "Remarks cannot be empty")
	private String remarks;

	private String creditNoteNumber;

	private boolean issued;

	private boolean utilized;

	@Min(value = 1, message = "\"Credit Note Amount\" cannot be zero")
	private double amount;

	public int getInvoice() {
		return invoice;
	}

	public void setInvoice(int invoice) {
		this.invoice = invoice;
	}

	public InvoiceBO getInvoiceBO() {
		return invoiceBO;
	}

	public void setInvoiceBO(InvoiceBO invoiceBO) {
		this.invoiceBO = invoiceBO;
	}

	public int getCustomer() {
		return customer;
	}

	public void setCustomer(int customer) {
		this.customer = customer;
	}

	public CustomerBO getCustomerBO() {
		return customerBO;
	}

	public void setCustomerBO(CustomerBO customerBO) {
		this.customerBO = customerBO;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
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

	public String getCreditNoteNumber() {
		return creditNoteNumber;
	}

	public void setCreditNoteNumber(String creditNoteNumber) {
		this.creditNoteNumber = creditNoteNumber;
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
}
