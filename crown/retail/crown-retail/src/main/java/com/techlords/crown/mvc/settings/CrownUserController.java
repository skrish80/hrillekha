package com.techlords.crown.mvc.settings;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.business.model.DepartmentBO;
import com.techlords.crown.business.model.DesignationBO;
import com.techlords.crown.business.model.LocationBO;
import com.techlords.crown.business.model.RoleBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.CrownUserValidator;
import com.techlords.crown.service.CrownUserService;
import com.techlords.infra.CrownConstants;

/**
 * Handles requests for the User Operations.
 */
@ManagedBean
@SessionScoped
public class CrownUserController extends CrownModelController {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(CrownUserController.class);

	private final List<CrownUserBO> userBOs = new ArrayList<CrownUserBO>();
	private CrownUserBO currentUser;

	private final List<LocationBO> locationBOs = new ArrayList<LocationBO>();
	private final List<RoleBO> roleBOs = new ArrayList<RoleBO>();

	private final List<DesignationBO> designationBOs = new ArrayList<DesignationBO>();
	private final List<DepartmentBO> departmentBOs = new ArrayList<DepartmentBO>();

	private boolean areAssociationsLoaded = false;

	public List<CrownUserBO> getUserBOs() {
		System.out.println("Getting all users...");
		long time1 = System.currentTimeMillis();

		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			userBOs.clear();
			setCurrentUser(null);
			CrownUserService service = CrownServiceLocator.INSTANCE
					.getCrownService(CrownUserService.class);
			userBOs.addAll(service.findAllUsers());
			isListLoaded = true;
		}
		long time2 = System.currentTimeMillis();
		System.err.println("TIME TAKEN TO LOAD USERS ::: " + (time2 - time1));
		return userBOs;
	}

	public String setupForm(boolean isCreateMode) {
		return (isCreateMode) ? setupForm(new CrownUserBO()) : null;
	}

	public String setupForm(CrownUserBO bo) {
		setCurrentUser(bo);
		setFieldAvailability(EMPTY_STRING);
		loadAssociations();
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/UpdateUser.xhtml");
		return null;
	}

	public CrownUserBO getLoggedInUser() {
		return CrownUserDetailsService.getCurrentUser();
	}

	public CrownUserBO getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(CrownUserBO currentUser) {
		this.currentUser = currentUser;
	}

	public String save() {
		currentUser.setDesignationBO(getAppModel(currentUser.getDesignation(),
				designationBOs));
		currentUser.setLocationBO(getAppModel(currentUser.getLocation(),
				locationBOs));
		currentUser.setDepartmentBO(getAppModel(currentUser.getDepartment(),
				departmentBOs));
		currentUser.setRoleBO(getAppModel(currentUser.getRole(), roleBOs));
		return (currentUser.isNew()) ? create() : update();

	}

	public void checkUniqueUsername() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("username",
				currentUser.getUsername()) ? AVAILABLE : UNAVAILABLE);
	}

	public void checkUniqueEmail() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("email",
				currentUser.getEmail()) ? AVAILABLE : UNAVAILABLE);
	}

	public void checkUniqueEmployeeNo() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("employee_no",
				currentUser.getEmployeeNo()) ? AVAILABLE : UNAVAILABLE);
	}

	public String create() {
		CrownUserValidator validator = new CrownUserValidator();
		try {
			validator.validateUserCreation(currentUser);
		} catch(Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		currentUser.setPassword(CrownUserDetailsService
				.createMD5Password(currentUser.getPassword()));
		CrownUserService service = CrownServiceLocator.INSTANCE
				.getCrownService(CrownUserService.class);
		try {
			service.createUser(currentUser, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;		}
		currentUser = null;
		navigationBean.setNavigationUrl("config/CrownUser.xhtml");
		return null;
	}

	public String update() {

		CrownUserService service = CrownServiceLocator.INSTANCE
				.getCrownService(CrownUserService.class);
		try {
			service.updateUser(currentUser, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		currentUser = null;
		navigationBean.setNavigationUrl("config/CrownUser.xhtml");
		return null;
	}

	public String delete(CrownUserBO bo) {
		CrownUserService service = CrownServiceLocator.INSTANCE
				.getCrownService(CrownUserService.class);
		try {
			service.deleteUser(bo, CrownUserDetailsService.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/CrownUser.xhtml");
		return null;
	}

	public String view(CrownUserBO bo) {
		setCurrentUser(bo);
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/ViewUser.xhtml");
		return null;
	}

	private String oldPassword;

	public void changePassword() {
		setCurrentUser(CrownUserDetailsService.getCurrentUser());
		oldPassword = currentUser.getPassword();
		navigationBean.setNavigationUrl("config/ChangePassword.xhtml");
	}

	public void updatePassword() {
		if (!oldPassword.equals(CrownUserDetailsService
				.createMD5Password(currentUser.getPassword()))) {
			FacesUtil.addErrorFlashMessage("Please enter correct old Password");
			return;
		}
		currentUser.setPassword(CrownUserDetailsService
				.createMD5Password(currentUser.getChangedPassword()));
		CrownUserService service = CrownServiceLocator.INSTANCE
				.getCrownService(CrownUserService.class);
		try {
			service.changePassword(currentUser, currentUser.getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return;
		}
		
		navigationBean.setNavigationUrl("home.xhtml");
		navigationBean.notifyHomePageListeners();
	}
	
	public void resetPassword(CrownUserBO userBO) {
		userBO.setPassword(CrownUserDetailsService
				.createMD5Password(CrownConstants.RESET_PASSWORD));
		CrownUserService service = CrownServiceLocator.INSTANCE
				.getCrownService(CrownUserService.class);
		try {
			service.changePassword(userBO, CrownUserDetailsService.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return;
		}
	}

	private void loadAssociations() {
		if (areAssociationsLoaded) {
			return;
		}

		designationBOs.clear();
		locationBOs.clear();
		departmentBOs.clear();
		roleBOs.clear();

		designationBOs.addAll(CrownMVCHelper.getDesignationBos());
		roleBOs.addAll(CrownMVCHelper.getRoleBos());
		locationBOs.addAll(CrownMVCHelper.getLocationBos());
		departmentBOs.addAll(CrownMVCHelper.getDepartmentBos());
		areAssociationsLoaded = true;
	}

	public List<LocationBO> getLocationBOs() {
		return locationBOs;
	}

	public List<RoleBO> getRoleBOs() {
		return roleBOs;
	}

	public List<DesignationBO> getDesignationBOs() {
		return designationBOs;
	}

	public List<DepartmentBO> getDepartmentBOs() {
		return departmentBOs;
	}
}
