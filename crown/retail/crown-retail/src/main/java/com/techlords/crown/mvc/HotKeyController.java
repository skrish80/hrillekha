/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.techlords.crown.mvc;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.techlords.crown.mvc.settings.WarehouseController;
import com.techlords.crown.mvc.util.FacesUtil;

/**
 * 
 * @author gv
 */
@ManagedBean
@SessionScoped
public class HotKeyController {
	private final NavigationBean navigationBean = FacesUtil
			.findNavigationBean();

	// /////////////////////////////////////////////////////////////////////////
	// /////////////////////// FOR SETTINGS MENU ///////////////////////////////
	// /////////////////////////////////////////////////////////////////////////
	public String getUserPage() {
		return "config/CrownUser.jsf?faces-redirect=true";
	}

	public String getItemCategoryPage() {
		return "stock/ItemCategory.jsf?faces-redirect=true";
	}

	public void getHomePage() {
		navigationBean.setNavigationUrl("home.xhtml");
		navigationBean.notifyHomePageListeners();
	}

	public void showView(String pageURL) {
		// For Warehouse and Retail Shop
		if ("config/Warehouse.xhtml".equals(pageURL)
				|| "config/RetailShop.xhtml".equals(pageURL)) {
			WarehouseController controller = FacesUtil.findBean(
					"warehouseController", WarehouseController.class);
			controller.setRetailShop("config/RetailShop.xhtml".equals(pageURL));
		}
		navigationBean.setNavigationUrl(pageURL);
	}
}
