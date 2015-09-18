package com.techlords.crown.mvc.invoice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.InvoiceBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.business.model.enums.PaymentStatusBO;
import com.techlords.crown.mvc.CrownLazyDataModel;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.service.InvoiceService;
import com.techlords.infra.CrownConstants;

@SuppressWarnings("serial")
public class LazyInvoiceDataModel extends CrownLazyDataModel<InvoiceBO> {

	private final InvoiceService service = CrownServiceLocator.INSTANCE
			.getCrownService(InvoiceService.class);

	private Object[] invoiceStates;
	private boolean amendPayment;
	private String invoiceType;
	private boolean isReturn;
	private int entity;
	final List<InvoiceBO> returnValue = new ArrayList<InvoiceBO>();

	@Override
	public List<InvoiceBO> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		if (!FacesUtil.isRenderPhase()) {
			return returnValue;
		}
		boolean pageTraversal = isOnlyPageTraversal(filters);
		System.err.println("FILTERS ::: " + filters);
		if (invoiceType != null && invoiceType.equals(CrownConstants.RETAIL)) {
			final WarehouseBO wh = CrownUserDetailsService.getCurrentShop();
			if (wh != null) {
				setEntity(wh.getEntity());
			}
		}
		returnValue.clear();
		returnValue.addAll(pageTraversal ? service.findAllInvoices(
				getInvoiceType(), first, pageSize, amendPayment, entity,
				invoiceStates) : service.findAllInvoices(getInvoiceType(),
				first, pageSize, filters, amendPayment, entity, invoiceStates));

		// AVOID CREDIT SALES/PARTIAL PAID INVOICES FROM BEING RETURNED
		if (isReturn()) {
			Iterator<InvoiceBO> iter = returnValue.iterator();
			while (iter.hasNext()) {
				InvoiceBO invoice = iter.next();
				if (invoice.getPaymentStatus() == PaymentStatusBO.FULL_PAYMENT
						.getStatusID()) {
					continue;
				}
				iter.remove();
			}
		}
		setRowCount(service.getFilteredInvoiceCount());
		return returnValue;
	}

	public final Object[] getInvoiceStates() {
		return invoiceStates;
	}

	public final void setInvoiceStates(Object... invoiceStates) {
		this.invoiceStates = invoiceStates;
	}

	public final boolean isAmendPayment() {
		return amendPayment;
	}

	public final void setAmendPayment(boolean amendPayment) {
		this.amendPayment = amendPayment;
	}

	public final String getInvoiceType() {
		return invoiceType;
	}

	public final void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public final boolean isReturn() {
		return isReturn;
	}

	public final void setReturn(boolean isReturn) {
		this.isReturn = isReturn;
	}

	public final int getEntity() {
		return entity;
	}

	public final void setEntity(int entity) {
		this.entity = entity;
	}
}
