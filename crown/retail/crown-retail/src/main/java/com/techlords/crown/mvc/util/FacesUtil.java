package com.techlords.crown.mvc.util;

import java.util.Arrays;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.PhaseId;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.mvc.NavigationBean;
import com.techlords.crown.mvc.invoice.AbstractInvoiceController;

public class FacesUtil {

	public static void addFlashMessage(String message, String... detail) {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext()
				.getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		FacesMessage msg = new FacesMessage(message);
		if (detail != null && detail.length > 0) {
			msg.setDetail(detail[0]);
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public static void addErrorFlashMessage(String message, String... detail) {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext()
				.getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message,
						(detail == null) ? "" : Arrays.toString(detail)));
	}

	public static void addCrownExceptionMessages(CrownException ex) {
		List<String> messages = ex.getMessages();
		if (messages.isEmpty()) {
			addErrorFlashMessage(ex.getMessage());
		}
		for (String msg : messages) {
			addErrorFlashMessage(ex.getMessage(), msg);
		}
	}

	public static void addExceptionMessages(Exception ex) {
		if (ex instanceof CrownException) {
			addCrownExceptionMessages((CrownException) ex);
			return;
		}
		Throwable e = ex.getCause();
		if (e instanceof OptimisticLockException) {
			addErrorFlashMessage("The item you are trying to modify has changed or deleted.", "Go back and try again!");
			return;
		}
		if (e instanceof RollbackException) {
			addErrorFlashMessage("The item you are trying to modify has changed or deleted.", "Go back and try again!");
			return;
		}
		addErrorFlashMessage(ex.getMessage());
	}

	public static void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context
				.getViewRoot().getViewId());
		viewRoot.getChildren().clear();
		context.setViewRoot(viewRoot);
		context.renderResponse(); // Optional
	}

	@SuppressWarnings("unchecked")
	public static <T extends AbstractInvoiceController> T findInvoiceControllerBean(
			String beanName) {
		return (T) findBean(beanName, AbstractInvoiceController.class);
	}

	public static NavigationBean findNavigationBean() {
		return (NavigationBean) findBean("navigationBean", NavigationBean.class);
	}

	public static final <T extends Object> T findBean(String beanName,
			Class<T> clazz) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().evaluateExpressionGet(context,
				"#{" + beanName + "}", clazz);
	}

	public static boolean isRenderPhase() {
		FacesContext context = FacesContext.getCurrentInstance();
		return (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE);
	}

	public static boolean isRequestPhase() {
		FacesContext context = FacesContext.getCurrentInstance();
		return (context.getCurrentPhaseId() == PhaseId.APPLY_REQUEST_VALUES);
	}

}