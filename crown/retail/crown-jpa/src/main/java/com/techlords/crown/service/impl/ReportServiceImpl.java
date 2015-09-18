/**
 * 
 */
package com.techlords.crown.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import com.techlords.crown.business.model.InvoiceDashboardBO;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.TotalStockBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.business.model.enums.PaymentStatusBO;
import com.techlords.crown.helpers.GeneralHelper;
import com.techlords.crown.persistence.Item;
import com.techlords.crown.persistence.TotalStock;
import com.techlords.crown.service.ReportService;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
final class ReportServiceImpl extends AbstractCrownService implements
		ReportService {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd-MMM-yyyy");

	@SuppressWarnings("unchecked")
	@Override
	public List<TotalStockBO> getReorderLevel() {
		final GeneralHelper helper = new GeneralHelper();
		final String reorderLevelQuery = "SELECT DISTINCT TS.ITEM_ID, TS.UOM, ITEM_QTY, UOM_QTY FROM TOTAL_STOCK TS, ITEM I" +
				" WHERE TS.ITEM_QTY <= I.REORDER_LEVEL OR TS.UOM_QTY <= I.REORDER_LEVEL";
		final Query query = getNativeQuery(reorderLevelQuery, TotalStock.class);
		query.setMaxResults(10);
		final List<TotalStockBO> bos = new ArrayList<TotalStockBO>();
		final List<TotalStock> stocks = query.getResultList();
		for (TotalStock stock : stocks) {
			TotalStockBO bo = helper.createTotalStockBO(stock);
			Item item = manager.find(Item.class, stock.getItemId());
			final ItemBO itemBO = new ItemBO();

			itemBO.setId(item.getItemId());
			itemBO.setItemCode(item.getItemCode());
			itemBO.setItemName(item.getItemName());
			itemBO.setReorderLevel(item.getReorderLevel());
			bo.setItemBO(itemBO);
			bos.add(bo);
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceDashboardBO> getInvoiceStateDashboard(String invoiceType) {
		Calendar today = Calendar.getInstance();
		Calendar oneMonthBack = Calendar.getInstance();
		oneMonthBack.add(Calendar.DATE, -30);

		String groupByStateQuery = "SELECT COUNT(*) AS CNT, INVOICE_STATE FROM INVOICE WHERE INVOICE_TYPE = '"
				+ invoiceType
				+ "' AND INVOICE_DATE BETWEEN '"
				+ DATE_FORMAT.format(oneMonthBack.getTime())
				+ "' AND '"
				+ DATE_FORMAT.format(today.getTime())
				+ "' GROUP BY INVOICE_STATE";
		Query query = getNativeQuery(groupByStateQuery);
		List<InvoiceDashboardBO> bos = new ArrayList<InvoiceDashboardBO>();
		final List<Object[]> list = query.getResultList();
		for (Object[] obj : list) {
			final InvoiceDashboardBO bo = new InvoiceDashboardBO();
			bo.setCount((Long) obj[0]);
			bo.setLegend(InvoiceStateBO.valueOf((Integer) obj[1]).name());
			bos.add(bo);
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceDashboardBO> getInvoicePaymentDashboard(
			String invoiceType) {
		Calendar today = Calendar.getInstance();
		Calendar oneMonthBack = Calendar.getInstance();
		oneMonthBack.add(Calendar.DATE, -30);

		String groupByStateQuery = "SELECT COUNT(*) AS CNT, PAYMENT_STATUS FROM INVOICE WHERE INVOICE_TYPE = '"
				+ invoiceType
				+ "' AND INVOICE_DATE BETWEEN '"
				+ DATE_FORMAT.format(oneMonthBack.getTime())
				+ "' AND '"
				+ DATE_FORMAT.format(today.getTime())
				+ "' GROUP BY PAYMENT_STATUS";
		Query query = getNativeQuery(groupByStateQuery);
		List<InvoiceDashboardBO> bos = new ArrayList<InvoiceDashboardBO>();
		final List<Object[]> list = query.getResultList();
		for (Object[] obj : list) {
			final InvoiceDashboardBO bo = new InvoiceDashboardBO();
			bo.setCount((Long) obj[0]);
			bo.setLegend(PaymentStatusBO.valueOf((Integer) obj[1]).name());
			bos.add(bo);
		}
		return bos;
	}
}
