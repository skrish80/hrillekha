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
@ManagedBean(name = "stockMenu")
public class StockCarouselMenu implements Serializable {
	private final List<CrownMenu> stockMenus;

	public StockCarouselMenu() {
		stockMenus = new CrownMenuList<CrownMenu>();

		stockMenus.add(new CrownMenu("/images/stock/category.jpg",
				"stock/ItemCategory.xhtml", "Item Category"));
		stockMenus.add(new CrownMenu("/images/stock/brand.jpg",
				"stock/ItemBrand.xhtml", "Item Brand"));
		stockMenus.add(new CrownMenu("/images/stock/item.jpg",
				"stock/Item.xhtml", "Item"));
		stockMenus.add(new CrownMenu("/images/stock/warehouse_stock.jpg",
				"stock/WarehouseStock.xhtml", "Warehouse Stock"));
		stockMenus.add(new CrownMenu("/images/stock/stolen_stock.jpg",
				"stock/StolenStock.xhtml", "Stolen Stock"));
		stockMenus.add(new CrownMenu("/images/stock/stock_allocation.jpg",
				"stock/StockAllocation.xhtml", "Stock Allocation"));
		stockMenus.add(new CrownMenu("/images/stock/stock_movement.jpg",
				"stock/StockMovement.xhtml", "Stock Movement"));
		stockMenus.add(new CrownMenu("/images/invoice/purchase.jpg",
				"stock/AllPurchaseInvoices.xhtml", "Purchase Invoice"));
	}

	public List<CrownMenu> getStockMenus() {
		return stockMenus;
	}

}
