package com.techlords.crown.mvc.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.CrownEntityBO;
import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.business.model.LocationBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.service.CrownUserService;
import com.techlords.crown.service.WarehouseService;
import com.techlords.infra.CrownConstants;

/**
 * Handles requests for the application home page.
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class WarehouseController extends CrownModelController {

	private static final Logger LOGGER = Logger
			.getLogger(WarehouseController.class);

	private final List<CrownEntityBO> entityBOs = new ArrayList<CrownEntityBO>();
	private WarehouseBO currentWarehouse;
	private final List<WarehouseBO> warehouseBOs = new ArrayList<WarehouseBO>();
	private final List<LocationBO> locationBOs = new ArrayList<LocationBO>();
	private final List<CrownUserBO> userBOs = new ArrayList<CrownUserBO>();
	private boolean isRetailShop;

	public List<WarehouseBO> getWarehouseBOs() {
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			warehouseBOs.clear();
			setCurrentWarehouse(null);
			WarehouseService warehouseService = CrownServiceLocator.INSTANCE
					.getCrownService(WarehouseService.class);
			if (isRetailShop) {
				warehouseBOs.addAll(warehouseService.findAllRetailShops());
			} else {
				warehouseBOs.addAll(warehouseService.findAllWarehouses());
			}
			isListLoaded = true;
		}
		return warehouseBOs;
	}

	public String setupForm(boolean isCreateMode) {
		if (isCreateMode) {
			WarehouseBO warehouse = new WarehouseBO();
			warehouse.setRetailShop(isRetailShop());
			return setupForm(warehouse);
		}
		return null;
	}

	public String setupForm(WarehouseBO bo) {
		setCurrentWarehouse(bo);
		setFieldAvailability(EMPTY_STRING);
		loadAssociations();
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/UpdateWarehouse.xhtml");
		return null;
	}

	public void checkUniqueWarehouseName() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("warehouse_name",
				currentWarehouse.getWarehouseName()) ? AVAILABLE : UNAVAILABLE);
	}

	public String save() {
		currentWarehouse.setEntityBO(getAppModel(currentWarehouse.getEntity(),
				entityBOs));
		currentWarehouse.setInchargeBO(getAppModel(
				currentWarehouse.getIncharge(), userBOs));
		currentWarehouse.setLocationBO(getAppModel(
				currentWarehouse.getLocation(), locationBOs));
		return currentWarehouse.isNew() ? create() : update();
	}

	public String create() {

		WarehouseService warehouseService = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);
		try {
			warehouseService.createWarehouse(currentWarehouse,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("config/Warehouse.xhtml");
		return null;
	}

	public String update() {

		WarehouseService warehouseService = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);
		try {
			warehouseService.updateWarehouse(currentWarehouse,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("config/Warehouse.xhtml");
		return null;
	}

	public String delete(WarehouseBO bo) {

		WarehouseService warehouseService = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);
		try {
			warehouseService.deleteWarehouse(bo, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/Warehouse.xhtml");
		return null;
	}

	public String view(WarehouseBO bo) {
		setCurrentWarehouse(bo);
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/ViewWarehouse.xhtml");
		return null;
	}

	private void loadAssociations() {
		final ExecutorService executorService = Executors.newFixedThreadPool(3);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				userBOs.clear();
				CrownUserService userService = CrownServiceLocator.INSTANCE
						.getCrownService(CrownUserService.class);
				userBOs.addAll(userService.findAllUsers());
			}
		});
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				entityBOs.clear();
				entityBOs.addAll(CrownMVCHelper.getEntityBos());
			}
		});
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				locationBOs.clear();
				locationBOs.addAll(CrownMVCHelper.getLocationBos());
			}
		});
		executorService.shutdown();
		try {
			executorService.awaitTermination(CrownConstants.WAIT_TIME,
					TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			e.printStackTrace();
		}
	}

	public WarehouseBO getCurrentWarehouse() {
		return currentWarehouse;
	}

	public void setCurrentWarehouse(WarehouseBO currentWarehouse) {
		this.currentWarehouse = currentWarehouse;
	}

	public List<CrownEntityBO> getEntityBOs() {
		return entityBOs;
	}

	public List<LocationBO> getLocationBOs() {
		return locationBOs;
	}

	public List<CrownUserBO> getUserBOs() {
		return userBOs;
	}

	public final boolean isRetailShop() {
		return isRetailShop;
	}

	public final void setRetailShop(boolean isRetailShop) {
		this.isRetailShop = isRetailShop;
	}

	public final String getCaptionText() {
		return isRetailShop() ? "Retail Shop" : "Warehouse";
	}
}
