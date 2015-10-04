/**
 * 
 */
package com.techlords.crown.helpers;

import java.util.Date;
import java.util.Set;

import com.techlords.crown.business.model.PurchaseInvoiceBO;
import com.techlords.crown.business.model.PurchaseInvoiceItemBO;
import com.techlords.crown.business.model.PurchaseInvoicePaymentBO;
import com.techlords.crown.business.model.SupplierBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.PurchaseInvoiceStateBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.persistence.Company;
import com.techlords.crown.persistence.PurchaseInvoice;
import com.techlords.crown.persistence.PurchaseInvoiceItem;
import com.techlords.crown.persistence.PurchaseInvoiceItemPK;
import com.techlords.crown.persistence.PurchaseInvoicePayment;
import com.techlords.crown.persistence.Supplier;

/**
 * @author gv
 * 
 */
public final class PurchaseInvoiceHelper {

	public SupplierBO createSupplierBO(Supplier supplier) {
		final SupplierBO bo = new SupplierBO();
		bo.setId(supplier.getSupplierId());
		bo.setVersion(supplier.getVersion());
		bo.setSupplierCode(supplier.getSupplierCode());
		bo.setSupplierName(supplier.getSupplierName());
		bo.setAddress(supplier.getAddress());
		bo.setPhone(supplier.getPhone());
		bo.setPoc(supplier.getPoc());
		bo.setTin(supplier.getTin());
		bo.setRemarks(supplier.getRemarks());

		bo.setStatusBO(StatusBO.valueOf(supplier.getStatusBean().getStatusId()));

		return bo;
	}

	public Supplier createSupplier(SupplierBO bo) {
		return createSupplier(bo, null);
	}

	public Supplier createSupplier(SupplierBO bo, Supplier toEdit) {
		Supplier supplier = (toEdit == null) ? new Supplier() : toEdit;
		supplier.setVersion(bo.getVersion());
		supplier.setSupplierName(bo.getSupplierName());
		supplier.setAddress(bo.getAddress());
		supplier.setPhone(bo.getPhone());
		supplier.setPoc(bo.getPoc());
		supplier.setTin(bo.getTin());
		supplier.setRemarks(bo.getRemarks());
		return supplier;
	}

	public PurchaseInvoice createInvoice(PurchaseInvoiceBO bo) {
		return createInvoice(bo, null);
	}

	public PurchaseInvoice createInvoice(PurchaseInvoiceBO bo,
			PurchaseInvoice toEdit) {
		final PurchaseInvoice invoice = (toEdit == null) ? new PurchaseInvoice()
				: toEdit;
		invoice.setVersion(bo.getVersion());
		final Date actionDate = new Date();
		PurchaseInvoiceStateBO state = bo.getInvoiceState();
		switch (state) {
		case NEW:
			invoice.setInvoiceDate(actionDate);
			invoice.setInvoiceAmount(bo.getInvoiceAmount());
			invoice.setTermsConditions(bo.getTermsConditions());
			break;
		case RECEIVED:
			invoice.setReceivedDate(actionDate);
			invoice.setGoodsReceiptNumber(bo.getGoodsReceiptNumber());
			break;
		default:
			break;
		}

		invoice.setInvoiceState(state.getStateCode());
		invoice.setRemarks(bo.getRemarks());
		return invoice;
	}

	public PurchaseInvoiceItem createInvoiceItem(Integer invoiceId,
			PurchaseInvoiceItemBO bo) {
		final PurchaseInvoiceItem item = new PurchaseInvoiceItem();

		final PurchaseInvoiceItemPK pk = new PurchaseInvoiceItemPK();
		pk.setInvoiceId(invoiceId);
		pk.setItemId(bo.getItem());
		pk.setAllocationType(bo.getAllocationType());

		item.setId(pk);
		item.setItemQty(bo.getItemQty());
		item.setPrice(bo.getPrice());
		return item;
	}

	public PurchaseInvoiceBO createInvoiceBO(PurchaseInvoice invoice) {
		final PurchaseInvoiceBO bo = new PurchaseInvoiceBO();
		bo.setId(invoice.getInvoiceId());
		bo.setVersion(invoice.getVersion());
		bo.setInvoiceNumber(invoice.getInvoiceNumber());
		bo.setInvoiceDate(invoice.getInvoiceDate());
		bo.setInvoiceAmount(invoice.getInvoiceAmount());
		bo.setTermsConditions(invoice.getTermsConditions());
		bo.setReceivedDate(invoice.getReceivedDate());
		bo.setGoodsReceiptNumber(invoice.getGoodsReceiptNumber());
		bo.setInvoiceState(PurchaseInvoiceStateBO.getValueOf(invoice
				.getInvoiceState()));
		bo.setRemarks(invoice.getRemarks());
		bo.setCurrency(invoice.getCurrencyBean().getCurrencyCode());

		final GeneralHelper generalHelper = new GeneralHelper();
		bo.setCreatedBy(generalHelper.getCrownUserBO(invoice.getCreatedBy()));
		bo.setReceivedBy(generalHelper.getCrownUserBO(invoice.getReceivedBy()));

		final Company company = invoice.getCompany();
		bo.setCompany(company.getCompanyId());
		bo.setCompanyBO(new CompanyHelper().createCompanyBO(company));

		final Supplier supp = invoice.getSupplier();
		bo.setSupplier(supp.getSupplierId());
		bo.setSupplierBO(createSupplierBO(supp));

		final Set<PurchaseInvoiceItem> items = invoice
				.getPurchaseInvoiceItems();
		for (final PurchaseInvoiceItem item : items) {
			bo.addInvoiceItem(createInvoiceItemBO(item));
		}
		return bo;
	}

	public PurchaseInvoiceItemBO createInvoiceItemBO(PurchaseInvoiceItem item) {
		final PurchaseInvoiceItemBO bo = new PurchaseInvoiceItemBO();

		final PurchaseInvoiceItemPK pk = item.getId();
		bo.setId(pk.getInvoiceId());
		bo.setItem(pk.getItemId());
		final ItemHelper itmHelper = new ItemHelper();
		bo.setItemBO(itmHelper.createItemBO(item.getItem()));

		bo.setAllocationType(pk.getAllocationType());

		bo.setAllocationTypeBO(AllocationTypeBO.valueOf(bo.getAllocationType()));

		bo.setItemQty(item.getItemQty());
		bo.setReceivedQty(item.getReceivedQty());

		bo.setPrice(item.getPrice());
		bo.setRemarks(item.getRemarks());

		return bo;
	}

	/**
	 * @param invoiceId
	 * @param paymentBO
	 * @return
	 */
	public PurchaseInvoicePayment createInvoicePayment(Integer invoiceId,
			PurchaseInvoicePaymentBO bo) {
		final PurchaseInvoicePayment payment = new PurchaseInvoicePayment();
		payment.setVersion(bo.getVersion());
		payment.setDraftNumber(bo.getDraftNumber());
		payment.setChequeDate(bo.getChequeDate());

		payment.setAmount(bo.getAmount());
		payment.setRemarks(bo.getRemarks());

		return payment;
	}

}
