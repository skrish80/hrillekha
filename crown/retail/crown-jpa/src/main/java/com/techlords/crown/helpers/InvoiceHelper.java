package com.techlords.crown.helpers;

import java.util.Date;
import java.util.Set;

import com.techlords.crown.business.model.CreditNoteBO;
import com.techlords.crown.business.model.InvoiceBO;
import com.techlords.crown.business.model.InvoiceItemBO;
import com.techlords.crown.business.model.InvoicePaymentBO;
import com.techlords.crown.business.model.InvoiceReturnBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.DiscountTypeBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.business.model.enums.PaymentModeBO;
import com.techlords.crown.business.model.enums.PaymentStatusBO;
import com.techlords.crown.persistence.Agent;
import com.techlords.crown.persistence.Bank;
import com.techlords.crown.persistence.Company;
import com.techlords.crown.persistence.CreditNote;
import com.techlords.crown.persistence.CrownEntity;
import com.techlords.crown.persistence.Currency;
import com.techlords.crown.persistence.Customer;
import com.techlords.crown.persistence.DiscountType;
import com.techlords.crown.persistence.Invoice;
import com.techlords.crown.persistence.InvoiceItem;
import com.techlords.crown.persistence.InvoiceItemPK;
import com.techlords.crown.persistence.InvoicePayment;
import com.techlords.crown.persistence.InvoiceReturn;
import com.techlords.crown.persistence.Item;
import com.techlords.crown.persistence.PaymentStatus;
import com.techlords.crown.persistence.Warehouse;

public final class InvoiceHelper {

	public Invoice createInvoice(final InvoiceBO bo) {
		return createInvoice(bo, null);
	}

	public Invoice createInvoice(final InvoiceBO bo, final Invoice toEdit) {
		final Invoice invoice = (toEdit == null) ? new Invoice() : toEdit;
		invoice.setVersion(bo.getVersion());
		final Date actionDate = new Date();
		switch (bo.getInvoiceStateBO()) {
		case NEW: {
			invoice.setInvoiceDate(actionDate);
			invoice.setInvoiceAmount(bo.getInvoiceAmount());
			invoice.setDiscountAmount(bo.getDiscountAmount());
			break;
		}
		case DELIVERED:
		case PARTIAL_DELIVERY:
			invoice.setDeliveredDate(actionDate);
			break;
		case RETURNED:
		case CANCELLED:
			invoice.setReturnDate(actionDate);
			invoice.setReturnAmount(invoice.getReturnAmount()
					+ bo.getReturnAmount());
			break;
		default:
			break;
		}

		invoice.setRemarks(bo.getRemarks());
		invoice.setTermsConditions(bo.getTermsConditions());
		invoice.setOtherPrice(bo.getOtherPriceTag());
		invoice.setOtherPriceAmount(bo.getOtherPriceAmount());
		invoice.setInvoiceType(bo.getInvoiceType());

		return invoice;
	}

	public InvoiceItem createInvoiceItem(int invoiceID, InvoiceItemBO bo) {
		final InvoiceItem item = new InvoiceItem();

		final InvoiceItemPK pk = new InvoiceItemPK();
		pk.setInvoiceId(invoiceID);
		pk.setItemId(bo.getItem());
		pk.setAllocationType(bo.getAllocationType());

		item.setId(pk);
		item.setItemQty(bo.getItemQty());
		item.setAmount(bo.getAmount());
		return item;
	}

	public InvoicePayment createInvoicePayment(int invoiceID,
			InvoicePaymentBO bo) {
		final InvoicePayment payment = new InvoicePayment();
		payment.setVersion(bo.getVersion());
		payment.setDraftNumber(bo.getDraftNumber());
		payment.setPayer(bo.getPayer());
		payment.setChequeDate(bo.getChequeDate());

		payment.setAmount(bo.getAmount());
		payment.setRemarks(bo.getRemarks());

		return payment;
	}

	public InvoiceBO createInvoiceBO(final Invoice invoice) {
		final InvoiceBO bo = new InvoiceBO();
		bo.setId(invoice.getInvoiceId());
		bo.setVersion(invoice.getVersion());
		bo.setInvoiceNumber(invoice.getInvoiceNumber());

		bo.setInvoiceDate(invoice.getInvoiceDate());
		bo.setDeliveryDate(invoice.getDeliveredDate());
		bo.setReturnDate(invoice.getReturnDate());

		bo.setInvoiceAmount(invoice.getInvoiceAmount());
		bo.setReturnAmount(invoice.getReturnAmount());
		bo.setDiscountAmount(invoice.getDiscountAmount());
		bo.setRemarks(invoice.getRemarks());

		bo.setRemarks(invoice.getRemarks());
		bo.setTermsConditions(invoice.getTermsConditions());
		bo.setOtherPriceTag(invoice.getOtherPrice());
		bo.setOtherPriceAmount(invoice.getOtherPriceAmount());
		bo.setInvoiceType(invoice.getInvoiceType());

		final int invoiceStatus = invoice.getInvoiceStateBean().getStateId();
		bo.setInvoiceState(invoiceStatus);
		bo.setInvoiceStateBO(InvoiceStateBO.valueOf(invoiceStatus));

		final WarehouseHelper warehouseHelper = new WarehouseHelper();
		final Warehouse deliveryWarehouse = invoice.getDeliveryWarehouse();
		if (deliveryWarehouse != null) {
			bo.setDeliveryWarehouse(deliveryWarehouse.getWarehouseId());
			bo.setDeliveryWarehouseBO(warehouseHelper
					.createWarehouseBO(deliveryWarehouse));
		}
		final GeneralHelper generalHelper = new GeneralHelper();
		final CrownEntity entity = invoice.getCrownEntity();
		bo.setEntity(entity.getEntityId());
		bo.setEntityBO(generalHelper.createCrownEntityBO(entity));

		final Agent agent = invoice.getAgent();
		if (agent != null) {
			bo.setAgent(agent.getAgentId());
			bo.setAgentBO(new AgentHelper().createAgentBO(agent));
		}

		final Customer customer = invoice.getCustomer();
		bo.setCustomer(customer.getCustomerId());
		bo.setCustomerBO(new CustomerHelper().createCustomerBO(customer));

		bo.setCreatedBy(generalHelper.getCrownUserBO(invoice.getCreatedBy()));

		final Company company = invoice.getCompany();
		bo.setCompany(company.getCompanyId());
		bo.setCompanyBO(new CompanyHelper().createCompanyBO(company));

		final Currency currency = invoice.getCurrencyBean();
		bo.setCurrencyBO(generalHelper.createCurrencyBO(currency));

		final DiscountType discountType = invoice.getDiscountTypeBean();
		if (discountType != null) {
			bo.setDiscountType(discountType.getDiscountTypeId());
			bo.setDiscountTypeBO(DiscountTypeBO.valueOf(discountType
					.getDiscountTypeId()));
		}

		final PaymentStatus paymentStatus = invoice.getPaymentStatusBean();
		bo.setPaymentStatus(paymentStatus.getPaymentStatusId());
		bo.setPaymentStatusBO(PaymentStatusBO.valueOf(paymentStatus
				.getPaymentStatusId()));

		Set<InvoiceItem> invoiceItems = invoice.getInvoiceItems();
		for (InvoiceItem invItem : invoiceItems) {
			bo.addInvoiceItem(createInvoiceItemBO(invItem));
		}

		Set<InvoicePayment> invoicePayments = invoice.getInvoicePayments();
		for (InvoicePayment invPmt : invoicePayments) {
			bo.addInvoicePayment(createInvoicePaymentBO(invPmt));
		}
		if (invoice.getInvoiceStateBean().getStateId() == InvoiceStateBO.RETURNED
				.getStateID()) {
			loadCreditNotes(bo, invoice);
			loadReturnItems(bo, invoice);
		}
		return bo;
	}

	private void loadReturnItems(InvoiceBO bo, Invoice invoice) {
		for (InvoiceReturn ret : invoice.getInvoiceReturns()) {
			bo.addInvoiceReturn(createInvoiceReturnBO(ret));
		}
	}

	public InvoiceReturn createInvoiceReturn(InvoiceReturnBO bo) {
		final InvoiceReturn ret = new InvoiceReturn();
		ret.setVersion(bo.getVersion());
		ret.setReturnQty(bo.getReturnQty());
		ret.setAmount(bo.getReturnAmount());
		ret.setRemarks(bo.getRemarks());
		ret.setReturnDate(new Date());
		return ret;
	}

	public InvoiceReturnBO createInvoiceReturnBO(InvoiceReturn ret) {
		final ItemHelper itmHelper = new ItemHelper();
		final InvoiceReturnBO bo = new InvoiceReturnBO();
		bo.setId(ret.getReturnId());
		bo.setVersion(ret.getVersion());

		bo.setInvoice(ret.getInvoice().getInvoiceId());

		Item item = ret.getItem();
		bo.setItem(item.getItemId());
		bo.setItemBO(itmHelper.createItemBO(item));

		bo.setReturnType(ret.getReturnType().getAllocationTypeId());
		bo.setReturnTypeBO(AllocationTypeBO.valueOf(bo.getReturnType()));

		bo.setReturnQty(ret.getReturnQty());
		bo.setReturnAmount(ret.getAmount());
		bo.setRemarks(ret.getRemarks());
		bo.setReturnDate(ret.getReturnDate());
		return bo;
	}

	public void loadCreditNotes(InvoiceBO bo, Invoice invoice) {
		for (CreditNote cn : invoice.getCreditNotes()) {
			CreditNoteBO cnBO = createCreditNoteBO(cn);
			cnBO.setInvoice(invoice.getInvoiceId());
			cnBO.setCustomer(invoice.getCustomer().getCustomerId());
			bo.addCreditNote(cnBO);
		}
	}

	public CreditNoteBO createCreditNoteBO(CreditNote note) {
		final CreditNoteBO bo = new CreditNoteBO();
		bo.setId(note.getCreditNoteId());
		bo.setVersion(note.getVersion());
		bo.setCreditNoteNumber(note.getNoteNumber());
		bo.setIssueDate(note.getIssueDate());
		bo.setAmount(note.getAmount());
		bo.setIssued(note.isIssued());
		bo.setRemarks(note.getRemarks());
		bo.setUtilized(note.isUtilized());
		return bo;
	}

	public CreditNote createCreditNote(CreditNoteBO bo, Invoice invoice) {
		final CreditNote note = new CreditNote();
		note.setCreditNoteId(bo.getId());
		note.setVersion(bo.getVersion());
		note.setIssueDate(new Date());
		note.setAmount(bo.getAmount());
		note.setInvoice(invoice);
		note.setCustomer(invoice.getCustomer());
		note.setRemarks(bo.getRemarks());
		return note;
	}

	public InvoiceItemBO createInvoiceItemBO(InvoiceItem item) {
		final InvoiceItemBO bo = new InvoiceItemBO();

		final InvoiceItemPK pk = item.getId();
		bo.setId(pk.getInvoiceId());
		bo.setItem(pk.getItemId());
		final ItemHelper itmHelper = new ItemHelper();
		bo.setItemBO(itmHelper.createItemBO(item.getItem()));

		bo.setAllocationType(pk.getAllocationType());

		bo.setAllocationTypeBO(AllocationTypeBO.valueOf(bo.getAllocationType()));

		bo.setItemQty(item.getItemQty());
		Integer delQty = item.getDeliveredQty();
		if (delQty == null) {
			delQty = 0;
		}
		// bo.setDeliveredQty(delQty);
		bo.setAlreadyDeliveredQty(delQty);

		bo.setAmount(item.getAmount());
		bo.setRemarks(item.getRemarks());

		return bo;
	}

	public InvoicePaymentBO createInvoicePaymentBO(InvoicePayment payment) {
		final InvoicePaymentBO bo = new InvoicePaymentBO();

		bo.setId(payment.getInvoice().getInvoiceId());
		bo.setVersion(payment.getVersion());
		bo.setPaymentMode(payment.getPaymentModeBean().getPaymentModeId());
		bo.setPaymentModeBO(PaymentModeBO.valueOf(bo.getPaymentMode()));

		bo.setDraftNumber(payment.getDraftNumber());
		bo.setChequeDate(payment.getChequeDate());
		bo.setPayer(payment.getPayer());

		bo.setAmount(payment.getAmount());
		bo.setRemarks(payment.getRemarks());

		// Get the bank
		final Bank bank = payment.getBank();
		if (bank != null) {
			bo.setBank(bank.getBankId());
			bo.setBankBO(new GeneralHelper().createBankBO(bank));
		}
		return bo;
	}
}
