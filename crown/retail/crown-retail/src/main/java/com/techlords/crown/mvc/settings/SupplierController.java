package com.techlords.crown.mvc.settings;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.LocationBO;
import com.techlords.crown.business.model.SupplierBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.service.PurchaseInvoiceService;

/**
 * Handles requests for the application home page.
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class SupplierController extends CrownModelController {

	private static final Logger LOGGER = Logger
			.getLogger(SupplierController.class);
	private final List<SupplierBO> supplierBOs = new ArrayList<SupplierBO>();
	private SupplierBO currentSupplier;
	private final List<LocationBO> locationBOs = new ArrayList<LocationBO>();
	private boolean areAssociationsLoaded = false;

	public List<SupplierBO> getSupplierBOs() {
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			supplierBOs.clear();
			setCurrentSupplier(null);
			PurchaseInvoiceService service = CrownServiceLocator.INSTANCE
					.getCrownService(PurchaseInvoiceService.class);
			supplierBOs.addAll(service.findAllSuppliers());
			isListLoaded = true;
		}
		return supplierBOs;
	}

	public String setupForm(boolean isCreateMode) {
		return isCreateMode ? setupForm(new SupplierBO()) : null;
	}

	public String setupForm(SupplierBO bo) {
		setCurrentSupplier(bo);
		setFieldAvailability(EMPTY_STRING);
		loadAssociations();
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/UpdateSupplier.xhtml");
		return null;
	}

	public void checkUniqueSupplierName() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("supplier_name",
				currentSupplier.getSupplierName()) ? AVAILABLE : UNAVAILABLE);
	}

	public void checkUniqueSbn() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("supplier@sbn",
				currentSupplier.getTin()) ? AVAILABLE : UNAVAILABLE);
	}

	public String save() {
		currentSupplier.setLocationBO(getAppModel(
				currentSupplier.getLocation(), locationBOs));
		return currentSupplier.isNew() ? create() : update();
	}

	public String create() {

		PurchaseInvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(PurchaseInvoiceService.class);
		try {
			service.createSupplier(currentSupplier, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("config/Supplier.xhtml");
		return null;
	}

	public String update() {

		PurchaseInvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(PurchaseInvoiceService.class);
		try {
			service.updateSupplier(currentSupplier, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("config/Supplier.xhtml");
		return null;
	}

	public String delete(SupplierBO bo) {
		PurchaseInvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(PurchaseInvoiceService.class);
		try {
			service.deleteSupplier(bo, CrownUserDetailsService.getCurrentUser()
					.getId());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/Supplier.xhtml");
		return null;
	}

	public String view(SupplierBO bo) {
		setCurrentSupplier(bo);
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/ViewSupplier.xhtml");
		return null;
	}

	private void loadAssociations() {
		if (areAssociationsLoaded) {
			return;
		}

		locationBOs.clear();
		locationBOs.addAll(CrownMVCHelper.getLocationBos());
		areAssociationsLoaded = true;
	}

	public SupplierBO getCurrentSupplier() {
		return currentSupplier;
	}

	public void setCurrentSupplier(SupplierBO currentSupplier) {
		this.currentSupplier = currentSupplier;
	}

	public List<LocationBO> getLocationBOs() {
		return locationBOs;
	}
}
