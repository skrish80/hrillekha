package com.techlords.crown.mvc.settings;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.CustomerBO;
import com.techlords.crown.business.model.CustomerTypeBO;
import com.techlords.crown.business.model.LocationBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.service.CustomerService;

/**
 * Handles requests for the application home page.
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class CustomerController extends CrownModelController {

	private static final Logger LOGGER = Logger
			.getLogger(CustomerController.class);

	private final List<CustomerBO> customerBOs = new ArrayList<CustomerBO>();
	private CustomerBO currentCustomer;
	private final List<LocationBO> locationBOs = new ArrayList<LocationBO>();
	private final List<CustomerTypeBO> customerTypeBOs = new ArrayList<CustomerTypeBO>();
	private boolean areAssociationsLoaded = false;

	public List<CustomerBO> getCustomerBOs() {
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			customerBOs.clear();
			setCurrentCustomer(null);
			CustomerService service = CrownServiceLocator.INSTANCE
					.getCrownService(CustomerService.class);
			customerBOs.addAll(service.findAllCustomers());
			isListLoaded = true;
		}
		return customerBOs;
	}

	public String setupForm(boolean isCreateMode) {
		return isCreateMode ? setupForm(new CustomerBO()) : null;
	}

	public String setupForm(CustomerBO bo) {
		setCurrentCustomer(bo);
		setFieldAvailability(EMPTY_STRING);
		loadAssociations();
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/UpdateCustomer.xhtml");
		return null;
	}

	public void checkUniqueCustomerName() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("customer_name",
				currentCustomer.getCustomerName()) ? AVAILABLE : UNAVAILABLE);
	}


	public String save() {
		currentCustomer.setCustomerTypeBO(getAppModel(
				currentCustomer.getCustomerType(), customerTypeBOs));
		return currentCustomer.isNew() ? create() : update();
	}

	public String create() {

		CustomerService service = CrownServiceLocator.INSTANCE
				.getCrownService(CustomerService.class);
		try {
			service.createCustomer(currentCustomer, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("config/Customer.xhtml");
		return null;
	}

	public String update() {

		CustomerService service = CrownServiceLocator.INSTANCE
				.getCrownService(CustomerService.class);
		try {
			service.updateCustomer(currentCustomer, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("config/Customer.xhtml");
		return null;
	}

	public String delete(CustomerBO bo) {
		CustomerService service = CrownServiceLocator.INSTANCE
				.getCrownService(CustomerService.class);
		try {
			service.deleteCustomer(bo, CrownUserDetailsService.getCurrentUser()
					.getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/Customer.xhtml");
		return null;
	}

	public String view(CustomerBO bo) {
		setCurrentCustomer(bo);
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/ViewCustomer.xhtml");
		return null;
	}

	private void loadAssociations() {
		if (areAssociationsLoaded) {
			return;
		}

		customerTypeBOs.clear();
		locationBOs.clear();

		customerTypeBOs.addAll(CrownMVCHelper.getCustomerTypeBos());
		locationBOs.addAll(CrownMVCHelper.getLocationBos());
		areAssociationsLoaded = true;
	}

	public CustomerBO getCurrentCustomer() {
		return currentCustomer;
	}

	public void setCurrentCustomer(CustomerBO currentCustomer) {
		this.currentCustomer = currentCustomer;
	}

	public List<LocationBO> getLocationBOs() {
		return locationBOs;
	}

	public List<CustomerTypeBO> getCustomerTypeBOs() {
		return customerTypeBOs;
	}
}
