package com.techlords.crown.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.PaymentStatusBO;
import com.techlords.crown.business.model.enums.PurchaseInvoiceStateBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class PurchaseInvoiceBO extends AppModel {

	private String invoiceNumber;
	private Date invoiceDate;
	private Date receivedDate;
	@Min(value = 1, message = "\"Invoice Amount\" cannot be zero")
	private double invoiceAmount;

	@Min(value = 1, message = "\"Final Invoice Amount\" cannot be zero")
	private double finalInvoiceAmount;

	private String remarks;
	private String termsConditions;
	private PurchaseInvoiceStateBO invoiceState;
	private String goodsReceiptNumber;

	@Min(value = 1, message = "Please select \"Company\"")
	private int company;
	private CompanyBO companyBO;

	private CrownUserBO createdBy;
	private CrownUserBO receivedBy;

	@Min(value = 1, message = "Please select \"Supplier\"")
	private int supplier;
	private SupplierBO supplierBO;

	@NotEmpty(message = "Select a Currency")
	private String currency;

	@Size(min = 1, message = "Please add at least one item to order")
	private List<PurchaseInvoiceItemBO> invoiceItems = new ArrayList<PurchaseInvoiceItemBO>();

	@Min(value = 1, message = "Please select \"Payment Status\"")
	private int paymentStatus;
	private PaymentStatusBO paymentStatusBO;

	// CAN BE EMPTY IF IT IS CREDIT SALES
	// @Size(min = 1, message = "Please add at least payment to order")
	private List<PurchaseInvoicePaymentBO> invoicePayments = new ArrayList<PurchaseInvoicePaymentBO>();

	// @Min(value = 1, message = "\"Total Paid Amount\" cannot be zero")
	// CAN BE CREDIT SALES
	private double totalPaidAmount;

	public final void addInvoiceItem(PurchaseInvoiceItemBO itemBO) {
		invoiceItems.add(itemBO);
	}

	public final void removeInvoiceItem(PurchaseInvoiceItemBO itemBO) {
		invoiceItems.remove(itemBO);
	}

	public final SupplierBO getSupplierBO() {
		return supplierBO;
	}

	public final void setSupplierBO(SupplierBO supplierBO) {
		this.supplierBO = supplierBO;
		if (supplierBO != null) {
			setSupplier(supplierBO.getId());
		}
	}

	public double getAmountWithoutTax() {
		double amount = 0;
		for (final PurchaseInvoiceItemBO item : invoiceItems) {
			final ItemBO bo = item.getItemBO();
			final int allocationType = item.getAllocationType();
			final int itemQty = item.getItemQty();
			amount += (allocationType == AllocationTypeBO.UOM.getAllocationTypeID()) ? (itemQty * bo
					.getUomPrice()) : (itemQty * bo.getItemPrice());
		}
		return amount;
	}

	public double getTotalTax() {
		double tax = 0;
		for (final PurchaseInvoiceItemBO item : invoiceItems) {
			final ItemBO bo = item.getItemBO();
			final double vat = bo.getVat();
			final int allocationType = item.getAllocationType();
			final int itemQty = item.getItemQty();
			tax += (allocationType == AllocationTypeBO.UOM.getAllocationTypeID()) ? (itemQty * (bo
					.getUomPrice() * (vat / 100))) : (itemQty * (bo.getItemPrice() * (vat / 100)));
		}
		return tax;
	}

	public final int getSupplier() {
		return supplier;
	}

	public final void setSupplier(int supplier) {
		this.supplier = supplier;
	}

	public final String getInvoiceNumber() {
		return invoiceNumber;
	}

	public final void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public final Date getInvoiceDate() {
		return invoiceDate;
	}

	public final void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public final Date getReceivedDate() {
		return receivedDate;
	}

	public final void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public final double getInvoiceAmount() {
		return invoiceAmount;
	}

	public final void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public final double getFinalInvoiceAmount() {
		return finalInvoiceAmount;
	}

	public final void setFinalInvoiceAmount(double finalInvoiceAmount) {
		this.finalInvoiceAmount = finalInvoiceAmount;
	}

	public final String getRemarks() {
		return remarks;
	}

	public final void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public final String getTermsConditions() {
		return termsConditions;
	}

	public final void setTermsConditions(String termsConditions) {
		this.termsConditions = termsConditions;
	}

	public final PurchaseInvoiceStateBO getInvoiceState() {
		return invoiceState;
	}

	public final void setInvoiceState(PurchaseInvoiceStateBO invoiceState) {
		this.invoiceState = invoiceState;
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
		if (companyBO != null) {
			setCompany(companyBO.getId());
		}
	}

	public final CrownUserBO getCreatedBy() {
		return createdBy;
	}

	public final void setCreatedBy(CrownUserBO createdBy) {
		this.createdBy = createdBy;
	}

	public final CrownUserBO getReceivedBy() {
		return receivedBy;
	}

	public final void setReceivedBy(CrownUserBO receivedBy) {
		this.receivedBy = receivedBy;
	}

	public final String getCurrency() {
		return currency;
	}

	public final void setCurrency(String currencyBO) {
		this.currency = currencyBO;
	}

	public final List<PurchaseInvoiceItemBO> getInvoiceItems() {
		return invoiceItems;
	}

	public final void setInvoiceItems(List<PurchaseInvoiceItemBO> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}

	public final String getGoodsReceiptNumber() {
		return goodsReceiptNumber;
	}

	public final void setGoodsReceiptNumber(String goodsReceiptNumber) {
		this.goodsReceiptNumber = goodsReceiptNumber;
	}

	/**
	 * @return the paymentStatus
	 */
	public int getPaymentStatus() {
		return paymentStatus;
	}

	/**
	 * @param paymentStatus
	 *            the paymentStatus to set
	 */
	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	/**
	 * @return the paymentStatusBO
	 */
	public PaymentStatusBO getPaymentStatusBO() {
		return paymentStatusBO;
	}

	/**
	 * @param paymentStatusBO
	 *            the paymentStatusBO to set
	 */
	public void setPaymentStatusBO(PaymentStatusBO paymentStatusBO) {
		this.paymentStatusBO = paymentStatusBO;
	}

	/**
	 * @return the invoicePayments
	 */
	public List<PurchaseInvoicePaymentBO> getInvoicePayments() {
		return invoicePayments;
	}

	/**
	 * @param invoicePayments
	 *            the invoicePayments to set
	 */
	public void setInvoicePayments(List<PurchaseInvoicePaymentBO> invoicePayments) {
		this.invoicePayments = invoicePayments;
	}

	public final void addInvoicePayment(PurchaseInvoicePaymentBO itemBO) {
		invoicePayments.add(itemBO);
	}

	public final void removeInvoicePayment(PurchaseInvoicePaymentBO itemBO) {
		invoicePayments.remove(itemBO);
	}

	public final double getTotalPaidAmount() {
		totalPaidAmount = 0;
		for (PurchaseInvoicePaymentBO bo : invoicePayments) {
			totalPaidAmount += bo.getAmount();
		}
		return totalPaidAmount;
	}

}