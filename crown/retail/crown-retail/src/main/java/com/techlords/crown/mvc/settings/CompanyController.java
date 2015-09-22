package com.techlords.crown.mvc.settings;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.CompanyBO;
import com.techlords.crown.business.model.CompanyTypeBO;
import com.techlords.crown.business.model.LocationBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.CompanyValidator;
import com.techlords.crown.service.CompanyService;

/**
 * Handles requests for the Company Operations.
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class CompanyController extends CrownModelController {

	private static final Logger LOGGER = Logger.getLogger(CompanyController.class);

	private final List<CompanyBO> companyBOs = new ArrayList<CompanyBO>();
	private CompanyBO currentCompany;
	private final List<LocationBO> locationBOs = new ArrayList<LocationBO>();
	private final List<CompanyTypeBO> companyTypeBOs = new ArrayList<CompanyTypeBO>();
	private boolean areAssociationsLoaded = false;

	public List<CompanyBO> getCompanyBOs() {
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			companyBOs.clear();
			setCurrentCompany(null);
			CompanyService service = CrownServiceLocator.INSTANCE
					.getCrownService(CompanyService.class);
			companyBOs.addAll(service.findAllCompanies());
			isListLoaded = true;
		}
		return companyBOs;
	}

	public String setupForm(boolean isCreateMode) {
		return isCreateMode ? setupForm(new CompanyBO()) : null;
	}

	public String setupForm(CompanyBO bo) {
		setCurrentCompany(bo);
		setFieldAvailability(EMPTY_STRING);
		loadAssociations();
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/UpdateCompany.xhtml");
		return null;
	}

	public String save() {
		currentCompany.setCompanyTypeBO(getAppModel(
				currentCompany.getCompanyType(), companyTypeBOs));
		currentCompany.setLocationBO(getAppModel(currentCompany.getLocation(),
				locationBOs));
		return currentCompany.isNew() ? create() : update();
	}

	public void checkUniqueCompanyName() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("company_name",
				currentCompany.getCompanyName()) ? AVAILABLE : UNAVAILABLE);
	}

	public void checkUniqueTIN() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("tin",
				currentCompany.getTin()) ? AVAILABLE : UNAVAILABLE);
	}

	public String create() {
		CompanyValidator validator = new CompanyValidator();
		try {
			validator.validateCompanyCreation(currentCompany);
		} catch(Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		CompanyService service = CrownServiceLocator.INSTANCE
				.getCrownService(CompanyService.class);
		try {
			service.createCompany(currentCompany, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("config/Company.xhtml");
		return null;
	}

	public String update() {
		CompanyService service = CrownServiceLocator.INSTANCE
				.getCrownService(CompanyService.class);
		try {
			service.updateCompany(currentCompany, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("config/Company.xhtml");
		return null;
	}

	public String delete(CompanyBO bo) {
		CompanyService service = CrownServiceLocator.INSTANCE
				.getCrownService(CompanyService.class);
		try {
			service.deleteCompany(bo, CrownUserDetailsService.getCurrentUser()
					.getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/Company.xhtml");
		return null;
	}

	public String view(CompanyBO bo) {
		setCurrentCompany(bo);
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/ViewCompany.xhtml");
		return null;
	}

	private void loadAssociations() {
		if (areAssociationsLoaded) {
			return;
		}
		companyTypeBOs.clear();
		locationBOs.clear();
		companyTypeBOs.addAll(CrownMVCHelper.getCompanyTypeBos());
		locationBOs.addAll(CrownMVCHelper.getLocationBos());
		areAssociationsLoaded = true;
	}

	public CompanyBO getCurrentCompany() {
		return currentCompany;
	}

	public void setCurrentCompany(CompanyBO currentCompany) {
		this.currentCompany = currentCompany;
	}

	public List<LocationBO> getLocationBOs() {
		return locationBOs;
	}

	public List<CompanyTypeBO> getCompanyTypeBOs() {
		return companyTypeBOs;
	}
}
