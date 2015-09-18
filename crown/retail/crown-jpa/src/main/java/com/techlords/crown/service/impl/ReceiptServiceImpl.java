package com.techlords.crown.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.OptimisticLockException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.ReceiptBO;
import com.techlords.crown.business.model.ReceiptPaymentBO;
import com.techlords.crown.helpers.AuditActionEnum;
import com.techlords.crown.helpers.GeneralHelper;
import com.techlords.crown.helpers.ReceiptHelper;
import com.techlords.crown.persistence.Bank;
import com.techlords.crown.persistence.Company;
import com.techlords.crown.persistence.Customer;
import com.techlords.crown.persistence.PaymentMode;
import com.techlords.crown.persistence.Receipt;
import com.techlords.crown.persistence.ReceiptPayment;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.InvoiceService;
import com.techlords.crown.service.ReceiptService;

@SuppressWarnings("serial")
final class ReceiptServiceImpl extends AbstractCrownService implements ReceiptService {
	private final ReceiptHelper helper = new ReceiptHelper();
	private static final String RECEIPT_NUM_PREFIX = "REC";

	@Transactional(propagation = Propagation.NESTED)
	@Override
	public ReceiptBO createReceipt(ReceiptBO bo, int userID) throws CrownException {

		final Receipt receipt = helper.createReceipt(bo);
		final GeneralService generalService = CrownServiceLocator.INSTANCE
				.getCrownService(GeneralService.class);
		final String receiptNumber = RECEIPT_NUM_PREFIX + GeneralHelper.getFormattedRunningDate()
				+ generalService.getRunningSequenceNumber("RECEIPT", "RECEIPT_DATE");
		receipt.setReceiptNumber(receiptNumber);
		try {
			final Customer cust = manager.find(Customer.class, bo.getCustomer());
			receipt.setCustomerBean(cust);
			receipt.setIssuingCompany(manager.find(Company.class, bo.getCompany()));
			manager.persist(receipt);
			final Set<ReceiptPayment> payments = new HashSet<ReceiptPayment>();

			for (ReceiptPaymentBO pmtBO : bo.getPayments()) {
				final ReceiptPayment pmt = helper.createReceiptPayment(pmtBO);
				pmt.setReceipt(receipt);
				pmt.setPaymentModeBean(manager.find(PaymentMode.class, pmtBO.getPaymentMode()));
				final Integer bankID = pmtBO.getBank();
				if (bankID != null && bankID > 0) {
					pmt.setBank(manager.find(Bank.class, bankID));
				}
				// PERSIST THE ITEM
				manager.persist(pmt);
				payments.add(pmt);
			}
			receipt.setReceiptPayments(payments);
			manager.merge(receipt);

			// UPDATE CUSTOMER CURRENT CREDIT
			double currentCredit = cust.getCurrentCredit();
			currentCredit -= receipt.getAmount();
			cust.setCurrentCredit(currentCredit);
			manager.persist(cust);

			bo.getCustomerBO().setCurrentCredit(cust.getCurrentCredit());
			bo.setReceiptDate(receipt.getReceiptDate());
			bo.setId(receipt.getReceiptId());

			auditLog(AuditActionEnum.GENERATE_RECEIPT, userID, "receipt",
					receipt.getReceiptNumber());
		} catch (Exception e) {
			throw new CrownException("Receipt cannot be created", e);
		}
		return helper.createReceiptBO(receipt);
	}

	@Transactional
	@Override
	public void printReceipt(ReceiptBO bo, int userID) throws CrownException {
		try {
			final Receipt receipt = manager.find(Receipt.class, bo.getId());
			receipt.setVersion(bo.getVersion());
			boolean isIussed = receipt.isIssued();
			receipt.setIssued(true);
			manager.merge(receipt);
			auditLog(AuditActionEnum.PRINT, userID, "receipt", receipt.getReceiptNumber()
					+ " - receipt was " + (isIussed ? "original" : "duplicate"));
		} catch (OptimisticLockException e) {
			throw new CrownException("Receipt has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
	}

	@Transactional(propagation = Propagation.NESTED)
	@Override
	public boolean amendReceiptPayments(ReceiptBO bo, List<ReceiptPaymentBO> removedCheques,
			int userID) throws CrownException {
		try {
			final Receipt receipt = manager.find(Receipt.class, bo.getId());
			double receiptAmt = receipt.getAmount();// 5000

			final Customer cust = manager.find(Customer.class, bo.getCustomer());
			// UPDATE CUSTOMER CURRENT CREDIT
			double currentCredit = cust.getCurrentCredit();
			// RECEIPT IS REMOVED. ADD CURRENT CREDIT NOW
			currentCredit += receiptAmt;

			final Set<ReceiptPayment> payments = receipt.getReceiptPayments();

			for (ReceiptPaymentBO pmtBO : bo.getPayments()) {
				final ReceiptPayment pmt = helper.createReceiptPayment(pmtBO);
				pmt.setReceipt(receipt);
				pmt.setPaymentModeBean(manager.find(PaymentMode.class, pmtBO.getPaymentMode()));
				final Integer bankID = pmtBO.getBank();
				if (bankID != null && bankID > 0) {
					pmt.setBank(manager.find(Bank.class, bankID));
				}
				receiptAmt += pmt.getAmount();// 0
				// PERSIST THE ITEM
				manager.persist(pmt);
				payments.add(pmt);
			}

			InvoiceService invService = CrownServiceLocator.INSTANCE
					.getCrownService(InvoiceService.class);

			for (ReceiptPaymentBO pmtBO : removedCheques) {
				final ReceiptPayment pmt = manager.find(ReceiptPayment.class, pmtBO.getId());
				// PERSIST THE ITEM
				receiptAmt -= pmt.getAmount();// 5000-3000
				payments.remove(pmt);
				manager.remove(pmt);
				// REMOVE THE INVOICE PAYMENTS
				invService.removeInvoicePayment(pmt.getChequeNumber());
			}
			receipt.setReceiptPayments(payments);
			receipt.setAmount(receiptAmt);// 2000
			manager.merge(receipt);

			// NOW SUBTRACT FROM CURRENT CREDIT
			currentCredit -= receiptAmt;
			cust.setCurrentCredit(currentCredit);
			manager.persist(cust);

			auditLog(AuditActionEnum.AMEND_RECEIPT, userID, "receipt", receipt.getReceiptNumber());
		} catch (OptimisticLockException e) {
			throw new CrownException("Receipt has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReceiptBO> findAllReceipts(int customerID) {
		final Customer cust = manager.find(Customer.class, customerID);
		final List<Receipt> receipts = manager.createNamedQuery("ReceiptPayment.findByCustomer")
				.setParameter(1, cust).getResultList();
		final List<ReceiptBO> bos = new ArrayList<ReceiptBO>();
		for (final Receipt rec : receipts) {
			bos.add(helper.createReceiptBO(rec));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReceiptBO> findAllUnusedReceipts(int customerID) {
		final Customer cust = manager.find(Customer.class, customerID);
		final List<Receipt> receipts = manager
				.createNamedQuery("ReceiptPayment.findUnusedByCustomer").setParameter(1, cust)
				.getResultList();
		final List<ReceiptBO> bos = new ArrayList<ReceiptBO>();
		for (final Receipt rec : receipts) {
			bos.add(helper.createReceiptBO(rec));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReceiptBO> findAllReceipts(String chequeNumber) {

		final Set<ReceiptBO> bos = new HashSet<ReceiptBO>();

		final List<ReceiptPayment> payments = manager
				.createNamedQuery("ReceiptPayment.findByChequeNumber")
				.setParameter(1, chequeNumber).getResultList();

		if (payments == null) {
			return new ArrayList<ReceiptBO>(bos);
		}
		for (ReceiptPayment pmt : payments) {
			bos.add(helper.createReceiptBO(pmt.getReceipt()));
		}
		return new ArrayList<ReceiptBO>(bos);
	}

}
