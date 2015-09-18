package com.techlords.crown.mvc.stock;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.business.model.WarehouseStockBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.WarehouseStockValidator;
import com.techlords.crown.service.ItemService;
import com.techlords.crown.service.WarehouseService;

/**
 * Handles requests for the application home page.
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class WarehouseStockController extends CrownModelController {

	private static final Logger LOGGER = Logger
			.getLogger(WarehouseStockController.class);

	private final List<ItemBO> itemBOs = new ArrayList<ItemBO>();
	private final List<WarehouseBO> warehouseBOs = new ArrayList<WarehouseBO>();
	private final List<WarehouseStockBO> warehouseStockBOs = new ArrayList<WarehouseStockBO>();
	private WarehouseStockBO currentWarehouseStock;
	private boolean createMode;

	public List<WarehouseStockBO> getWarehouseStockBOs() {
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			warehouseStockBOs.clear();
			setCurrentWarehouseStock(null);
			setCreateMode(false);

			final WarehouseService service = CrownServiceLocator.INSTANCE
					.getCrownService(WarehouseService.class);
			warehouseStockBOs.addAll(service.findAllWarehouseStock());
			isListLoaded = true;
		}
		return warehouseStockBOs;
	}

	public String setupForm(boolean isCreateMode) {
		setCreateMode(isCreateMode);
		return isCreateMode() ? setupForm(new WarehouseStockBO()) : null;
	}

	public String setupForm(WarehouseStockBO bo) {
		setCurrentWarehouseStock(bo);
		loadAssociations();
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/UpdateWarehouseStock.xhtml");
		return null;
	}

	public String save() {
		currentWarehouseStock.setItemBO(getAppModel(
				currentWarehouseStock.getItemID(), itemBOs));
		currentWarehouseStock.setWarehouseBO(getAppModel(
				currentWarehouseStock.getWarehouseID(), warehouseBOs));
		// do only for this as isNew() is different from other App Model Objects
		return isCreateMode() ? create() : update();
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

	public String create() {

		WarehouseStockValidator validator = new WarehouseStockValidator();
		try {
			validator.validateStockCreation(currentWarehouseStock);
		} catch(Exception e) {
			FacesUtil.addErrorFlashMessage("Cannot create Warehouse Stock",
					e.getMessage());
			return null;
		}
		if (CrownMVCHelper.containsWarehouseItem(warehouseStockBOs,
				currentWarehouseStock)) {
			FacesUtil.addErrorFlashMessage("Cannot create Warehouse Stock",
					"Stock already exists: Update instead of trying to create");
			setCreateMode(false);
			return null;
		}
		setCreateMode(false);
		WarehouseService service = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);
		try {
			service.createWarehouseStock(currentWarehouseStock,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/WarehouseStock.xhtml");
		return null;
	}

	public String update() {
		WarehouseStockValidator validator = new WarehouseStockValidator();
		try {
			validator.validateStockUpdate(currentWarehouseStock);
		} catch(Exception e) {
			FacesUtil.addErrorFlashMessage("Cannot add Warehouse Stock",
					e.getMessage());
			return null;
		}
		currentWarehouseStock.setItemQty(currentWarehouseStock.getUpdItemQty());
		currentWarehouseStock.setUomQty(currentWarehouseStock.getUpdUomQty());
		WarehouseService service = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);
		try {
			service.updateWarehouseStock(currentWarehouseStock,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/WarehouseStock.xhtml");
		return null;
	}

	public String view(WarehouseStockBO bo) {
		setCurrentWarehouseStock(bo);
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/ViewWarehouseStock.xhtml");
		return null;
	}

	public WarehouseStockBO getCurrentWarehouseStock() {
		return currentWarehouseStock;
	}

	public void setCurrentWarehouseStock(WarehouseStockBO currentWarehouseStock) {
		this.currentWarehouseStock = currentWarehouseStock;
	}

	public List<ItemBO> getItemBOs() {
		return itemBOs;
	}

	public List<WarehouseBO> getWarehouseBOs() {
		return warehouseBOs;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}
}
