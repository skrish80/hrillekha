package com.techlords.crown.mvc.reports;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.chart.PieChartModel;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.CrownAuditBO;
import com.techlords.crown.business.model.InvoiceDashboardBO;
import com.techlords.crown.business.model.TotalStockBO;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.service.AuditService;
import com.techlords.crown.service.ReportService;
import com.techlords.infra.CrownConstants;

@SessionScoped
@ManagedBean
public class ReportsController {
	private final ReportService service = CrownServiceLocator.INSTANCE
			.getCrownService(ReportService.class);

	private final List<CrownAuditBO> auditData = new ArrayList<CrownAuditBO>();
	private final List<TotalStockBO> reorderData = new ArrayList<TotalStockBO>();
	private final PieChartModel retailInvPmtData = new PieChartModel();
	private final PieChartModel retailInvSttData = new PieChartModel();
	private final PieChartModel wholesaleInvPmtData = new PieChartModel();
	private final PieChartModel wholesaleInvSttData = new PieChartModel();

	private void loadAuditReport() {
		if (FacesUtil.isRenderPhase()) {
			auditData.clear();
			final AuditService service = CrownServiceLocator.INSTANCE
					.getCrownService(AuditService.class);
			auditData.addAll(service.getAllAuditReport());
		}
	}

	public final List<CrownAuditBO> getAuditData() {
		loadAuditReport();
		return auditData;
	}

	public final PieChartModel getRetailInvPmtData() {
		if (FacesUtil.isRenderPhase()) {
			retailInvPmtData.clear();
			final List<InvoiceDashboardBO> data = service
					.getInvoicePaymentDashboard(CrownConstants.RETAIL);
			if(data.isEmpty()) {
				retailInvPmtData.set("No Invoices", 0);
			}
			for (InvoiceDashboardBO bo : data) {
				retailInvPmtData.set(bo.getLegend(), bo.getCount());
			}
		}
		return retailInvPmtData;
	}

	public final PieChartModel getWholesaleInvPmtData() {
		if (FacesUtil.isRenderPhase()) {
			wholesaleInvPmtData.clear();
			final List<InvoiceDashboardBO> data = service
					.getInvoicePaymentDashboard(CrownConstants.WHOLESALE);
			if(data.isEmpty()) {
				wholesaleInvPmtData.set("No Invoices", 0);
			}
			for (InvoiceDashboardBO bo : data) {
				wholesaleInvPmtData.set(bo.getLegend(), bo.getCount());
			}
		}
		return wholesaleInvPmtData;
	}

	public final PieChartModel getRetailInvSttData() {
		if (FacesUtil.isRenderPhase()) {
			retailInvSttData.clear();
			final List<InvoiceDashboardBO> data = service
					.getInvoiceStateDashboard(CrownConstants.RETAIL);
			if(data.isEmpty()) {
				retailInvSttData.set("No Invoices", 0);
			}
			for (InvoiceDashboardBO bo : data) {
				retailInvSttData.set(bo.getLegend(), bo.getCount());
			}
		}
		return retailInvSttData;
	}

	public final PieChartModel getWholesaleInvSttData() {
		if (FacesUtil.isRenderPhase()) {
			wholesaleInvSttData.clear();
			final List<InvoiceDashboardBO> data = service
					.getInvoiceStateDashboard(CrownConstants.WHOLESALE);
			if(data.isEmpty()) {
				wholesaleInvSttData.set("No Invoices", 0);
			}
			for (InvoiceDashboardBO bo : data) {
				wholesaleInvSttData.set(bo.getLegend(), bo.getCount());
			}
		}
		return wholesaleInvSttData;
	}

	public final List<TotalStockBO> getReorderData() {
		if (FacesUtil.isRenderPhase()) {
			reorderData.clear();
			reorderData.addAll(service.getReorderLevel());
		}
		return reorderData;
	}
}
