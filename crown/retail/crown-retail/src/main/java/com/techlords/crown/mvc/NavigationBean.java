package com.techlords.crown.mvc;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;

import org.apache.log4j.Logger;

import com.techlords.crown.mvc.auth.CrownUserDetailsService;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class NavigationBean implements Serializable {

	private static final Logger LOGGER = Logger.getLogger(NavigationBean.class);
	
	// private static final String SLASH_CHAR = "/";
	private String navigationUrl;
	private final String UPDATE_FORM_ID = ":includeForm:mainPanel";
	private final String TITLE_PREFIX = "Crown Wholesale/Retail Product";
	private String title = TITLE_PREFIX;
	// private final HttpServletRequest req = FacesUtil.findBean("request",
	// HttpServletRequest.class);
	// private final String prefixURL = req.getContextPath() + SLASH_CHAR;
	private final String accessDeniedUrl = "AccessDenied.xhtml";

	private final List<IHomePageListener> homePageListeners = new ArrayList<IHomePageListener>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUPDATE_FORM_ID() {
		return UPDATE_FORM_ID;
	}

	public String getNavigationUrl() {
		if (CrownUserDetailsService.getCurrentUser() == null) {
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("login.jsf");
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		return navigationUrl;
	}

	public void setNavigationUrl(String navigationUrl) {
		this.navigationUrl = CrownUserDetailsService
				.isLinkAllowed(navigationUrl) ? navigationUrl : accessDeniedUrl;
		notifyHomePageListeners();
	}

	public void addHomePageListener(IHomePageListener listener) {
		homePageListeners.add(listener);
	}

	public void removeHomePageListener(IHomePageListener listener) {
		homePageListeners.remove(listener);
	}

	public final void notifyHomePageListeners() {
		for (final IHomePageListener listener : homePageListeners) {
			listener.homePageSelected();
		}
	}
}
