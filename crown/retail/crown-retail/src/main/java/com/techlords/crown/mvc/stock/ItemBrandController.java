package com.techlords.crown.mvc.stock;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.ItemBrandBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.service.ItemService;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class ItemBrandController extends CrownModelController {
	private static final Logger LOGGER = Logger
			.getLogger(ItemBrandController.class);

	private final List<ItemBrandBO> brandBOs = new ArrayList<ItemBrandBO>();
	private ItemBrandBO currentBrand;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	public List<ItemBrandBO> getBrandBOs() {
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			brandBOs.clear();
			setCurrentBrand(null);
			ItemService service = CrownServiceLocator.INSTANCE
					.getCrownService(ItemService.class);
			brandBOs.addAll(service.findAllItemBrands());
			isListLoaded = true;
		}
		return brandBOs;
	}

	public String setupForm(boolean isCreateMode) {
		return isCreateMode ? setupForm(new ItemBrandBO()) : null;
	}

	public String setupForm(ItemBrandBO bo) {
		setCurrentBrand(bo);
		setFieldAvailability(EMPTY_STRING);
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/UpdateItemBrand.xhtml");
		return null;
	}

	public void checkUniqueBrandName() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("brand_name",
				currentBrand.getItemBrand()) ? AVAILABLE : UNAVAILABLE);
	}

	public String save() {
		return currentBrand.isNew() ? create() : update();
	}

	public String create() {

		ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);
		try {
			service.createItemBrand(currentBrand, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/ItemBrand.xhtml");
		return null;
	}

	public String update() {

		ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);
		try {
			service.updateItemBrand(currentBrand, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/ItemBrand.xhtml");
		return null;
	}

	public String delete(ItemBrandBO bo) {
		ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);
		try {
			service.deleteItemBrand(bo, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/ItemBrand.xhtml");
		return null;
	}

	public String view(ItemBrandBO bo) {
		setCurrentBrand(bo);
		isListLoaded = false;
		navigationBean.setNavigationUrl("stock/ViewItemBrand.xhtml");
		return null;
	}

	public ItemBrandBO getCurrentBrand() {
		return currentBrand;
	}

	public void setCurrentBrand(ItemBrandBO currentBrand) {
		this.currentBrand = currentBrand;
	}
}
