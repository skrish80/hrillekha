package com.techlords.crown.mvc.stock;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.StolenStockBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.StockValidator;
import com.techlords.crown.service.ItemService;
import com.techlords.crown.service.WarehouseService;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class StolenStockController extends CrownModelController {

	private static final Logger LOGGER = Logger
			.getLogger(StolenStockController.class);

	private final List<StolenStockBO> stolenStockBOs = new ArrayList<StolenStockBO>();
	private StolenStockBO currentStolenStock;
	private final List<ItemBO> itemBOs = new ArrayList<ItemBO>();
	private final List<WarehouseBO> warehouseBOs = new ArrayList<WarehouseBO>();

	public List<StolenStockBO> getStolenStockBOs() {
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			stolenStockBOs.clear();
			setCurrentStolenStock(null);

			WarehouseService service = CrownServiceLocator.INSTANCE
					.getCrownService(WarehouseService.class);
			stolenStockBOs.addAll(service.findAllStolenItems());
			isListLoaded = true;
		}
		return stolenStockBOs;
	}

	public String setupForm(boolean isCreateMode) {
		return isCreateMode ? setupForm(new StolenStockBO()) : null;
	}

	public String setupForm(StolenStockBO bo) {
		setCurrentStolenStock(bo);
		loadAssociations();
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/UpdateStolenStock.xhtml");
		return null;
	}

	public String save() {
		currentStolenStock.setItemBO(getAppModel(currentStolenStock.getItem(),
				itemBOs));
		currentStolenStock.setWarehouseBO(getAppModel(
				currentStolenStock.getWarehouse(), warehouseBOs));
		// do only for this as isNew() is different from other App Model Objects
		return currentStolenStock.isNew() ? create() : null;
	}

	public String create() {
		final StockValidator validator = new StockValidator();
		try {
			validator.validateStolenStock(currentStolenStock);
		} catch(Exception e) {
			FacesUtil.addErrorFlashMessage(e.getMessage(), e.getMessage());
			return null;
		}

		WarehouseService service = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);
		try {
			service.createStolenStock(currentStolenStock, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/StolenStock.xhtml");
		return null;
	}

	public String view(StolenStockBO bo) {
		setCurrentStolenStock(bo);
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/ViewStolenStock.xhtml");
		return null;
	}

	private void loadAssociations() {
		ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);
		WarehouseService warehouseService = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);

		itemBOs.clear();
		warehouseBOs.clear();

		itemBOs.addAll(service.findAllItems());
		warehouseBOs.addAll(warehouseService.findAllWarehouses());
		warehouseBOs.addAll(warehouseService.findAllRetailShops());
	}

	public StolenStockBO getCurrentStolenStock() {
		return currentStolenStock;
	}

	public void setCurrentStolenStock(StolenStockBO currentStolenStock) {
		this.currentStolenStock = currentStolenStock;
	}

	public List<ItemBO> getItemBOs() {
		return itemBOs;
	}

	public List<WarehouseBO> getWarehouseBOs() {
		return warehouseBOs;
	}
}
