package com.techlords.crown.menu;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.techlords.crown.mvc.util.FacesUtil;

@ManagedBean
@SessionScoped
public class CrownMenuModel {

	private final MenuModel menuModel = new DefaultMenuModel();

	@PostConstruct
	public void initMenus() {
		DefaultSubMenu configSubmenu = new DefaultSubMenu();
		configSubmenu.setLabel("Settings");
		configSubmenu.setStyle("font-weight:bold;");
		configSubmenu.setExpanded(true);
		ConfigCarouselMenu configMenus = FacesUtil.findBean("configMenu", ConfigCarouselMenu.class);
		for (CrownMenu menu : configMenus.getConfigMenus()) {
			final DefaultMenuItem it = new DefaultMenuItem();
			it.setId("ID" + it.hashCode());
			it.setValue(menu.getDescription());
			it.setCommand(("#{hotKeyController.showView('" + menu.getLinkURL() + "')}"));
			it.setUpdate(":includeForm:mainPanel");
			it.setProcess("@this");
			configSubmenu.getElements().add(it);
		}
		if (!configSubmenu.getElements().isEmpty()) {
			menuModel.addElement(configSubmenu);
		}

		DefaultSubMenu stockSubmenu = new DefaultSubMenu();
		stockSubmenu.setLabel("Stock");
		stockSubmenu.setExpanded(true);
		StockCarouselMenu stockMenus = FacesUtil.findBean("stockMenu", StockCarouselMenu.class);
		for (CrownMenu menu : stockMenus.getStockMenus()) {
			final DefaultMenuItem it = new DefaultMenuItem();
			it.setId("ID" + it.hashCode());
			it.setValue(menu.getDescription());
			it.setCommand(("#{hotKeyController.showView('" + menu.getLinkURL() + "')}"));
			it.setUpdate(":includeForm:mainPanel");
			it.setProcess("@this");
			stockSubmenu.getElements().add(it);
		}
		if (!stockSubmenu.getElements().isEmpty()) {
			menuModel.addElement(stockSubmenu);
		}

		DefaultSubMenu wsInvoiceSubmenu = new DefaultSubMenu();
		wsInvoiceSubmenu.setLabel("Wholesale Invoice");
		wsInvoiceSubmenu.setExpanded(true);
		InvoiceCarouselMenu invoiceMenus = FacesUtil.findBean("invoiceCarouselMenu",
				InvoiceCarouselMenu.class);

		for (InvoiceMenu menu : invoiceMenus.getWholesaleMenus()) {
			final DefaultMenuItem it = new DefaultMenuItem();
			it.setId("ID" + it.hashCode());
			it.setValue(menu.getDescription());
			it.setCommand(("#{invoiceControllerFactory.showView('" + menu.getNextAction() + "', '"
					+ menu.getInvoiceType() + "')}"));
			it.setUpdate(":includeForm:mainPanel");
			it.setProcess("@this");
			wsInvoiceSubmenu.getElements().add(it);
		}
		if (!wsInvoiceSubmenu.getElements().isEmpty()) {
			menuModel.addElement(wsInvoiceSubmenu);
		}

		// RETAIL INVOICES
		DefaultSubMenu rtInvoiceSubmenu = new DefaultSubMenu();
		rtInvoiceSubmenu.setLabel("Retail Invoice");
		rtInvoiceSubmenu.setExpanded(true);
		for (InvoiceMenu menu : invoiceMenus.getRetailMenus()) {
			final DefaultMenuItem it = new DefaultMenuItem();
			it.setId("ID" + it.hashCode());
			it.setValue(menu.getDescription());
			it.setCommand(("#{invoiceControllerFactory.showView('" + menu.getNextAction() + "', '"
					+ menu.getInvoiceType() + "')}"));
			it.setUpdate(":includeForm:mainPanel");
			it.setProcess("@this");
			rtInvoiceSubmenu.getElements().add(it);
		}
		if (!rtInvoiceSubmenu.getElements().isEmpty()) {
			menuModel.addElement(rtInvoiceSubmenu);
		}

		// REPORTS MENU
		DefaultSubMenu reports = new DefaultSubMenu();
		reports.setLabel("Reports");
		reports.setExpanded(true);
		ReportCarouselMenu reportMenus = FacesUtil.findBean("reportCarouselMenu",
				ReportCarouselMenu.class);

		for (CrownMenu menu : reportMenus.getReportMenus()) {
			final DefaultMenuItem it = new DefaultMenuItem();
			it.setId("ID" + it.hashCode());
			it.setValue(menu.getDescription());
			it.setCommand(("#{hotKeyController.showView('" + menu.getLinkURL() + "')}"));
			it.setUpdate(":includeForm:mainPanel");
			it.setProcess("@this");
			reports.getElements().add(it);
		}
		if (!reports.getElements().isEmpty()) {
			menuModel.addElement(reports);
		}

	}

	public final MenuModel getMenuModel() {
		return menuModel;
	}
}
