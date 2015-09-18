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
@ManagedBean(name = "configMenu")
public class ConfigCarouselMenu implements Serializable {
	private final List<CrownMenu> configMenus;

	public ConfigCarouselMenu() {
		configMenus = new CrownMenuList<CrownMenu>();
		configMenus.add(new CrownMenu("/images/config/user.PNG",
				"config/CrownUser.xhtml", "User"));
		configMenus.add(new CrownMenu("/images/config/company.PNG",
				"config/Company.xhtml", "Company"));
		configMenus.add(new CrownMenu("/images/config/customer.PNG",
				"config/Customer.xhtml", "Customer"));
		configMenus.add(new CrownMenu("/images/config/agent.PNG",
				"config/Supplier.xhtml", "Supplier"));
		// configMenus.add(new CrownMenu("/images/config/agent.PNG",
		// "config/Agent.xhtml", "Agent"));
		configMenus.add(new CrownMenu("/images/config/warehouse.PNG",
				"config/Warehouse.xhtml", "Warehouse"));
		configMenus.add(new CrownMenu("/images/config/warehouse.PNG",
				"config/RetailShop.xhtml", "Retail Shop"));
		configMenus.add(new CrownMenu("/images/config/credit.PNG",
				"config/CreditReceipt.xhtml", "Receipts"));

	}

	/**
	 * @return the orderViews
	 */
	public List<CrownMenu> getConfigMenus() {
		return configMenus;
	}

}
