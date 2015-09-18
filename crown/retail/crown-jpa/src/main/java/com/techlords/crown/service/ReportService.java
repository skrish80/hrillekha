package com.techlords.crown.service;

import java.util.List;

import com.techlords.crown.business.model.InvoiceDashboardBO;
import com.techlords.crown.business.model.TotalStockBO;


public interface ReportService extends CrownService {
	List<TotalStockBO> getReorderLevel();
	
	/**
	 * Gets list of Invoices for last week grouped by state
	 * 
	 * @return
	 */
	List<InvoiceDashboardBO> getInvoiceStateDashboard(String invoiceType);

	/**
	 * Gets list of Invoices for last week grouped by payment status
	 * 
	 * @return
	 */
	List<InvoiceDashboardBO> getInvoicePaymentDashboard(String invoiceType);
}
