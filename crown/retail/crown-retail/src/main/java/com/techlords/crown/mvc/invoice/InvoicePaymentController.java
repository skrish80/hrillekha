package com.techlords.crown.mvc.invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CreditNoteBO;
import com.techlords.crown.business.model.InvoiceBO;
import com.techlords.crown.business.model.InvoiceItemBO;
import com.techlords.crown.business.model.InvoicePaymentBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.business.model.enums.PaymentModeBO;
import com.techlords.crown.business.model.enums.PaymentStatusBO;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.InvoiceValidator;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.InvoiceService;
import com.techlords.infra.CrownConstants;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class InvoicePaymentController extends AbstractInvoiceController {

	private final List<InvoicePaymentBO> existingPayments = new ArrayList<InvoicePaymentBO>();
	private final List<InvoicePaymentBO> removedCheques = new ArrayList<InvoicePaymentBO>();
	private double totalPaidAmt = 0d;
	private String payer;

	@PostConstruct
	public void loadMyInvoices() {

		final ExecutorService executorService = Executors.newFixedThreadPool(2);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				invoiceModel.setInvoiceStates(InvoiceStateBO.CANCELLED
						.getStateID());
				invoiceModel.setAmendPayment(true);
			}
		});
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				final GeneralService generalService = CrownServiceLocator.INSTANCE
						.getCrownService(GeneralService.class);
				bankBOs.clear();
				bankBOs.addAll(generalService.findAllBanks());
			}
		});

		executorService.shutdown();
		try {
			executorService.awaitTermination(CrownConstants.WAIT_TIME,
					TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			e.printStackTrace();
		}
	}

	public void removeInvoicePayment(InvoicePaymentBO bo) {
		currentInvoice.removeInvoicePayment(bo);
		totalPaidAmt -= bo.getAmount();
	}

	public void removeExistingPayment(InvoicePaymentBO bo) {
		existingPayments.remove(bo);
		removedCheques.add(bo);
		totalPaidAmt -= bo.getAmount();
	}

	public void addPayment(InvoicePaymentBO bo) {
		totalPaidAmt = 0;
		for (InvoicePaymentBO pmt : existingPayments) {
			totalPaidAmt += pmt.getAmount();
		}
		totalPaidAmt += currentInvoice.getTotalPaidAmount();
	}

	public void setupForm(InvoiceBO bo) {
		setCurrentInvoice(bo);
		setPayer(null);
		itemBOs.clear();
		for (InvoiceItemBO invItem : currentInvoice.getInvoiceItems()) {
			itemBOs.add(invItem.getItemBO());
		}
		existingPayments.clear();
		removedCheques.clear();
		existingPayments.addAll(currentInvoice.getInvoicePayments());
		totalPaidAmt = currentInvoice.getTotalPaidAmount();

		currentInvoice.getInvoicePayments().clear();
		navigationBean.setNavigationUrl("invoice/InvoiceAmendPayment.xhtml");
		isListLoaded = false;
	}

	public String updatePayment() {
		if (currentInvoice.getInvoicePayments().isEmpty()) {
			FacesUtil.addCrownExceptionMessages(new CrownException(
					"Add Payments First"));
			return null;
		}
		currentInvoice.setTotalPaidAmount(totalPaidAmt);
		for (InvoicePaymentBO pmt : currentInvoice.getInvoicePayments()) {
			pmt.setPayer(payer);
		}
		currentInvoice.setPaymentStatusBO(PaymentStatusBO
				.valueOf(currentInvoice.getPaymentStatus()));

		final InvoiceValidator validator = new InvoiceValidator();
		try {
			validator.validateInvoicePayment(totalPaidAmt, currentInvoice);
		} catch(Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		InvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(InvoiceService.class);
		try {
			service.updateInvoicePayments(currentInvoice, removedCheques,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch(Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		existingPayments.clear();
		FacesUtil.findBean("invoiceControllerFactory",
				InvoiceControllerFactory.class).showView("PAY",
				getInvoiceType());
		return null;
	}

	@Override
	public String save() {
		return updatePayment();
	}

	public final List<InvoicePaymentBO> getExistingPayments() {
		return existingPayments;
	}

	public final double getTotalPaidAmt() {
		return totalPaidAmt;
	}

	public final void setTotalPaidAmt(double totalPaidAmt) {
		this.totalPaidAmt = totalPaidAmt;
	}

	public final String getPayer() {
		return payer;
	}

	public final void setPayer(String payer) {
		this.payer = payer;
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
			addPayment(pmt);
		}
		pmt.setCreditNoteAvl(!flag);
	}
}
