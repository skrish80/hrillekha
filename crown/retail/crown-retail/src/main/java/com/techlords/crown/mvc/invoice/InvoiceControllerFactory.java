package com.techlords.crown.mvc.invoice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.techlords.crown.mvc.NavigationBean;
import com.techlords.crown.mvc.util.FacesUtil;

@ManagedBean
@SessionScoped
public class InvoiceControllerFactory {

	private String nextAction;
	private String invoiceType;

	private final Map<String, AbstractInvoiceController> CONTROLLER_MAP = new HashMap<String, AbstractInvoiceController>();
	private final NavigationBean navigationBean = FacesUtil
			.findNavigationBean();

	@PostConstruct
	public void initMap() {
		CONTROLLER_MAP.put("PRINT",
				FacesUtil.findInvoiceControllerBean("invoiceCommonController"));
		CONTROLLER_MAP.put("SALES",
				FacesUtil.findInvoiceControllerBean("invoiceCreateController"));
		CONTROLLER_MAP
				.put("DELIVER", FacesUtil
						.findInvoiceControllerBean("deliverInvoiceController"));
		CONTROLLER_MAP.put("RETURN",
				FacesUtil.findInvoiceControllerBean("returnInvoiceController"));
		// CONTROLLER_MAP.put("PURCHASE", FacesUtil
		// .findInvoiceControllerBean("purchaseInvoiceController"));
		CONTROLLER_MAP
				.put("PAY", FacesUtil
						.findInvoiceControllerBean("invoicePaymentController"));
		CONTROLLER_MAP.put("CREDIT_NOTE",
				FacesUtil.findInvoiceControllerBean("creditNoteController"));
	}

	public String getNextAction() {
		return nextAction;
	}

	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}

	public AbstractInvoiceController getInvoiceController() {
		return CONTROLLER_MAP.get(nextAction);
	}

	private void updateInvoiceTypes() {
		Collection<AbstractInvoiceController> controllers = CONTROLLER_MAP
				.values();
		for (AbstractInvoiceController ctlr : controllers) {
			ctlr.setInvoiceType(invoiceType);
		}
	}

	public void showView(String nextAction, String invoiceType) {
		System.err.println("Invoice Controller Factory Create View");
		long time1 = System.currentTimeMillis();
		setNextAction(nextAction);
		setInvoiceType(invoiceType);
		updateInvoiceTypes();
		// if (navigationBean.getNavigationUrl().equals(
		// "invoice/LocalInvoices.xhtml")) {
		// navigationBean.setNavigationUrl("invoice/LocalInvoices.xhtml");
		// return;
		// }
		String navURL = "invoice/All"+invoiceType+"Invoices.xhtml";

		if (nextAction.equals("CREDIT_NOTE")) {
			navURL = "invoice/CreditNote"+invoiceType+"Invoices.xhtml";
		} else if (nextAction.equals("SALES")) {
			navURL = "invoice/Local"+invoiceType+"Invoices.xhtml";
		}
		navigationBean.setNavigationUrl(navURL);
		long time2 = System.currentTimeMillis();
		System.err
				.println("Time Taken to show View ICF ::: " + (time2 - time1));
	}

	public final String getInvoiceType() {
		return invoiceType;
	}

	public final String getInvoiceTypeExpanded() {
		return invoiceType.equals("WS") ? "Wholesale" : "Retail";
	}

	public final void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

}
