package com.techlords.crown.mvc.stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.CrownEntityBO;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.StockAllocationBO;
import com.techlords.crown.business.model.TotalStockBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.StockValidator;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.ItemService;
import com.techlords.crown.service.StockService;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class StockAllocationController extends CrownModelController {

	private static final Logger LOGGER = Logger
			.getLogger(StockAllocationController.class);

	private final List<StockAllocationBO> stockAllocationBOs = new ArrayList<StockAllocationBO>();
	private StockAllocationBO currentAllocation;

	private final List<ItemBO> itemBOs = new ArrayList<ItemBO>();
	private final List<AllocationTypeBO> typeBOs = Arrays
			.asList(AllocationTypeBO.values());
	private final List<CrownEntityBO> entityBOs = new ArrayList<CrownEntityBO>();

	private TotalStockBO totalStock = null;

	/**
	 * Simply selects the home view to render by returning its name.
	 */

	public List<StockAllocationBO> getStockAllocationBOs() {
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			stockAllocationBOs.clear();
			setCurrentAllocation(null);
			setTotalStock(null);

			StockService service = CrownServiceLocator.INSTANCE
					.getCrownService(StockService.class);
			stockAllocationBOs.addAll(service.findAllAllocatedStock());
			isListLoaded = true;
		}
		return stockAllocationBOs;
	}

	public void getAvailableStock() {
		System.err.println("SELECTION ::: " + currentAllocation.getItem());
		GeneralService generalService = CrownServiceLocator.INSTANCE
				.getCrownService(GeneralService.class);
		// totalStock = generalService
		// .getTotalAvailableQuantity(currentAllocation.getItem());
		List<TotalStockBO> bos = generalService.getAllocationAvailability(
				currentAllocation.getEntity(), currentAllocation.getItem());
		if (bos.isEmpty()) {
			FacesUtil.addErrorFlashMessage("Stock does not exist for item");
			return;
		}
		totalStock = bos.get(0);
	}

	public String setupForm(boolean isCreateMode) {
		return isCreateMode ? setupForm(new StockAllocationBO()) : null;
	}

	public String setupForm(StockAllocationBO bo) {
		setCurrentAllocation(bo);
		loadAssociations();
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/UpdateStockAllocation.xhtml");
		return null;
	}

	public String save() {
		currentAllocation.setItemBO(getAppModel(currentAllocation.getItem(),
				itemBOs));
		currentAllocation.setEntityBO(getAppModel(
				currentAllocation.getEntity(), entityBOs));
		currentAllocation.setAllocationTypeBO(AllocationTypeBO
				.valueOf(currentAllocation.getAllocationType()));
		// do only for this as isNew() is different from other App Model Objects
		return currentAllocation.isNew() ? create() : update();
	}

	public String create() {
		final StockValidator validator = new StockValidator();
		try {
			getAvailableStock();
			validator.validateStockAllocation(currentAllocation, totalStock);
		} catch(Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		StockService service = CrownServiceLocator.INSTANCE
				.getCrownService(StockService.class);
		try {
			service.createStockAllocation(currentAllocation,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/StockAllocation.xhtml");
		return null;
	}

	public String update() {

		StockService service = CrownServiceLocator.INSTANCE
				.getCrownService(StockService.class);
		try {
			service.updateStockAllocation(currentAllocation,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/StockAllocation.xhtml");
		return null;
	}

	public String release(StockAllocationBO bo) {
		setCurrentAllocation(bo);
		StockService service = CrownServiceLocator.INSTANCE
				.getCrownService(StockService.class);
		try {
			service.releaseAllocation(currentAllocation, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/StockAllocation.xhtml");
		return null;
	}

	public String view(StockAllocationBO bo) {
		setCurrentAllocation(bo);
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/ViewStockAllocation.xhtml");
		return null;
	}

	private void loadAssociations() {
		entityBOs.clear();
		itemBOs.clear();

		GeneralService generalService = CrownServiceLocator.INSTANCE
				.getCrownService(GeneralService.class);
		entityBOs.addAll(generalService.findAllWholesaleEntities());
		entityBOs.addAll(generalService.findAllRetailShops());

		ItemService locationService = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);

		itemBOs.addAll(locationService.findAllItems());
	}

	public StockAllocationBO getCurrentAllocation() {
		return currentAllocation;
	}

	public void setCurrentAllocation(StockAllocationBO currentAllocation) {
		this.currentAllocation = currentAllocation;
	}

	public TotalStockBO getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(TotalStockBO totalStock) {
		this.totalStock = totalStock;
	}

	public List<ItemBO> getItemBOs() {
		return itemBOs;
	}

	public List<AllocationTypeBO> getTypeBOs() {
		return typeBOs;
	}

	public List<CrownEntityBO> getEntityBOs() {
		return entityBOs;
	}
}
