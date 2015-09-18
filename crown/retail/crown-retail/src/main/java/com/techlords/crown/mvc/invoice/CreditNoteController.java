package com.techlords.crown.mvc.invoice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.CreditNoteBO;
import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.business.model.InvoiceBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.service.InvoiceService;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class CreditNoteController extends AbstractInvoiceController {

	private static final Logger LOGGER = Logger
			.getLogger(CreditNoteController.class);

	private final List<CreditNoteBO> creditNoteBOs = new ArrayList<CreditNoteBO>();
	private int currentInvoiceID;

	private CreditNoteBO currentCreditNote;

	@PostConstruct
	public void loadMyInvoices() {
		invoiceModel.setInvoiceStates(InvoiceStateBO.RETURNED.getStateID());
	}

	public List<InvoiceBO> getInvoiceBOs() {
		creditNoteBOs.clear();
		if (FacesUtil.isRenderPhase()) {
			if (!isListLoaded) {
				invoiceBOs.clear();
				setCurrentInvoiceID(-1);
				final InvoiceService service = CrownServiceLocator.INSTANCE
						.getCrownService(InvoiceService.class);
				invoiceBOs.addAll(service.findAllInvoices(getInvoiceType(),
						InvoiceStateBO.RETURNED.getStateID()));
				isListLoaded = true;
			}
		}
		return invoiceBOs;
	}

	private void loadCreditNotes() {
		if (currentInvoice == null) {
			return;
		}
		creditNoteBOs.clear();
		creditNoteBOs.addAll(currentInvoice.getCreditNotes());
		for (CreditNoteBO bo : creditNoteBOs) {
			bo.setInvoiceBO(currentInvoice);
			bo.setCustomerBO(currentInvoice.getCustomerBO());
			String remarks = bo.getRemarks();
			remarks = remarks.replaceAll("#", "<br/>");
			bo.setRemarks(remarks);
		}
	}

	public final List<CreditNoteBO> getCreditNoteBOs() {
		if (FacesUtil.isRenderPhase()) {
			loadCreditNotes();
		}
		return creditNoteBOs;
	}

	public void setupForm() {

		setCurrentCreditNote(new CreditNoteBO());
		currentCreditNote.setAmount(currentInvoice.getReturnAmount()
				- currentInvoice.getCreditNoteAmount());

		isListLoaded = false;
		navigationBean.setNavigationUrl("invoice/CreateCreditNote.xhtml");
	}

	public void viewCreditNote(CreditNoteBO bo) {
		setCurrentCreditNote(bo);

		isListLoaded = false;
		navigationBean.setNavigationUrl("invoice/ViewCreditNote.xhtml");
	}

	public String create() {
		if (currentCreditNote.getAmount() <= 0.0) {
			FacesUtil.addErrorFlashMessage("Credit Note Amount cannot be zero");
			return null;
		}
		currentCreditNote.setInvoice(currentInvoiceID);
		currentCreditNote.setAmount(currentInvoice.getReturnAmount()
				- currentInvoice.getCreditNoteAmount());
		final CrownUserBO user = CrownUserDetailsService.getCurrentUser();
		String remarks = currentCreditNote.getRemarks();
		remarks += "#Created by ::: " + user.getFirstName() + " "
				+ user.getLastName() + "#";
		currentCreditNote.setRemarks(remarks);

		try {
			InvoiceService service = CrownServiceLocator.INSTANCE
					.getCrownService(InvoiceService.class);
			service.createCreditNote(currentCreditNote, user.getId());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		FacesUtil.findBean("invoiceControllerFactory",
				InvoiceControllerFactory.class).showView("CREDIT_NOTE",
				getInvoiceType());
		return null;
	}

	public String printCreditNote() {

		if (!currentCreditNote.isIssued()) {
			InvoiceService service = CrownServiceLocator.INSTANCE
					.getCrownService(InvoiceService.class);
			try {
				service.issueCreditNote(currentCreditNote.getId(),
						CrownUserDetailsService.getCurrentUser().getId());
			} catch(Exception e) {
				LOGGER.error(e.getMessage(), e);
				FacesUtil.addExceptionMessages(e);
				return null;
			}
		}
		FacesUtil.findBean("invoiceControllerFactory",
				InvoiceControllerFactory.class).showView("CREDIT_NOTE",
				getInvoiceType());
		return null;
	}

	@Override
	public String save() {
		return create();
	}

	public final int getCurrentInvoiceID() {
		return currentInvoiceID;
	}

	public final void setCurrentInvoiceID(int currentInvoiceID) {
		this.currentInvoiceID = currentInvoiceID;
		setCurrentInvoice(getAppModel(currentInvoiceID, invoiceBOs));
	}

	public final CreditNoteBO getCurrentCreditNote() {
		return currentCreditNote;
	}

	public final void setCurrentCreditNote(CreditNoteBO currentCreditNote) {
		this.currentCreditNote = currentCreditNote;
	}
}
