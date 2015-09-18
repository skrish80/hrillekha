/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.techlords.crown.menu;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;

import com.techlords.infra.CrownConstants;

/**
 * 
 * @author gv
 */
@SuppressWarnings("serial")
@ManagedBean(name = "invoiceCarouselMenu")
public class InvoiceCarouselMenu implements Serializable {
	private final List<InvoiceMenu> wholesaleMenus;
	private final List<InvoiceMenu> retailMenus;
	private static final String WHOLESALE_INVOICE_TYPE = CrownConstants.WHOLESALE;
	private static final String RETAIL_INVOICE_TYPE = CrownConstants.RETAIL;

	public InvoiceCarouselMenu() {
		wholesaleMenus = new CrownMenuList<InvoiceMenu>();
		retailMenus = new CrownMenuList<InvoiceMenu>();

//		populateWholesaleMenus();
		populateRetailMenus();
	}

	@SuppressWarnings("unused")
	private void populateWholesaleMenus() {
		wholesaleMenus.add(new InvoiceMenu("/images/invoice/sales-invoice.PNG",
				"invoice/LocalWSInvoices.xhtml", "Sales Invoice", "SALES",
				WHOLESALE_INVOICE_TYPE));
		wholesaleMenus.add(new InvoiceMenu(
				"/images/invoice/purchase-invoice.PNG",
				"invoice/AllWSInvoices.xhtml", "Amend Payment", "PAY",
				WHOLESALE_INVOICE_TYPE));
		wholesaleMenus.add(new InvoiceMenu(
				"/images/invoice/deliver-invoice.PNG",
				"invoice/AllWSInvoices.xhtml", "Deliver Invoice", "DELIVER",
				WHOLESALE_INVOICE_TYPE));
		wholesaleMenus.add(new InvoiceMenu("/images/invoice/credit-note.PNG",
				"invoice/CreditNoteWSInvoices.xhtml", "Credit Note",
				"CREDIT_NOTE", WHOLESALE_INVOICE_TYPE));
		wholesaleMenus.add(new InvoiceMenu(
				"/images/invoice/return-invoice.PNG",
				"invoice/AllWSInvoices.xhtml", "Return Invoice", "RETURN",
				WHOLESALE_INVOICE_TYPE));
		wholesaleMenus.add(new InvoiceMenu("/images/invoice/Wholesale.jpg",
				"invoice/AllWSInvoices.xhtml", "All Invoices", "PRINT",
				WHOLESALE_INVOICE_TYPE));
	}

	private void populateRetailMenus() {
		retailMenus.add(new InvoiceMenu("/images/invoice/RT_Sales.jpg",
				"invoice/LocalRTInvoices.xhtml", "Sales Invoice", "SALES",
				RETAIL_INVOICE_TYPE));
		retailMenus.add(new InvoiceMenu("/images/invoice/deliver-invoice.PNG",
				"invoice/AllRTInvoices.xhtml", "Deliver Invoice", "DELIVER",
				RETAIL_INVOICE_TYPE));
		retailMenus.add(new InvoiceMenu("/images/invoice/credit-note.PNG",
				"invoice/CreditNoteRTInvoices.xhtml", "Credit Note",
				"CREDIT_NOTE", RETAIL_INVOICE_TYPE));
		retailMenus.add(new InvoiceMenu("/images/invoice/return-invoice.PNG",
				"invoice/AllRTInvoices.xhtml", "Return Invoice", "RETURN",
				RETAIL_INVOICE_TYPE));
		retailMenus.add(new InvoiceMenu("/images/invoice/Retail.jpg",
				"invoice/AllRTInvoices.xhtml", "All Invoices", "PRINT",
				RETAIL_INVOICE_TYPE));
	}

	/**
	 * @return the orderViews
	 */
	public List<InvoiceMenu> getWholesaleMenus() {
		return wholesaleMenus;
	}

	public final List<InvoiceMenu> getRetailMenus() {
		return retailMenus;
	}
}
