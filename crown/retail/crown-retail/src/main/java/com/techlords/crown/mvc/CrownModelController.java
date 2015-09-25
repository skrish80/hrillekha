package com.techlords.crown.mvc;

import java.util.Arrays;
import java.util.List;

import org.primefaces.context.RequestContext;

import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public abstract class CrownModelController implements IHomePageListener {
	public static final String AVAILABLE = "Available";
	public static final String UNAVAILABLE = "Not Available";
	public static final String EMPTY_STRING = "";
	private final List<StatusBO> statuses = Arrays.asList(StatusBO.values());

	private String fieldAvailability;

	protected final NavigationBean navigationBean = FacesUtil
			.findNavigationBean();

	protected boolean isListLoaded = false;

	public void homePageSelected() {
		isListLoaded = false;
	}

	protected CrownModelController() {
		navigationBean.addHomePageListener(this);
	}

	public final void doNothing() {
		// do nothing...
	}

	abstract protected String save();

	public static <T extends AppModel> T getAppModel(Integer id, List<T> list) {
		for (T t : list) {
			if (id.equals(t.getId())) {
				return t;
			}
		}
		return null;
	}

	public String getFieldAvailability() {
		return fieldAvailability;
	}
	
	public void reset() {
        RequestContext.getCurrentInstance().reset("includeForm:mainPanel");
    }

	public void setFieldAvailability(String fieldAvailability) {
		this.fieldAvailability = fieldAvailability;
	}

	public List<StatusBO> getStatuses() {
		return statuses;
	}

	public NavigationBean getNavigationBean() {
		return navigationBean;
	}
}
