package com.techlords.crown.mvc.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.PurchaseInvoiceBO;
import com.techlords.crown.mvc.CrownLazyDataModel;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.service.PurchaseInvoiceService;

@SuppressWarnings("serial")
public class LazyPurchaseInvoiceDataModel extends
		CrownLazyDataModel<PurchaseInvoiceBO> {
	private final PurchaseInvoiceService service = CrownServiceLocator.INSTANCE
			.getCrownService(PurchaseInvoiceService.class);
	final List<PurchaseInvoiceBO> returnValue = new ArrayList<PurchaseInvoiceBO>();

	private String invoiceState;


	/** (non-Javadoc)
	 * @see org.primefaces.model.LazyDataModel#load(int, int, java.lang.String, org.primefaces.model.SortOrder, java.util.Map)
	 */
	@Override
	public List<PurchaseInvoiceBO> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		if (!FacesUtil.isRenderPhase()) {
			return returnValue;
		}
		boolean pageTraversal = isOnlyPageTraversal(filters);
		returnValue.clear();
		returnValue.addAll(pageTraversal ? service.findAllInvoices(first,
				pageSize, invoiceState) : service.findAllInvoices(first,
				pageSize, filters, invoiceState));
		setRowCount(service.getFilteredInvoiceCount());

		return returnValue;
	}

	public final String getInvoiceState() {
		return invoiceState;
	}

	public final void setInvoiceState(String invoiceState) {
		this.invoiceState = invoiceState;
	}
}
