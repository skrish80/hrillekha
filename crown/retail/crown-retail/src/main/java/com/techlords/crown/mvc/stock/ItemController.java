package com.techlords.crown.mvc.stock;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.CurrencyBO;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.ItemBrandBO;
import com.techlords.crown.business.model.ItemCategoryBO;
import com.techlords.crown.business.model.UomBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.StockValidator;
import com.techlords.crown.service.ItemService;

/**
 * Handles requests for the application home page.
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class ItemController extends CrownModelController {

	private static final Logger LOGGER = Logger.getLogger(ItemController.class);

	private final List<ItemBO> itemBOs = new ArrayList<ItemBO>();
	private ItemBO currentItem;
	private final List<ItemBrandBO> brandBOs = new ArrayList<ItemBrandBO>();
	private final List<ItemCategoryBO> categoryBOs = new ArrayList<ItemCategoryBO>();
	private final List<CurrencyBO> currencyBOs = new ArrayList<CurrencyBO>();
	private final List<UomBO> uomBOs = new ArrayList<UomBO>();

	private final LazyDataModel<ItemBO> itemModel = new LazyItemDataModel();

	public List<ItemBO> getItemBOs() {
		System.out.println("Getting all ITEMS...");
		long time1 = System.currentTimeMillis();
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			itemBOs.clear();
			setCurrentItem(null);
			ItemService service = CrownServiceLocator.INSTANCE
					.getCrownService(ItemService.class);
			itemBOs.addAll(service.findAllItems());
			isListLoaded = true;
		}
		long time2 = System.currentTimeMillis();
		System.err.println("TIME TAKEN TO LOAD ITEMS ::: " + (time2 - time1));
		return itemBOs;
	}

	public String setupForm(boolean isCreateMode) {
		return isCreateMode ? setupForm(new ItemBO()) : null;
	}

	public String setupForm(ItemBO bo) {
		setCurrentItem(bo);
		loadAssociations();
		setFieldAvailability(EMPTY_STRING);
		currentItem.setCurrency("SCR");
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/UpdateItem.xhtml");
		return null;
	}

	public String save() {
		currentItem.setBrandBO(getAppModel(currentItem.getBrand(), brandBOs));
		currentItem.setCategoryBO(getAppModel(currentItem.getCategory(),
				categoryBOs));
		currentItem.setUomBO(getAppModel(currentItem.getUom(), uomBOs));
		final StockValidator validator = new StockValidator();
		try {
			validator.validateItemCreation(currentItem);
		} catch(Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		return currentItem.isNew() ? create() : update();
	}

	public void updateItemPrice() {
		if (currentItem.getPiecesPerUOM() == 0) {
			FacesUtil.addErrorFlashMessage("Enter Pieces Per UOM");
			return;
		}
		currentItem.setItemPrice(currentItem.getUomPrice()
				/ currentItem.getPiecesPerUOM());
	}

	public void checkUniqueItemName() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("item_name",
				currentItem.getItemName()) ? AVAILABLE : UNAVAILABLE);
	}

	public String create() {

		ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);
		try {
			service.createItem(currentItem, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/Item.xhtml");
		return null;
	}

	public String update() {

		ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);
		try {
			service.updateItem(currentItem, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/Item.xhtml");
		return null;
	}

	public String delete(ItemBO bo) {
		ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);
		try {
			service.deleteItem(bo, CrownUserDetailsService.getCurrentUser()
					.getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/Item.xhtml");
		return null;
	}

	public String view(ItemBO bo) {
		setCurrentItem(bo);
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/ViewItem.xhtml");
		return null;
	}

	public void loadAssociations() {
		ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);

		brandBOs.clear();
		categoryBOs.clear();

		brandBOs.addAll(service.findAllItemBrands());
		categoryBOs.addAll(service.findAllItemCategories());
		if (currencyBOs.isEmpty())
			currencyBOs.addAll(CrownMVCHelper.getCurrencyBos());
		if (uomBOs.isEmpty())
			uomBOs.addAll(CrownMVCHelper.getUomBos());
	}

	public ItemBO getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(ItemBO currentItem) {
		this.currentItem = currentItem;
	}

	public List<ItemBrandBO> getBrandBOs() {
		return brandBOs;
	}

	public List<ItemCategoryBO> getCategoryBOs() {
		return categoryBOs;
	}

	public List<CurrencyBO> getCurrencyBOs() {
		return currencyBOs;
	}

	public List<UomBO> getUomBOs() {
		return uomBOs;
	}

	public final LazyDataModel<ItemBO> getItemModel() {
		return itemModel;
	}
}
