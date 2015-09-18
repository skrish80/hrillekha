/**
 * 
 */
package com.techlords.crown.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.OptimisticLockException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CreditNoteBO;
import com.techlords.crown.business.model.InvoiceBO;
import com.techlords.crown.business.model.InvoiceItemBO;
import com.techlords.crown.business.model.InvoicePaymentBO;
import com.techlords.crown.business.model.InvoiceReturnBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.business.model.enums.PaymentModeBO;
import com.techlords.crown.business.model.enums.PaymentStatusBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.helpers.AuditActionEnum;
import com.techlords.crown.helpers.GeneralHelper;
import com.techlords.crown.helpers.InvoiceHelper;
import com.techlords.crown.helpers.ItemHelper;
import com.techlords.crown.helpers.QueryBuilder;
import com.techlords.crown.persistence.Agent;
import com.techlords.crown.persistence.AllocationType;
import com.techlords.crown.persistence.Bank;
import com.techlords.crown.persistence.Company;
import com.techlords.crown.persistence.CreditNote;
import com.techlords.crown.persistence.CrownEntity;
import com.techlords.crown.persistence.CrownUser;
import com.techlords.crown.persistence.Currency;
import com.techlords.crown.persistence.Customer;
import com.techlords.crown.persistence.DiscountType;
import com.techlords.crown.persistence.Invoice;
import com.techlords.crown.persistence.InvoiceItem;
import com.techlords.crown.persistence.InvoiceItemPK;
import com.techlords.crown.persistence.InvoicePayment;
import com.techlords.crown.persistence.InvoiceReturn;
import com.techlords.crown.persistence.InvoiceState;
import com.techlords.crown.persistence.Item;
import com.techlords.crown.persistence.PaymentMode;
import com.techlords.crown.persistence.PaymentStatus;
import com.techlords.crown.persistence.Receipt;
import com.techlords.crown.persistence.Status;
import com.techlords.crown.persistence.TotalStock;
import com.techlords.crown.persistence.Warehouse;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.InvoiceService;
import com.techlords.crown.service.StockService;
import com.techlords.crown.service.WarehouseService;
import com.techlords.infra.CrownConstants;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
final class InvoiceServiceImpl extends AbstractCrownService implements InvoiceService {
	private final InvoiceHelper helper = new InvoiceHelper();
	private static final String INVOICE_NUM_PREFIX = "INV";
	private static final String CREDITNOTE_PREFIX = "CN";
	private int filteredInvoiceCount;
	private int filteredBulkItemsCount;

	// ////////////////INVIOCE//////////////////////////////////
	private void updateInvoiceAssociations(Invoice invoice, InvoiceBO bo, int userID) {
		invoice.setAgent(manager.find(Agent.class, bo.getAgent()));
		invoice.setCrownEntity(manager.find(CrownEntity.class, bo.getEntity()));
		invoice.setCustomer(manager.find(Customer.class, bo.getCustomer()));
		// CURRENTLY DOING WITH SEYCHELLES RUPEES ONLY
		/* bo.getCurrencyBO().getCurrencyCode() */
		invoice.setCurrencyBean(manager.find(Currency.class, "SCR"));
		invoice.setDiscountTypeBean(manager.find(DiscountType.class, bo.getDiscountType()));
		invoice.setDeliveryWarehouse(manager.find(Warehouse.class, bo.getDeliveryWarehouse()));
		invoice.setPaymentStatusBean(manager.find(PaymentStatus.class, bo.getPaymentStatus()));
		invoice.setInvoiceStateBean(manager.find(InvoiceState.class, bo.getInvoiceState()));
		invoice.setStatusBean(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));

		invoice.setCreatedBy(manager.find(CrownUser.class, userID));
		invoice.setCompany(manager.find(Company.class, bo.getCompany()));
		invoice.setVersion(bo.getVersion());
	}

	@Transactional
	private void createInvoiceItems(Invoice invoice, InvoiceBO bo) {
		final List<InvoiceItemBO> itemBOs = bo.getInvoiceItems();
		final Set<InvoiceItem> items = new HashSet<InvoiceItem>();
		for (InvoiceItemBO itemBO : itemBOs) {
			InvoiceItem item = helper.createInvoiceItem(invoice.getInvoiceId(), itemBO);
			// PERSIST THE ITEM
			item.setItem(manager.find(Item.class, itemBO.getItem()));
			manager.persist(item);

			items.add(item);
		}
		invoice.setInvoiceItems(items);
	}

	@Transactional
	private void createInvoiceReturns(Invoice invoice, InvoiceBO bo) throws CrownException {
		final List<InvoiceReturnBO> returnBOs = bo.getInvoiceReturns();
		final WarehouseService warehouseService = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);

		Set<InvoiceReturn> invoiceReturns = invoice.getInvoiceReturns();
		if (invoiceReturns == null) {
			invoiceReturns = new HashSet<InvoiceReturn>();
		}

		for (InvoiceReturnBO retBO : returnBOs) {
			final InvoiceReturn ret = helper.createInvoiceReturn(retBO);
			ret.setInvoice(invoice);
			ret.setItem(manager.find(Item.class, retBO.getItem()));
			ret.setReturnType(manager.find(AllocationType.class, retBO.getReturnType()));

			manager.persist(ret);
			invoiceReturns.add(ret);

			// UPDATE WAREHOUSE STOCK
			// IF RETURN IS DAMAGED, user has to explicitly go to
			// Stolen/Damaged/Unpacked stock to update the return item & qty
			warehouseService.updateWarehouseStock(bo.getDeliveryWarehouse(), retBO.getItem(),
					retBO.getReturnType(), retBO.getReturnQty(), false);
		}
		invoice.setInvoiceReturns(invoiceReturns);
	}

	@Transactional(propagation = Propagation.NESTED)
	private void createInvoicePayments(Invoice invoice, InvoiceBO bo, boolean isUpdate, int userID)
			throws CrownException {
		final List<InvoicePaymentBO> paymentBOs = bo.getInvoicePayments();
		Set<InvoicePayment> payments = invoice.getInvoicePayments();
		if (payments == null) {
			payments = new HashSet<InvoicePayment>();
		}
		for (InvoicePaymentBO paymentBO : paymentBOs) {
			InvoicePayment payment = helper.createInvoicePayment(invoice.getInvoiceId(), paymentBO);
			payment.setInvoice(invoice);
			payment.setPaymentModeBean(manager.find(PaymentMode.class, paymentBO.getPaymentMode()));
			if (paymentBO.getPaymentMode() == PaymentModeBO.CREDIT_NOTE.getModeID()) {
				updateCreditNoteUsage(paymentBO.getDraftNumber(), userID);
			}
			if (paymentBO.getPaymentMode() == PaymentModeBO.RECEIPT.getModeID()) {
				updateReceiptUsage(paymentBO, userID);
			}
			final Integer bankID = paymentBO.getBank();
			if (bankID != null && bankID > 0) {
				payment.setBank(manager.find(Bank.class, bankID));
			}
			// PERSIST THE ITEM
			manager.persist(payment);

			payments.add(payment);
		}
		// if (isUpdate) {
		// payments.addAll(invoice.getInvoicePayments());
		// }
		invoice.setInvoicePayments(payments);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private void updateCreditNoteUsage(String draftNumber, int userID) throws CrownException {
		final List<CreditNote> creditNotes = manager.createNamedQuery("CreditNote.findByNumber")
				.setParameter(1, draftNumber).getResultList();
		if (creditNotes.size() > 1) {
			throw new CrownException("Duplicate Credit Notes Number!");
		}
		for (CreditNote note : creditNotes) {
			note.setUtilized(true);
			manager.merge(note);
			auditLog(AuditActionEnum.UTILIZED, userID, "creditNote", draftNumber);
		}
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private void updateReceiptUsage(InvoicePaymentBO paymentBO, int userID) throws CrownException {
		final String draftNumber = paymentBO.getDraftNumber();
		final double pmtAmount = paymentBO.getAmount();
		final List<Receipt> receipts = manager.createNamedQuery("Receipt.findByNumber")
				.setParameter(1, draftNumber).getResultList();
		if (receipts.size() > 1) {
			throw new CrownException("Duplicate Receipt Number!");
		}

		for (Receipt note : receipts) {
			final double alreadyUsedAmt = note.getUsedAmount();
			final double usableAmount = note.getAmount() - alreadyUsedAmt;
			if (pmtAmount > usableAmount) {
				throw new CrownException("Amount Error! You can use up to a maximum of "
						+ usableAmount);
			}
			note.setUsedAmount(alreadyUsedAmt + pmtAmount);

			note.setUsedState((note.getUsedAmount() == note.getAmount()) ? CrownConstants.RECEIPT_FULLYUSED
					: (note.getUsedAmount() < note.getAmount()) ? CrownConstants.RECEIPT_PARTLYUSED
							: CrownConstants.RECEIPT_UNUSED);

			// if (note.getUsedAmount() == note.getAmount()) {
			// note.setUsedState(CrownConstants.RECEIPT_FULLYUSED);
			// } else if (note.getUsedAmount() < note.getAmount()) {
			// note.setUsedState(CrownConstants.RECEIPT_PARTLYUSED);
			// } else {
			// note.setUsedState(CrownConstants.RECEIPT_UNUSED);
			// }
			note.setIssued(true);
			manager.merge(note);
			auditLog(AuditActionEnum.UTILIZED, userID, "receipt", draftNumber);

			// UPDATE CUSTOMER CREDIT
			final Customer cust = note.getCustomerBean();
			double currCredit = cust.getCurrentCredit();
			currCredit += pmtAmount;
			cust.setCurrentCredit(currCredit);
		}
	}

	@Transactional(propagation = Propagation.NESTED)
	@Override
	public InvoiceBO createInvoice(InvoiceBO bo, int userID) throws CrownException {
		Invoice invoice = helper.createInvoice(bo);
		try {
			final GeneralService generalService = CrownServiceLocator.INSTANCE
					.getCrownService(GeneralService.class);
			final String invoiceNumber = INVOICE_NUM_PREFIX
					+ GeneralHelper.getFormattedRunningDate()
					+ generalService.getRunningSequenceNumber("INVOICE", "INVOICE_DATE");
			invoice.setInvoiceNumber(invoiceNumber);
			updateInvoiceAssociations(invoice, bo, userID);
			manager.persist(invoice);

			// ASSOCIATE THE INVOICE ITEMS
			createInvoiceItems(invoice, bo);
			createInvoicePayments(invoice, bo, false, userID);
			manager.merge(invoice);

			// Customer CREDIT HISTORY
			if (bo.getPaymentStatus() != PaymentStatusBO.FULL_PAYMENT.getStatusID()) {
				final Customer cust = manager.find(Customer.class, bo.getCustomer());
				double currCredit = cust.getCurrentCredit();
				double invoiceCreditAmount = (bo.getFinalInvoiceAmount() - bo.getTotalPaidAmount());
				cust.setCurrentCredit(currCredit + invoiceCreditAmount);
				manager.merge(cust);
			}
			auditLog(AuditActionEnum.CREATE, userID, "invoice", invoice.getInvoiceNumber());

			manager.flush();
		} catch (Exception e) {
			throw new CrownException("Invoice cannot be created", e);
		}
		return helper.createInvoiceBO(invoice);
	}

	@Transactional(propagation = Propagation.NESTED)
	@Override
	public boolean deliverInvoice(InvoiceBO bo, int userID) throws CrownException {
		try {
			final Invoice invoice = helper.createInvoice(bo,
					manager.find(Invoice.class, bo.getId()));
			invoice.setDeliveryWarehouse(manager.find(Warehouse.class, bo.getDeliveryWarehouse()));
			deliverInvoiceItems(invoice, bo, true);
			auditLog(AuditActionEnum.DELIVER, userID, "invoice", invoice.getInvoiceNumber());
		} catch (OptimisticLockException e) {
			throw new CrownException("Invoice has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional(propagation = Propagation.NESTED)
	@Override
	public boolean returnInvoice(InvoiceBO bo, int userID) throws CrownException {
		try {
			final Invoice invoice = helper.createInvoice(bo,
					manager.find(Invoice.class, bo.getId()));
			invoice.setInvoiceStateBean(manager.find(InvoiceState.class, bo.getInvoiceState()));
			createInvoiceReturns(invoice, bo);
			auditLog(AuditActionEnum.RETURN, userID, "invoice", invoice.getInvoiceNumber());
		} catch (OptimisticLockException e) {
			throw new CrownException("Invoice has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional(propagation = Propagation.NESTED)
	private void deliverInvoiceItems(final Invoice invoice, final InvoiceBO bo,
			final boolean isDelivered) throws CrownException {
		invoice.setInvoiceStateBean(manager.find(InvoiceState.class, bo.getInvoiceState()));
		final List<InvoiceItemBO> itemBOs = bo.getInvoiceItems();
		final WarehouseService warehouseService = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);
		final StockService stockService = CrownServiceLocator.INSTANCE
				.getCrownService(StockService.class);

		for (final InvoiceItemBO itemBO : itemBOs) {
			final InvoiceItemPK pk = new InvoiceItemPK();
			pk.setInvoiceId(itemBO.getId());
			pk.setItemId(itemBO.getItem());
			pk.setAllocationType(itemBO.getAllocationType());

			final InvoiceItem item = manager.find(InvoiceItem.class, pk);
			final Integer updateQty = isDelivered ? itemBO.getDeliveredQty() : itemBO
					.getReturnedQty();
			int allocationType = itemBO.getAllocationType();
			Integer alreadyDeliveredQty = item.getDeliveredQty();
			if (alreadyDeliveredQty == null) {
				alreadyDeliveredQty = 0;
			}
			item.setDeliveredQty(updateQty + alreadyDeliveredQty);
			manager.merge(item);
			// UPDATE WAREHOUSE STOCK
			// IF RETURN IS DAMAGED, user has to explicitly go to
			// Stolen/Damaged/Unpacked stock to update the return item & qty
			warehouseService.updateWarehouseStock(bo.getDeliveryWarehouse(), pk.getItemId(),
					allocationType, updateQty, isDelivered);

			if (isDelivered) {
				// UPDATE THE ENTITY STOCK ALLOCATION ONLY ON DELIVERY
				stockService.updateStockAllocationOnDelivery(bo.getEntity(), itemBO.getItem(),
						allocationType, updateQty);
			}
		}

		manager.merge(invoice);
		manager.getEntityManagerFactory().getCache().evict(TotalStock.class);
	}

	@Transactional(propagation = Propagation.NESTED)
	@Override
	public boolean updateInvoicePayments(InvoiceBO bo, List<InvoicePaymentBO> removedCheques,
			int userID) throws CrownException {
		try {
			final Invoice invoice = manager.find(Invoice.class, bo.getId());
			invoice.setVersion(bo.getVersion());
			createInvoicePayments(invoice, bo, true, userID);

			invoice.setPaymentStatusBean(manager.find(PaymentStatus.class, bo.getPaymentStatus()));

			// REMOVE CHEQUES FROM INVOICE PAYMENTS
			final Set<InvoicePayment> payments = invoice.getInvoicePayments();
			for (InvoicePaymentBO paymentBO : removedCheques) {
				InvoicePayment payment = manager.find(InvoicePayment.class, paymentBO.getId());
				payment.setVersion(paymentBO.getVersion());
				// REMOVE THE ITEM
				manager.remove(payment);
				payments.remove(payment);
			}
			invoice.setInvoicePayments(payments);
			if (!removedCheques.isEmpty()) {
				invoice.setPaymentStatusBean(manager.find(PaymentStatus.class,
						PaymentStatusBO.PARTIAL_PAYMENT.getStatusID()));
			}
			auditLog(AuditActionEnum.UPDATE_PMT, userID, "invoice", invoice.getInvoiceNumber());
		} catch (OptimisticLockException e) {
			throw new CrownException("Invoice has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}

		return true;
	}

	@Transactional
	@Override
	public boolean cancelInvoice(InvoiceBO bo, int userID) throws CrownException {
		try {
			final Invoice invoice = manager.find(Invoice.class, bo.getId());
			invoice.setVersion(bo.getVersion());
			invoice.setInvoiceStateBean(manager.find(InvoiceState.class, bo.getInvoiceState()));
			manager.merge(invoice);
			auditLog(AuditActionEnum.CANCEL, userID, "invoice", invoice.getInvoiceNumber());
		} catch (OptimisticLockException e) {
			throw new CrownException("Invoice has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceBO> findAllInvoices(String invoiceType) {
		final List<Invoice> invoices = manager.createNativeQuery(
				"select I from Invoice I where I.invoiceType = " + invoiceType
						+ "order by I.invoiceDate desc").getResultList();

		final List<InvoiceBO> bos = new ArrayList<InvoiceBO>();
		for (final Invoice inv : invoices) {
			bos.add(helper.createInvoiceBO(inv));
		}
		return bos;
	}

	@Transactional
	@Override
	public void printInvoice(InvoiceBO bo, int userID) throws CrownException {
		try {
			final Invoice invoice = manager.find(Invoice.class, bo.getId());
			invoice.setVersion(bo.getVersion());
			invoice.setInvoiceStateBean(manager.find(InvoiceState.class,
					InvoiceStateBO.PRINTED.getStateID()));
			manager.persist(invoice);
			auditLog(AuditActionEnum.PRINT, userID, "invoice", invoice.getInvoiceNumber());
		} catch (OptimisticLockException e) {
			throw new CrownException("Invoice has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceBO> findAllInvoices(String invoiceType, Object... invoiceStates) {
		if (invoiceStates == null || invoiceStates.length == 0) {
			return findAllInvoices(invoiceType);
		}
		final GeneralHelper generalHelper = new GeneralHelper();
		final String invoiceQuery = generalHelper
				.buildInvoiceStateQuery(invoiceType, invoiceStates);
		Query query = getNativeQuery(invoiceQuery, Invoice.class);
		final List<Invoice> invoices = query.getResultList();
		final List<InvoiceBO> bos = new ArrayList<InvoiceBO>();
		for (final Invoice inv : invoices) {
			bos.add(helper.createInvoiceBO(inv));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceBO> findPaymentPendingInvoices() {
		Query qq = manager
				.createNamedQuery("Invoice.paymentPendingInvoices")
				.setParameter("invState", InvoiceStateBO.CANCELLED.getStateID())
				.setParameter(
						"paymentStatuses",
						Arrays.asList(PaymentStatusBO.CREDIT_SALES.getStatusID(),
								PaymentStatusBO.PARTIAL_PAYMENT.getStatusID()));

		// final GeneralHelper generalHelper = new GeneralHelper();
		// final String invoiceQuery = generalHelper.buildPaymentInvoiceQuery();
		// Query query = getNativeQuery(invoiceQuery, Invoice.class);
		final List<Invoice> invoices = qq.getResultList();
		final List<InvoiceBO> bos = new ArrayList<InvoiceBO>();
		for (final Invoice inv : invoices) {
			bos.add(helper.createInvoiceBO(inv));
		}
		return bos;
	}

	private void validateCreditNoteCreation(Invoice invoice) throws CrownException {
		final double returnAmt = invoice.getReturnAmount();
		double sumCredAmt = 0;
		for (CreditNote cn : invoice.getCreditNotes()) {
			sumCredAmt += cn.getAmount();
		}
		if (returnAmt <= sumCredAmt) {
			throw new CrownException(
					"Credit Note Limit EXCEEDED for Invoice (Credit Note already created) "
							+ invoice.getInvoiceNumber());
		}
	}

	@Transactional
	@Override
	public boolean createCreditNote(CreditNoteBO bo, int userID) throws CrownException {
		try {
			final Invoice invoice = manager.find(Invoice.class, bo.getInvoice());
			validateCreditNoteCreation(invoice);
			final CreditNote note = helper.createCreditNote(bo, invoice);

			final GeneralService generalService = CrownServiceLocator.INSTANCE
					.getCrownService(GeneralService.class);
			final String creditNoteNumber = CREDITNOTE_PREFIX
					+ GeneralHelper.getFormattedRunningDate()
					+ generalService.getRunningSequenceNumber("CREDIT_NOTE", "ISSUE_DATE");
			note.setNoteNumber(creditNoteNumber);
			manager.persist(note);
			Set<CreditNote> creditNotes = invoice.getCreditNotes();
			if (creditNotes == null) {
				creditNotes = new HashSet<CreditNote>();
			}
			creditNotes.add(note);
			invoice.setCreditNotes(creditNotes);
			auditLog(AuditActionEnum.CREATE, userID, "creditNote", note.getNoteNumber());
			bo.setId(note.getCreditNoteId());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Invoice/Credit Note has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean issueCreditNote(int creditNoteID, int userID) throws CrownException {
		try {
			CreditNote note = manager.find(CreditNote.class, creditNoteID);
			note.setIssued(true);
			manager.persist(note);
			auditLog(AuditActionEnum.PRINT, userID, "creditNote", note.getNoteNumber());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Credit Note has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceItemBO> findUndeliveredInvoiceItems(Integer entityID, Object... itemIDs) {
		final GeneralHelper generalHelper = new GeneralHelper();
		final String stockAllocationQuery = generalHelper.buildUndeliveredInvoiceItemsQuery(
				entityID, itemIDs);
		// SELECT ITEM_ID, SUM(ITEM_QTY), ALLOCATION_TYPE FROM INVOICE_ITEMS
		// WHERE ITEM_ID IN (1,2) AND INVOICE_ID IN (SELECT INVOICE_ID FROM
		// INVOICE WHERE INVOICE_STATE IN (1,2,6)) GROUP BY
		// ITEM_ID,ALLOCATION_TYPE
		// TODO no other go
		final Query query = getNativeQuery(stockAllocationQuery);
		final List<InvoiceItemBO> bos = new ArrayList<InvoiceItemBO>();
		final List<Object[]> list = query.getResultList();
		for (Object[] obj : list) {
			final InvoiceItemBO bo = new InvoiceItemBO();
			bo.setItem((Integer) obj[0]);
			bo.setItemQty(((Long) obj[1]).intValue());
			bo.setAllocationType((Integer) obj[2]);
			bos.add(bo);
		}
		return bos;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CreditNoteBO> isCreditNoteUtilized(String draftNumber) {
		final List<CreditNote> creditNotes = manager.createNamedQuery("CreditNote.findByNumber")
				.setParameter(1, draftNumber).getResultList();
		List<CreditNoteBO> bos = new ArrayList<CreditNoteBO>();

		for (CreditNote note : creditNotes) {
			bos.add(helper.createCreditNoteBO(note));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void removeInvoicePayment(String chequeNumber) throws CrownException {
		try {
			final List<InvoicePayment> payments = manager
					.createNamedQuery("InvoicePayment.findByChequeNum")
					.setParameter(1, chequeNumber).getResultList();

			if (payments == null) {
				return;
			}

			for (InvoicePayment pmt : payments) {
				final Invoice invoice = pmt.getInvoice();
				invoice.setPaymentStatusBean(manager.find(PaymentStatus.class,
						PaymentStatusBO.PARTIAL_PAYMENT.getStatusID()));
				manager.remove(pmt);
				invoice.getInvoicePayments().remove(pmt);
				manager.merge(invoice);
			}
		} catch (OptimisticLockException e) {
			throw new CrownException("Invoice has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
	}

	/**
	 * FILTERING & LAZY LOAD
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceBO> findAllInvoices(String invoiceType, int first, int pageSize,
			Map<String, String> filters, boolean isAmendPayment, int entity,
			Object... invoiceStates) {

		final List<InvoiceBO> bos = new ArrayList<InvoiceBO>();
		final Query filterQuery = createFilterQuery(invoiceType, isAmendPayment, filters, entity,
				invoiceStates);
		if (filterQuery == null) {
			setFilteredInvoiceCount(findInvoiceCount(invoiceType));
			return bos;
		}

		setFilteredInvoiceCount(filterQuery.getResultList().size());

		Query query = filterQuery.setFirstResult(first).setMaxResults(pageSize);
		final List<Invoice> invoices = query.getResultList();
		for (final Invoice invoice : invoices) {
			bos.add(helper.createInvoiceBO(invoice));
		}
		return bos;
	}

	private Query createFilterQuery(String invoiceType, boolean isAmendPayment,
			Map<String, String> filters, int entity, Object... invoiceStates) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Invoice> query = builder.createQuery(Invoice.class);
		Root<Invoice> invoice = query.from(Invoice.class);
		query.select(invoice);

		List<Predicate> predicateList = new ArrayList<Predicate>();
		predicateList.add(builder.equal(invoice.get("invoiceType"), invoiceType));
		if (isAmendPayment) {
			predicateList.add(builder.notEqual(invoice.<InvoiceState> get("invoiceStateBean")
					.<Integer> get("stateId"), InvoiceStateBO.CANCELLED.getStateID()));
			predicateList.add(invoice
					.<PaymentStatus> get("paymentStatusBean")
					.<Integer> get("paymentStatusId")
					.in(PaymentStatusBO.CREDIT_SALES.getStatusID(),
							PaymentStatusBO.PARTIAL_PAYMENT.getStatusID()));
		} else {
			if (invoiceStates != null && invoiceStates.length > 0) {
				predicateList.add(invoice.<InvoiceState> get("invoiceStateBean")
						.<Integer> get("stateId").in(Arrays.asList(invoiceStates)));
			}
			if (entity > 0 && CrownConstants.RETAIL.equals(invoiceType)) {
				predicateList.add(builder.equal(invoice.<CrownEntity> get("crownEntity")
						.<Integer> get("entityId"), entity));
			}
		}
		for (String filterProperty : filters.keySet()) {

			String filterValue = filters.get(filterProperty);
			if (filterValue == null || filterValue.trim().length() < 2) {
				continue;
			}
			filterValue = filterValue.trim();
			if (filterProperty.equals("globalFilter")) {
				predicateList.add(builder.or(
				// INVOICE NUMBER
						builder.like(builder.upper(invoice.<String> get("invoiceNumber")),
								filterValue.toUpperCase() + "%"),
						// INVOIEC STATE
						builder.like(builder.upper(invoice.<InvoiceState> get("invoiceStateBean")
								.<String> get("state")), filterValue.toUpperCase() + "%"),
						// CUSTOMER
						builder.like(
								builder.upper(invoice.<Customer> get("customer").<String> get(
										"customerName")), filterValue.toUpperCase() + "%"),
						// COMPANY
						builder.like(
								builder.upper(invoice.<Company> get("company").<String> get(
										"companyName")), filterValue.toUpperCase() + "%"),
						// PAYMENT STATUS
						builder.like(builder.upper(invoice.<PaymentStatus> get("paymentStatusBean")
								.<String> get("paymentStatus")), filterValue.toUpperCase() + "%")));
			}
			if (filterProperty.equals("invoiceNumber")) {
				predicateList.add(builder.like(
						builder.upper(invoice.<String> get("invoiceNumber")),
						filterValue.toUpperCase() + "%"));
			}
			if (filterProperty.equals("invoiceStateBO.stateName")) {
				predicateList.add(builder.like(
						builder.upper(invoice.<InvoiceState> get("invoiceStateBean").<String> get(
								"state")), filterValue.toUpperCase() + "%"));
			}
			if (filterProperty.equals("customerBO.customerName")) {
				predicateList.add(builder.like(
						builder.upper(invoice.<Customer> get("customer").<String> get(
								"customerName")), filterValue.toUpperCase() + "%"));
			}
			if (filterProperty.equals("companyBO.companyName")) {
				predicateList
						.add(builder.like(
								builder.upper(invoice.<Company> get("company").<String> get(
										"companyName")), filterValue.toUpperCase() + "%"));
			}
			if (filterProperty.equals("paymentStatusBO.status")) {
				predicateList.add(builder.like(builder.upper(invoice.<PaymentStatus> get(
						"paymentStatusBean").<String> get("paymentStatus")),
						filterValue.toUpperCase() + "%"));
			}
		}

		query.where(predicateList.toArray(new Predicate[0])).distinct(true)
				.orderBy(builder.desc(invoice.get("invoiceDate")));

		return manager.createQuery(query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceBO> findAllInvoices(String invoiceType, int first, int pageSize,
			boolean isAmendPayment, int entity, Object... invoiceStates) {
		final List<InvoiceBO> bos = new ArrayList<InvoiceBO>();
		final Query filterQuery = createFilterQuery(invoiceType, isAmendPayment,
				Collections.EMPTY_MAP, entity, invoiceStates);
		if (filterQuery == null) {
			setFilteredInvoiceCount(findInvoiceCount(invoiceType));
			return bos;
		}

		setFilteredInvoiceCount(filterQuery.getResultList().size());

		Query query = filterQuery.setFirstResult(first).setMaxResults(pageSize);
		final List<Invoice> invoices = query.getResultList();
		for (final Invoice inv : invoices) {
			bos.add(helper.createInvoiceBO(inv));
		}

		return bos;
	}

	@Override
	public int findInvoiceCount(String invoiceType) {
		Query query = manager
				.createNativeQuery("SELECT COUNT(*) FROM INVOICE WHERE INVOICE_TYPE = '"
						+ invoiceType + "'");
		return ((Long) query.getSingleResult()).intValue();
	}

	@Override
	public int getFilteredInvoiceCount() {
		return filteredInvoiceCount;
	}

	private void setFilteredInvoiceCount(int count) {
		this.filteredInvoiceCount = count;
	}

	@Override
	public int getFilteredBulkItemsCount() {
		return filteredBulkItemsCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceItemBO> findBulkInvoiceItems(Integer brand, Integer category) {
		final List<InvoiceItemBO> bos = new ArrayList<InvoiceItemBO>();
		if (brand == null && category == null) {
			return bos;
		}
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM ITEM WHERE STATUS = 1 AND ");

		final QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.addQuery("item_category", category);
		queryBuilder.addQuery("item_brand", brand);
		builder.append(queryBuilder.getQuery());
		builder.append(" ORDER BY ITEM_NAME");

		Query query = getNativeQuery(builder.toString(), Item.class);
		final List<Item> items = query.getResultList();
		final ItemHelper itemHelper = new ItemHelper();
		for (final Item item : items) {
			final InvoiceItemBO bo = new InvoiceItemBO();
			bo.setAllocationType(AllocationTypeBO.UOM.getAllocationTypeID());
			bo.setItemBO(itemHelper.createItemBO(item));
			bos.add(bo);
		}
		return bos;
	}
}
