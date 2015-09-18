package com.techlords.crown.business.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.DiscountTypeBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.business.model.enums.PaymentStatusBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class InvoiceBO extends AppModel {

	private String invoiceNumber;
	private String invoiceType;
	private Date invoiceDate;
	@Min(value = 1, message = "\"Invoice Amount\" cannot be zero")
	private double invoiceAmount;

	private int discountType;
	private DiscountTypeBO discountTypeBO;
	private double discountAmount;

	@Min(value = 1, message = "\"Final Invoice Amount\" cannot be zero")
	private double finalInvoiceAmount;
	// @Min(value = 1, message = "\"Total Paid Amount\" cannot be zero")
	// CAN BE CREDIT SALES
	private double totalPaidAmount;

	private double returnAmount;
	private Date returnDate;
	private Date deliveryDate;
	private String remarks;

	private String termsConditions;
	private String otherPriceTag;
	private double otherPriceAmount;

	private int invoiceState;
	private InvoiceStateBO invoiceStateBO;

	@Min(value = 1, message = "Please select \"Entity\"")
	private int entity;
	private CrownEntityBO entityBO;

	@Min(value = 1, message = "Please select \"Company\"")
	private int company;
	private CompanyBO companyBO;

	private CrownUserBO createdBy;

	@Min(value = 1, message = "Please select \"Agent\"")
	private int agent;
	private AgentBO agentBO;

	@Min(value = 1, message = "Please select \"Customer\"")
	private int customer;
	private CustomerBO customerBO;

	@Min(value = 1, message = "Please select \"Payment Status\"")
	private int paymentStatus;
	private PaymentStatusBO paymentStatusBO;

	private int status;
	private StatusBO statusBO;

	private CurrencyBO currencyBO;

	private int deliveryWarehouse;
	private WarehouseBO deliveryWarehouseBO;

	@Size(min = 1, message = "Please add at least one item to order")
	private List<InvoiceItemBO> invoiceItems = new ArrayList<InvoiceItemBO>();

	// CAN BE EMPTY IF IT IS CREDIT SALES
	// @Size(min = 1, message = "Please add at least payment to order")
	private List<InvoicePaymentBO> invoicePayments = new ArrayList<InvoicePaymentBO>();

	private List<InvoiceReturnBO> invoiceReturns = new ArrayList<InvoiceReturnBO>();

	private List<CreditNoteBO> creditNotes = new ArrayList<CreditNoteBO>();

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

	public final double getInvoiceAmount() {
		return invoiceAmount;
	}

	public final void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public final int getDiscountType() {
		return discountType;
	}

	public final void setDiscountType(int discountType) {
		this.discountType = discountType;
	}

	public final double getDiscountAmount() {
		return discountAmount;
	}

	public final void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public final double getReturnAmount() {
		return returnAmount;
	}

	public final void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public final double getReturnAmtCumulative() {
		double cumulative = returnAmount;
		for (InvoiceReturnBO bo : invoiceReturns) {
			cumulative += bo.getReturnAmount();
		}
		return cumulative;
	}

	public final Date getReturnDate() {
		return returnDate;
	}

	public final void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public final int getInvoiceState() {
		return invoiceState;
	}

	public final void setInvoiceState(int invoiceState) {
		this.invoiceState = invoiceState;
	}

	public final InvoiceStateBO getInvoiceStateBO() {
		return invoiceStateBO;
	}

	public final void setInvoiceStateBO(InvoiceStateBO invoiceStateBO) {
		this.invoiceStateBO = invoiceStateBO;
	}

	public final int getEntity() {
		return entity;
	}

	public final void setEntity(int entity) {
		this.entity = entity;
	}

	public final CrownEntityBO getEntityBO() {
		return entityBO;
	}

	public final void setEntityBO(CrownEntityBO entityBO) {
		this.entityBO = entityBO;
		if (entityBO != null) {
			setEntity(entityBO.getId());
		}
	}

	public final int getAgent() {
		return agent;
	}

	public final void setAgent(int agent) {
		this.agent = agent;
	}

	public final AgentBO getAgentBO() {
		return agentBO;
	}

	public final void setAgentBO(AgentBO agentBO) {
		this.agentBO = agentBO;
		if (agentBO != null) {
			setAgent(agentBO.getId());
		}
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
		if (customerBO != null) {
			setCustomer(customerBO.getId());
		}
	}

	public final int getPaymentStatus() {
		return paymentStatus;
	}

	public final void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public final int getStatus() {
		return status;
	}

	public final void setStatus(int status) {
		this.status = status;
	}

	public final StatusBO getStatusBO() {
		return statusBO;
	}

	public final void setStatusBO(StatusBO statusBO) {
		this.statusBO = statusBO;
	}

	public final CurrencyBO getCurrencyBO() {
		return currencyBO;
	}

	public final void setCurrencyBO(CurrencyBO currencyBO) {
		this.currencyBO = currencyBO;
	}

	public final DiscountTypeBO getDiscountTypeBO() {
		return discountTypeBO;
	}

	public final void setDiscountTypeBO(DiscountTypeBO discountTypeBO) {
		this.discountTypeBO = discountTypeBO;
	}

	public final List<InvoiceItemBO> getInvoiceItems() {
		Collections.sort(invoiceItems, new Comparator<InvoiceItemBO>() {

			@Override
			public int compare(InvoiceItemBO o1, InvoiceItemBO o2) {
				final ItemBO bo1 = o1.getItemBO();
				final ItemBO bo2 = o2.getItemBO();
				if(bo1 != null && bo2 != null) {
					return bo1.getItemName().compareTo(bo2.getItemName());
				}
				return 0;
			}
		});
		return invoiceItems;
	}

	public final void setInvoiceItems(List<InvoiceItemBO> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}

	public final List<InvoicePaymentBO> getInvoicePayments() {
		return invoicePayments;
	}

	public final void setInvoicePayments(List<InvoicePaymentBO> invoicePayments) {
		this.invoicePayments = invoicePayments;
	}

	public final void addInvoiceItem(InvoiceItemBO itemBO) {
		invoiceItems.add(itemBO);
	}

	public final void removeInvoiceItem(InvoiceItemBO itemBO) {
		invoiceItems.remove(itemBO);
	}

	public final void addInvoicePayment(InvoicePaymentBO itemBO) {
		invoicePayments.add(itemBO);
	}

	public final void removeInvoicePayment(InvoicePaymentBO itemBO) {
		invoicePayments.remove(itemBO);
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void resetDiscount() {
		setDiscountAmount(0);
		setDiscountTypeBO(null);
		setDiscountType(0);
	}

	public double getFinalInvoiceAmount() {
		if (invoiceAmount <= 0) {
			resetDiscount();
			finalInvoiceAmount = 0;
			return finalInvoiceAmount;
		}
		final double totalTax = getTotalTax();
		final double totalAmountWithoutTax = getAmountWithoutTax();
		final DiscountTypeBO discountType = DiscountTypeBO
				.valueOf(getDiscountType());
		//Calculate the discount for Invoice Amount Excluding TAX
		if (DiscountTypeBO.PERCENTAGE.equals(discountType)) {
			finalInvoiceAmount = totalTax + (totalAmountWithoutTax
					- ((discountAmount / 100) * totalAmountWithoutTax));
		} else {
			finalInvoiceAmount = totalTax + (totalAmountWithoutTax - discountAmount);
		}
		return finalInvoiceAmount;
	}

	public PaymentStatusBO getPaymentStatusBO() {
		return paymentStatusBO;
	}

	public void setPaymentStatusBO(PaymentStatusBO paymentStatusBO) {
		this.paymentStatusBO = paymentStatusBO;
	}

	public int getDeliveryWarehouse() {
		return deliveryWarehouse;
	}

	public void setDeliveryWarehouse(int deliveryWarehouse) {
		this.deliveryWarehouse = deliveryWarehouse;
	}

	public WarehouseBO getDeliveryWarehouseBO() {
		return deliveryWarehouseBO;
	}

	public void setDeliveryWarehouseBO(WarehouseBO deliveryWarehouseBO) {
		this.deliveryWarehouseBO = deliveryWarehouseBO;
	}

	public void setFinalInvoiceAmount(double finalInvoiceAmount) {
		this.finalInvoiceAmount = finalInvoiceAmount;
	}

	public final double getTotalPaidAmount() {
		totalPaidAmount = 0;
		for (InvoicePaymentBO bo : invoicePayments) {
			totalPaidAmount += bo.getAmount();
		}
		return totalPaidAmount;
	}

	public final void setTotalPaidAmount(double totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}

	public List<CreditNoteBO> getCreditNotes() {
		return creditNotes;
	}

	public void setCreditNotes(List<CreditNoteBO> creditNotes) {
		this.creditNotes = creditNotes;
	}

	public void addCreditNote(CreditNoteBO creditNoteBO) {
		creditNotes.add(creditNoteBO);
	}

	public void removeCreditNote(CreditNoteBO creditNoteBO) {
		creditNotes.remove(creditNoteBO);
	}

	public double getCreditNoteAmount() {
		double creditNoteAmt = 0d;
		for (CreditNoteBO bo : creditNotes) {
			creditNoteAmt += bo.getAmount();
		}
		return creditNoteAmt;
	}

	public List<InvoiceReturnBO> getInvoiceReturns() {
		return invoiceReturns;
	}

	public void setInvoiceReturns(List<InvoiceReturnBO> invoiceReturns) {
		this.invoiceReturns = invoiceReturns;
	}

	public void addInvoiceReturn(InvoiceReturnBO invReturnBO) {
		invoiceReturns.add(invReturnBO);
	}

	public void removeInvoiceReturn(InvoiceReturnBO invReturnBO) {
		invoiceReturns.remove(invReturnBO);
	}

	public String getInvoiceStateName() {
		return invoiceStateBO.getStateName();
	}

	public String getPaymentStatusName() {
		return paymentStatusBO.getStatus();
	}

	public final String getTermsConditions() {
		return termsConditions;
	}

	public final void setTermsConditions(String termsConditions) {
		this.termsConditions = termsConditions;
	}

	public final String getOtherPriceTag() {
		return otherPriceTag;
	}

	public final void setOtherPriceTag(String otherPriceTag) {
		this.otherPriceTag = otherPriceTag;
	}

	public final double getOtherPriceAmount() {
		return otherPriceAmount;
	}

	public final void setOtherPriceAmount(double otherPriceAmount) {
		this.otherPriceAmount = otherPriceAmount;
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

	public final CrownUserBO getCreatedBy() {
		return createdBy;
	}

	public final void setCreatedBy(CrownUserBO createdBy) {
		this.createdBy = createdBy;
	}

	public final String getInvoiceType() {
		return invoiceType;
	}

	public final void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public double getAmountWithoutTax() {
		double amount = 0;
		for (final InvoiceItemBO item : invoiceItems) {
			final ItemBO bo = item.getItemBO();
			final int allocationType = item.getAllocationType();
			final int itemQty = item.getItemQty();
			amount += (allocationType == AllocationTypeBO.UOM
					.getAllocationTypeID()) ? (itemQty * bo.getUomPrice())
					: (itemQty * bo.getItemPrice());
		}
		return amount;
	}
	
	public double getTotalTax() {
		double tax = 0;
		for (final InvoiceItemBO item : invoiceItems) {
			final ItemBO bo = item.getItemBO();
			final double vat = bo.getVat();
			final int allocationType = item.getAllocationType();
			final int itemQty = item.getItemQty();
			tax += (allocationType == AllocationTypeBO.UOM
					.getAllocationTypeID()) ? (itemQty * (bo.getUomPrice() * (vat/100)))
					: (itemQty * (bo.getItemPrice() * (vat/100)));
		}
		return tax;
	}
	
	public int getTotalQuantity() {
		int qty = 0;
		for (final InvoiceItemBO item : invoiceItems) {
			qty += item.getItemQty();
		}
		return qty;
	}

}