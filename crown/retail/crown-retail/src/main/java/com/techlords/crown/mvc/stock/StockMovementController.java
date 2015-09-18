package com.techlords.crown.mvc.stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.StockMovementBO;
import com.techlords.crown.business.model.StockMovementItemBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.business.model.WarehouseStockBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.MoveStatusBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.flow.CrownFlowService;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.StockValidator;
import com.techlords.crown.service.ItemService;
import com.techlords.crown.service.StockService;
import com.techlords.crown.service.WarehouseService;

/**
 * Handles requests for the application home page.
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class StockMovementController extends CrownModelController {

	private static final Logger LOGGER = Logger
			.getLogger(StockMovementController.class);

	private final List<StockMovementBO> stockMovementBOs = new ArrayList<StockMovementBO>();
	private final List<AllocationTypeBO> typeBOs = Arrays
			.asList(AllocationTypeBO.values());

	private final List<ItemBO> itemBOs = new ArrayList<ItemBO>();
	private final List<MoveStatusBO> moveStatusBOs = new ArrayList<MoveStatusBO>(
			Arrays.asList(MoveStatusBO.values()));
	private final List<WarehouseBO> warehouseBOs = new ArrayList<WarehouseBO>();
	private final List<WarehouseStockBO> warehouseStockBOs = new ArrayList<WarehouseStockBO>();

	private StockMovementBO currentMovement;

	public String reinitMoveItem() {
		currentMovement.addStockMovementItem(new StockMovementItemBO());
		return null;
	}

	public String removeMoveItem(StockMovementItemBO bo) {
		currentMovement.removeStockMovementItem(bo);
		return null;
	}

	public List<StockMovementBO> getStockMovementBOs() {
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			stockMovementBOs.clear();
			warehouseStockBOs.clear();
			setCurrentMovement(null);
			StockService service = CrownServiceLocator.INSTANCE
					.getCrownService(StockService.class);
			stockMovementBOs.addAll(service.findAllMovementStock());
			isListLoaded = true;
		}
		return stockMovementBOs;
	}

	public String checkWarehouseItemsAvailability() {

		WarehouseService warehouseService = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);
		warehouseStockBOs.clear();
		List<Integer> itemIDs = new ArrayList<Integer>();
		if (currentMovement.getStockMovementItems().isEmpty()) {
			FacesUtil.addErrorFlashMessage("Add Items", "Add Movement Items");
			return null;
		}
		for (StockMovementItemBO itm : currentMovement.getStockMovementItems()) {
			itemIDs.add(itm.getItem());
		}
		warehouseStockBOs.addAll(warehouseService.findWarehouseStock(
				currentMovement.getFromWarehouse(), itemIDs.toArray()));
		return null;
	}

	public String setupForm(boolean isCreateMode) {
		return isCreateMode ? setupForm(new StockMovementBO()) : null;
	}

	public String setupForm(StockMovementBO bo) {
		setCurrentMovement(bo);
		loadAssociations();
		loadStatuses();
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/UpdateStockMovement.xhtml");
		FacesUtil.addFlashMessage("Select Move Status");
		return null;
	}

	private void loadStatuses() {
		moveStatusBOs.clear();
		final CrownFlowService flowService = CrownFlowService.INSTANCE;
		final String flowClass = StockMovementBO.class.getSimpleName();
		if (currentMovement.isNew()) {
			int firstStepID = flowService.getFirstStepID(flowClass);
			moveStatusBOs.add(MoveStatusBO.valueOf(firstStepID));
		} else {
			moveStatusBOs.addAll(MoveStatusBO.getValues(flowService
					.getNextSteps(flowClass, currentMovement.getMoveStatus())));
		}
	}

	public String save() {
		currentMovement.setFromWarehouseBO(getAppModel(
				currentMovement.getFromWarehouse(), warehouseBOs));
		currentMovement.setToWarehouseBO(getAppModel(
				currentMovement.getToWarehouse(), warehouseBOs));
		currentMovement.setMoveStatusBO(MoveStatusBO.valueOf(currentMovement
				.getMoveStatus()));
		return currentMovement.isNew() ? create() : update();
	}

	public String create() {
		final StockValidator validator = new StockValidator();
		for (StockMovementItemBO itm : currentMovement.getStockMovementItems()) {
			itm.setItemBO(getAppModel(itm.getItem(), itemBOs));
		}

		try {
			checkWarehouseItemsAvailability();
			validator.validateStockOnMove(currentMovement, warehouseStockBOs);
		} catch(Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}

		StockService service = CrownServiceLocator.INSTANCE
				.getCrownService(StockService.class);
		try {
			service.createStockMovement(currentMovement,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/StockMovement.xhtml");
		return null;
	}

	public String update() {
		if (currentMovement.getMoveStatus() == MoveStatusBO.CANCELLED
				.getMoveStatusID()) {
			return cancel();
		}

		final StockValidator validator = new StockValidator();
		for (StockMovementItemBO itm : currentMovement.getStockMovementItems()) {
			itm.setItemBO(getAppModel(itm.getItem(), itemBOs));
		}

		try {
			validator.validateStockOnMove(currentMovement, warehouseStockBOs);
		} catch(Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		StockService service = CrownServiceLocator.INSTANCE
				.getCrownService(StockService.class);
		try {
			service.updateStockMovement(currentMovement,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/StockMovement.xhtml");
		return null;
	}

	public String cancel() {
		StockService service = CrownServiceLocator.INSTANCE
				.getCrownService(StockService.class);
		try {
			service.cancelStockMovement(currentMovement, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/StockMovement.xhtml");
		return null;
	}

	public String view(StockMovementBO bo) {
		setCurrentMovement(bo);
		loadAssociations();
		loadStatuses();
		for (StockMovementItemBO itemBO : bo.getStockMovementItems()) {
			int itemID = itemBO.getItem();
			itemBO.setItemBO(getAppModel(itemID, itemBOs));
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/ViewStockMovement.xhtml");
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

	public StockMovementBO getCurrentMovement() {
		return currentMovement;
	}

	public void setCurrentMovement(StockMovementBO currentMovement) {
		this.currentMovement = currentMovement;
	}

	public List<AllocationTypeBO> getTypeBOs() {
		return typeBOs;
	}

	public List<ItemBO> getItemBOs() {
		return itemBOs;
	}

	public List<MoveStatusBO> getMoveStatusBOs() {
		return moveStatusBOs;
	}

	public List<WarehouseBO> getWarehouseBOs() {
		return warehouseBOs;
	}

	public List<WarehouseStockBO> getWarehouseStockBOs() {
		return warehouseStockBOs;
	}

	public boolean canReturn() {
		if (currentMovement.isNew()) {
			return false;
		}
		currentMovement.setMoveStatusBO(MoveStatusBO.valueOf(currentMovement
				.getMoveStatus()));

		switch (currentMovement.getMoveStatusBO()) {
		case MOVED:
		case PARTIAL_ACCEPT:
		case RETURNED:
			return true;
		default:
			break;
		}
		return false;
	}

	public boolean canReceive() {
		if (currentMovement.isNew()) {
			return false;
		}
		currentMovement.setMoveStatusBO(MoveStatusBO.valueOf(currentMovement
				.getMoveStatus()));
		switch (currentMovement.getMoveStatusBO()) {
		case MOVED:
		case PARTIAL_ACCEPT:
		case ACCEPTED:
			return true;
		default:
			break;
		}
		return false;
	}
}
