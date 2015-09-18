package com.techlords.crown.mvc.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.business.model.RoleBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.service.WarehouseService;
import com.techlords.infra.UserDetails;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class LoginBean extends CrownModelController {
	
	private static final Logger LOGGER = Logger.getLogger(LoginBean.class);

	private String username;
	private String password;
	private final List<WarehouseBO> retailShops = new ArrayList<WarehouseBO>();
	private int currentShop;
	private WarehouseBO currentShopBO;
	private boolean retailLogin;

	@PostConstruct
	public void init() {
		retailShops.clear();
		retailShops.addAll(CrownServiceLocator.INSTANCE.getCrownService(
				WarehouseService.class).findAllRetailShops());
	}
	
	private void validateRetailUser(UserDetails details) throws CrownException {
		final CrownUserBO bo = (CrownUserBO) details;
		final RoleBO role = bo.getRoleBO();
		if(role == null) {
			throw new CrownException("Not a valid user account. Contact System Administrator");
		}
		if(role.getRole().equals("RETAIL_INCHARGE") && currentShop < 1) {
			throw new CrownException("Select the Shop to login!");
		}
	}

	// This is the action method called when the user clicks the "login" button
	public String doLogin() throws IOException, ServletException {
		try {
			final ExternalContext context = FacesContext.getCurrentInstance()
					.getExternalContext();
			final CrownUserDetailsService userDetailsService = new CrownUserDetailsService();
			final UserDetails details = userDetailsService.loadUserByUsername(
					username, password);
			context.getSessionMap().put("currentUser", details);
			validateRetailUser(details);
			CrownMVCHelper.loadStatics();

			setCurrentShopBO(getAppModel(currentShop, retailShops));
			context.getSessionMap().put("currentShop", currentShopBO);

			// It's OK to return null here because Faces is just going to exit.
			navigationBean.setNavigationUrl("home.xhtml");
			context.redirect("Main.jsf");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addErrorFlashMessage(e.getMessage(), e.getMessage());
			return null;
		}
		return null;
	}

	@Override
	public String save() {
		return null;
	}

	public final String getUsername() {
		return username;
	}

	public final void setUsername(String username) {
		this.username = username;
	}

	public final String getPassword() {
		return password;
	}

	public final void setPassword(String password) {
		this.password = password;
	}

	public final List<WarehouseBO> getRetailShops() {
		return retailShops;
	}

	public final boolean isRetailLogin() {
		return retailLogin;
	}

	public final void setRetailLogin(boolean retailLogin) {
		this.retailLogin = retailLogin;
	}

	public final WarehouseBO getCurrentShopBO() {
		return currentShopBO;
	}

	public final void setCurrentShopBO(WarehouseBO currentShopBO) {
		this.currentShopBO = currentShopBO;
	}

	public final int getCurrentShop() {
		return currentShop;
	}

	public final void setCurrentShop(int currentShop) {
		this.currentShop = currentShop;
	}
}