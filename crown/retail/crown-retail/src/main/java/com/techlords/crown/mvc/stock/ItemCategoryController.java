package com.techlords.crown.mvc.stock;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.ItemCategoryBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.service.ItemService;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class ItemCategoryController extends CrownModelController {
	private static final Logger LOGGER = Logger
			.getLogger(ItemCategoryController.class);

	private final List<ItemCategoryBO> categoryBOs = new ArrayList<ItemCategoryBO>();
	private ItemCategoryBO currentCategory;

	/**
	 * Simply selects the home view to render by returning its name.
	 */

	public List<ItemCategoryBO> getCategoryBOs() {
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			categoryBOs.clear();
			setCurrentCategory(null);
			ItemService service = CrownServiceLocator.INSTANCE
					.getCrownService(ItemService.class);
			categoryBOs.addAll(service.findAllItemCategories());
			isListLoaded = true;
		}
		return categoryBOs;
	}

	public String setupForm(boolean isCreateMode) {
		return isCreateMode ? setupForm(new ItemCategoryBO()) : null;
	}

	public String setupForm(ItemCategoryBO bo) {
		setCurrentCategory(bo);
		setFieldAvailability(EMPTY_STRING);
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/UpdateItemCategory.xhtml");
		return null;
	}

	public void checkUniqueCategoryName() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("category_name",
				currentCategory.getItemCategory()) ? AVAILABLE : UNAVAILABLE);
	}

	public String save() {
		return currentCategory.isNew() ? create() : update();
	}

	public String create() {
		ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);
		try {
			service.createItemCategory(currentCategory, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/ItemCategory.xhtml");
		return null;
	}

	public String update() {

		ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);
		try {
			service.updateItemCategory(currentCategory, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/ItemCategory.xhtml");
		return null;
	}

	public String delete(ItemCategoryBO bo) {
		ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);
		try {
			service.deleteItemCategory(bo, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/ItemCategory.xhtml");
		return null;
	}

	public String view(ItemCategoryBO bo) {
		setCurrentCategory(bo);
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/ViewItemCategory.xhtml");
		return null;
	}

	public ItemCategoryBO getCurrentCategory() {
		return currentCategory;
	}

	public void setCurrentCategory(ItemCategoryBO currentCategory) {
		this.currentCategory = currentCategory;
	}
}
