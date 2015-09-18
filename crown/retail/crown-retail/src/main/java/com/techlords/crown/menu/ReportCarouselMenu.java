/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.techlords.crown.menu;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;

/**
 * 
 * @author gv
 */
@SuppressWarnings("serial")
@ManagedBean
public class ReportCarouselMenu implements Serializable {
	private final List<CrownMenu> reportMenus;

	public ReportCarouselMenu() {
		reportMenus = new CrownMenuList<CrownMenu>();
		
		reportMenus.add(new CrownMenu("/images/config/user.PNG",
				"reports/InvoiceDashboard.xhtml", "Invoice Dashboard"));
		reportMenus.add(new CrownMenu("/images/config/user.PNG",
				"reports/ReorderReport.xhtml", "Reorder Level"));
		reportMenus.add(new CrownMenu("/images/config/user.PNG",
				"reports/AuditReport.xhtml", "Audit Report"));
	}

	public final List<CrownMenu> getReportMenus() {
		return reportMenus;
	}
}
