package com.techlords.crown.mvc.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.BankBO;
import com.techlords.crown.business.model.CompanyBO;
import com.techlords.crown.business.model.CustomerBO;
import com.techlords.crown.business.model.ReceiptBO;
import com.techlords.crown.business.model.ReceiptPaymentBO;
import com.techlords.crown.business.model.enums.PaymentModeBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.ReceiptValidator;
import com.techlords.crown.service.CompanyService;
import com.techlords.crown.service.CustomerService;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.ReceiptService;
import com.techlords.infra.CrownConstants;

@ManagedBean
@SessionScoped
public class ReceiptController extends CrownModelController {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger
			.getLogger(ReceiptController.class);

	private final List<CustomerBO> customerBOs = new ArrayList<CustomerBO>();
	private final List<CompanyBO> companyBOs = new ArrayList<CompanyBO>();
	private final List<ReceiptBO> receiptBOs = new ArrayList<ReceiptBO>();
	protected final List<PaymentModeBO> paymentModeBOs = new ArrayList<PaymentModeBO>(
			Arrays.asList(PaymentModeBO.values()));
	protected final List<BankBO> bankBOs = new ArrayList<BankBO>();
	private final List<ReceiptPaymentBO> existingPayments = new ArrayList<ReceiptPaymentBO>();
	private final List<ReceiptPaymentBO> removedPayments = new ArrayList<ReceiptPaymentBO>();

	private CustomerBO currentCustomer;
	private ReceiptBO currentReceipt;
	private int currentCustomerID;

	private double totalPaidAmt = 0d;

	@PostConstruct
	public void removeCreditNoteMode() {
		paymentModeBOs.remove(PaymentModeBO.CREDIT_NOTE);
	}

	public List<CustomerBO> getCustomerBOs() {
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			customerBOs.clear();
			receiptBOs.clear();
			existingPayments.clear();
			removedPayments.clear();
			setCurrentCustomer(null);
			setCurrentCustomerID(-1);
			final CustomerService service = CrownServiceLocator.INSTANCE
					.getCrownService(CustomerService.class);
			customerBOs.addAll(service.findAllCustomers());
			isListLoaded = true;
		}
		return customerBOs;
	}

	public void loadCreditLimitForm() {
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/UpdateCreditLimit.xhtml");
	}

	public void updateCreditLimit() {
		final double creditLimit = currentCustomer.getCreditLimit();
		if (creditLimit <= 0) {
			FacesUtil.addErrorFlashMessage("Credit Limit Error",
					"Credit Limit cannot be zero");
			return;
		}

		final CustomerService service = CrownServiceLocator.INSTANCE
				.getCrownService(CustomerService.class);
		try {
			service.updateCreditLimit(currentCustomer, creditLimit,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return;
		}
		navigationBean.setNavigationUrl("config/CreditReceipt.xhtml");
	}

	public void loadReceipts() {
		final ReceiptService service = CrownServiceLocator.INSTANCE
				.getCrownService(ReceiptService.class);
		receiptBOs.clear();
		receiptBOs.addAll(service.findAllReceipts(currentCustomerID));
	}

	public void addPayment() {
		currentReceipt.addPayment(new ReceiptPaymentBO());
	}

	public void removePayment(ReceiptPaymentBO bo) {
		currentReceipt.removePayment(bo);
		totalPaidAmt -= bo.getAmount();
	}

	public void removeExistingPayment(ReceiptPaymentBO bo) {
		existingPayments.remove(bo);
		removedPayments.add(bo);
		totalPaidAmt -= bo.getAmount();
	}

	public void addPayment(ReceiptPaymentBO bo) {
		totalPaidAmt = 0;
		for (ReceiptPaymentBO pmt : existingPayments) {
			totalPaidAmt += pmt.getAmount();
		}
		totalPaidAmt += currentReceipt.getAmount();
	}

	private void loadAssociations() {
		final ExecutorService executorService = Executors.newFixedThreadPool(2);

		executorService.execute(new Runnable() {

			@Override
			public void run() {
				companyBOs.clear();
				CompanyService service = CrownServiceLocator.INSTANCE
						.getCrownService(CompanyService.class);
				companyBOs.addAll(service.findAllCompanies());
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

	public void loadReceiptForm() {
		loadReceiptForm(new ReceiptBO());
	}

	public void loadReceiptForm(ReceiptBO bo) {
		setCurrentReceipt(bo);
		loadAssociations();
		totalPaidAmt = currentReceipt.getAmount();
		if (!bo.isNew()) {
			removedPayments.clear();
			existingPayments.addAll(currentReceipt.getPayments());
			currentReceipt.getPayments().clear();
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/UpdateReceipt.xhtml");
	}

	public void viewReceipt(ReceiptBO receipt) {
		setCurrentReceipt(receipt);
		navigationBean.setNavigationUrl("config/ViewReceipt.xhtml");
	}

	@Override
	public String save() {
		currentReceipt.setCustomer(currentCustomerID);
		return currentReceipt.isNew() ? create() : update();
	}

	private String create() {
		final ReceiptValidator validator = new ReceiptValidator();
		try {
			validator.validateReceipt(currentReceipt);
		} catch (Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}

		for (ReceiptPaymentBO pmt : currentReceipt.getPayments()) {
			pmt.setPayer(currentCustomer.getCustomerName());
			pmt.setPaymentModeBO(PaymentModeBO.valueOf(pmt.getPaymentMode()));
			if (pmt.getPaymentModeBO() != PaymentModeBO.CASH) {
				pmt.setBankBO(getAppModel(pmt.getBank(), bankBOs));
			}
		}

		currentReceipt.setCustomerBO(currentCustomer);
		currentReceipt.setCompanyBO(getAppModel(currentReceipt.getCompany(),
				companyBOs));

		final ReceiptService service = CrownServiceLocator.INSTANCE
				.getCrownService(ReceiptService.class);
		try {
			currentReceipt = service.createReceipt(currentReceipt,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		viewReceipt(currentReceipt);
		return null;
	}

	public void printReceipt() {
		if (!currentReceipt.isIssued()) {
			final ReceiptService service = CrownServiceLocator.INSTANCE
					.getCrownService(ReceiptService.class);
			try {
				service.printReceipt(currentReceipt, CrownUserDetailsService
						.getCurrentUser().getId());
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				FacesUtil.addExceptionMessages(e);
				return;
			}
		}
		navigationBean.setNavigationUrl("config/CreditReceipt.xhtml");
	}

	private String update() {
		final ReceiptValidator validator = new ReceiptValidator();
		try {
			validator.validateReceipt(currentReceipt);
		} catch (Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		final ReceiptService service = CrownServiceLocator.INSTANCE
				.getCrownService(ReceiptService.class);
		try {
			service.amendReceiptPayments(currentReceipt, removedPayments,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch (Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("config/CreditReceipt.xhtml");
		// REDIRECT TO PRINT
		return null;
	}

	public final CustomerBO getCurrentCustomer() {
		return currentCustomer;
	}

	public final void setCurrentCustomer(CustomerBO currentCustomer) {
		this.currentCustomer = currentCustomer;
	}

	public final int getCurrentCustomerID() {
		return currentCustomerID;
	}

	public final void setCurrentCustomerID(int currentCustomerID) {
		this.currentCustomerID = currentCustomerID;
		setCurrentCustomer(getAppModel(currentCustomerID, customerBOs));
	}

	public final List<ReceiptBO> getReceiptBOs() {
		return receiptBOs;
	}

	public final List<CompanyBO> getCompanyBOs() {
		return companyBOs;
	}

	public final ReceiptBO getCurrentReceipt() {
		return currentReceipt;
	}

	public final void setCurrentReceipt(ReceiptBO currentReceipt) {
		this.currentReceipt = currentReceipt;
	}

	public final List<PaymentModeBO> getPaymentModeBOs() {
		return paymentModeBOs;
	}

	public final List<BankBO> getBankBOs() {
		return bankBOs;
	}

	public final double getTotalPaidAmt() {
		return totalPaidAmt;
	}

	public final void setTotalPaidAmt(double totalPaidAmt) {
		this.totalPaidAmt = totalPaidAmt;
	}

	public final List<ReceiptPaymentBO> getExistingPayments() {
		return existingPayments;
	}

}
