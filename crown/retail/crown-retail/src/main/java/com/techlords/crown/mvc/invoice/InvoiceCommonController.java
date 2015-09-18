package com.techlords.crown.mvc.invoice;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.CreditNoteBO;
import com.techlords.crown.business.model.InvoiceBO;
import com.techlords.crown.business.model.InvoicePaymentBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.business.model.enums.PaymentModeBO;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.InvoiceValidator;
import com.techlords.crown.service.InvoiceService;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
/**
 * For Creation List
 * @author gv
 */
public class InvoiceCommonController extends AbstractInvoiceController {
	private static final Logger LOGGER = Logger
			.getLogger(InvoiceCommonController.class);

	@PostConstruct
	public void loadMyInvoices() {
		invoiceModel.setInvoiceStates();

		loadAssociations();
	}

	public String save() {
		return null;
	}

	public String printInvoice() {

		if (currentInvoice.getInvoiceState() == InvoiceStateBO.NEW.getStateID()) {
			InvoiceService service = CrownServiceLocator.INSTANCE
					.getCrownService(InvoiceService.class);
			try {
				service.printInvoice(currentInvoice,
						CrownUserDetailsService.getCurrentUser().getId());
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		FacesUtil.findBean("invoiceControllerFactory",
				InvoiceControllerFactory.class).showView("PRINT",
				getInvoiceType());
		return null;
	}

	public void viewInvoice(InvoiceBO bo) {

		setCurrentInvoice(bo);
//		setAssociations();
		isListLoaded = false;
		navigationBean.setNavigationUrl("invoice/ViewInvoice.xhtml");
	}

	public boolean isCancellable(InvoiceBO invoiceBO) {
		int invState = invoiceBO.getInvoiceState();
		return (invState == InvoiceStateBO.NEW.getStateID())
				|| (invState == InvoiceStateBO.PRINTED.getStateID());
	}

	public void cancelInvoice(InvoiceBO invoiceBO) {

		invoiceBO.setInvoiceStateBO(InvoiceStateBO.CANCELLED);
		invoiceBO.setInvoiceState(InvoiceStateBO.CANCELLED.getStateID());
		InvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(InvoiceService.class);
		try {
			service.cancelInvoice(invoiceBO, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addErrorFlashMessage(e.getMessage());
			e.printStackTrace();
			return;
		}
		navigationBean.setNavigationUrl("invoice/AllInvoices.xhtml");
	}

	public void isCreditNoteUtilized(InvoicePaymentBO pmt) {
		if (pmt.getPaymentMode() != PaymentModeBO.CREDIT_NOTE.getModeID()) {
			pmt.setCreditNoteAvl(true);
			return;
		}
		final InvoiceValidator val = new InvoiceValidator();
		final CreditNoteBO status = val.getCreditNoteStatus(pmt);
		final boolean flag = (status == null || status.isUtilized());
		if (!flag) {
			pmt.setAmount(status.getAmount());
		}
		pmt.setCreditNoteAvl(!flag);
	}

}
