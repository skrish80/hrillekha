package com.techlords.crown.service.impl;

import java.util.ArrayList;
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
import com.techlords.crown.business.model.PurchaseInvoiceBO;
import com.techlords.crown.business.model.PurchaseInvoiceItemBO;
import com.techlords.crown.business.model.PurchaseInvoicePaymentBO;
import com.techlords.crown.business.model.SupplierBO;
import com.techlords.crown.business.model.enums.PaymentStatusBO;
import com.techlords.crown.business.model.enums.PurchaseInvoiceStateBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.helpers.AuditActionEnum;
import com.techlords.crown.helpers.GeneralHelper;
import com.techlords.crown.helpers.ItemHelper;
import com.techlords.crown.helpers.PurchaseInvoiceHelper;
import com.techlords.crown.helpers.QueryBuilder;
import com.techlords.crown.persistence.Bank;
import com.techlords.crown.persistence.Company;
import com.techlords.crown.persistence.CrownUser;
import com.techlords.crown.persistence.Currency;
import com.techlords.crown.persistence.Item;
import com.techlords.crown.persistence.PaymentMode;
import com.techlords.crown.persistence.PaymentStatus;
import com.techlords.crown.persistence.PurchaseInvoice;
import com.techlords.crown.persistence.PurchaseInvoiceItem;
import com.techlords.crown.persistence.PurchaseInvoiceItemPK;
import com.techlords.crown.persistence.PurchaseInvoicePayment;
import com.techlords.crown.persistence.Status;
import com.techlords.crown.persistence.Supplier;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.PurchaseInvoiceService;

@SuppressWarnings("serial")
public class PurchaseInvoiceServiceImpl extends AbstractCrownService implements
		PurchaseInvoiceService {
	private static final String INVOICE_NUM_PREFIX = "PINV";

	private final PurchaseInvoiceHelper helper = new PurchaseInvoiceHelper();
	private static final String SUPPLIER_PREFIX = "SUP";

	private int filteredInvoiceCount;

	private void updateAssociations(PurchaseInvoice invoice, PurchaseInvoiceBO bo, int userID) {
		invoice.setSupplier(manager.find(Supplier.class, bo.getSupplier()));
		invoice.setCompany(manager.find(Company.class, bo.getCompany()));
		invoice.setCurrencyBean((Currency) manager.createNamedQuery("Currency.findByCode")
				.setParameter(1, bo.getCurrency()).getSingleResult());
		invoice.setCreatedBy(manager.find(CrownUser.class, userID));
		invoice.setVersion(bo.getVersion());
	}

	@Transactional
	private void createInvoiceItems(PurchaseInvoice invoice, PurchaseInvoiceBO bo) {
		final List<PurchaseInvoiceItemBO> itemBOs = bo.getInvoiceItems();
		final Set<PurchaseInvoiceItem> items = new HashSet<PurchaseInvoiceItem>();
		for (PurchaseInvoiceItemBO itemBO : itemBOs) {
			PurchaseInvoiceItem item = helper.createInvoiceItem(invoice.getInvoiceId(), itemBO);
			// PERSIST THE ITEM
			item.setItem(manager.find(Item.class, itemBO.getItem()));
			manager.persist(item);

			items.add(item);
		}
		invoice.setPurchaseInvoiceItems(items);
	}

	@Transactional(propagation = Propagation.NESTED)
	@Override
	public PurchaseInvoiceBO createInvoice(PurchaseInvoiceBO bo, int userID) throws CrownException {
		final PurchaseInvoice invoice = helper.createInvoice(bo);
		try {
			updateAssociations(invoice, bo, userID);
			final GeneralService generalService = CrownServiceLocator.INSTANCE
					.getCrownService(GeneralService.class);
			final String invoiceNumber = INVOICE_NUM_PREFIX
					+ GeneralHelper.getFormattedRunningDate()
					+ generalService.getRunningSequenceNumber("PURCHASE_INVOICE", "INVOICE_DATE");
			invoice.setInvoiceNumber(invoiceNumber);
			manager.persist(invoice);

			// ASSOCIATE INVOICE ITEMS
			createInvoiceItems(invoice, bo);
			manager.merge(invoice);

			auditLog(AuditActionEnum.CREATE, userID, "purchaseInvoice", invoice.getInvoiceNumber());
			manager.flush();
		} catch (Exception e) {
			throw new CrownException("Purchase Invoice cannot be created", e);
		}
		return helper.createInvoiceBO(invoice);
	}

	@Transactional
	@Override
	public boolean receiveInvoice(PurchaseInvoiceBO bo, int userID) throws CrownException {
		try {
			final PurchaseInvoice invoice = helper.createInvoice(bo,
					manager.find(PurchaseInvoice.class, bo.getId()));
			invoice.setVersion(bo.getVersion());
			invoice.setReceivedBy(manager.find(CrownUser.class, userID));

			final List<PurchaseInvoiceItemBO> itemBOs = bo.getInvoiceItems();
			for (PurchaseInvoiceItemBO itemBO : itemBOs) {
				final PurchaseInvoiceItemPK pk = new PurchaseInvoiceItemPK();
				pk.setInvoiceId(bo.getId());
				pk.setItemId(itemBO.getItem());
				pk.setAllocationType(itemBO.getAllocationType());

				PurchaseInvoiceItem item = manager.find(PurchaseInvoiceItem.class, pk);
				item.setReceivedQty(itemBO.getReceivedQty());
				manager.merge(item);
			}
			manager.merge(invoice);

			auditLog(AuditActionEnum.RECEIVE, userID, "purchaseInvoice", invoice.getInvoiceNumber());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Purchase Invoice has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean cancelInvoice(PurchaseInvoiceBO bo, int userID) throws CrownException {
		try {
			final PurchaseInvoice invoice = manager.find(PurchaseInvoice.class, bo.getId());
			invoice.setVersion(bo.getVersion());
			invoice.setInvoiceState(PurchaseInvoiceStateBO.CANCELLED.getStateCode());
			manager.merge(invoice);
			auditLog(AuditActionEnum.CANCEL, userID, "purchaseInvoice", invoice.getInvoiceNumber());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Purchase Invoice has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean printInvoice(PurchaseInvoiceBO bo, int userID) throws CrownException {
		try {
			final PurchaseInvoice invoice = manager.find(PurchaseInvoice.class, bo.getId());
			invoice.setVersion(bo.getVersion());
			invoice.setInvoiceState(PurchaseInvoiceStateBO.PRINTED.getStateCode());
			manager.merge(invoice);
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Purchase Invoice has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseInvoiceBO> findAllInvoices() {
		final List<PurchaseInvoice> invoices = manager.createQuery(
				"select I from PurchaseInvoice I order by I.invoiceDate desc").getResultList();

		final List<PurchaseInvoiceBO> bos = new ArrayList<PurchaseInvoiceBO>();
		for (final PurchaseInvoice inv : invoices) {
			bos.add(helper.createInvoiceBO(inv));
		}
		return bos;
	}

	public int findInvoiceCount(String invoiceState) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(*) FROM PURCHASE_INVOICE");
		if (invoiceState != null && invoiceState.trim().length() > 0) {
			builder.append(" WHERE INVOICE_STATE = '" + invoiceState + "'");
		}
		Query query = manager.createNativeQuery(builder.toString());
		return ((Long) query.getSingleResult()).intValue();
	}

	@Override
	public int getFilteredInvoiceCount() {
		return filteredInvoiceCount;
	}

	private void setFilteredInvoiceCount(int count) {
		this.filteredInvoiceCount = count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseInvoiceBO> findAllInvoices(int first, int pageSize, String invoiceState) {
		final List<PurchaseInvoiceBO> bos = new ArrayList<PurchaseInvoiceBO>();
		final Query filterQuery = createFilterQuery(invoiceState, Collections.EMPTY_MAP);
		if (filterQuery == null) {
			setFilteredInvoiceCount(findInvoiceCount(invoiceState));
			return bos;
		}

		setFilteredInvoiceCount(filterQuery.getResultList().size());

		Query query = filterQuery.setFirstResult(first).setMaxResults(pageSize);
		final List<PurchaseInvoice> invoices = query.getResultList();

		for (final PurchaseInvoice inv : invoices) {
			bos.add(helper.createInvoiceBO(inv));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseInvoiceBO> findAllInvoices(int first, int pageSize,
			Map<String, Object> filters, String invoiceState) {
		final List<PurchaseInvoiceBO> bos = new ArrayList<PurchaseInvoiceBO>();
		final Query filterQuery = createFilterQuery(invoiceState, filters);
		if (filterQuery == null) {
			setFilteredInvoiceCount(findInvoiceCount(invoiceState));
			return bos;
		}

		setFilteredInvoiceCount(filterQuery.getResultList().size());

		Query query = filterQuery.setFirstResult(first).setMaxResults(pageSize);
		final List<PurchaseInvoice> invoices = query.getResultList();

		for (final PurchaseInvoice inv : invoices) {
			bos.add(helper.createInvoiceBO(inv));
		}
		return bos;
	}

	private Query createFilterQuery(String invoiceState, Map<String, Object> filters) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<PurchaseInvoice> query = builder.createQuery(PurchaseInvoice.class);
		Root<PurchaseInvoice> invoice = query.from(PurchaseInvoice.class);
		query.select(invoice);

		List<Predicate> predicateList = new ArrayList<Predicate>();
		if (invoiceState != null && invoiceState.trim().length() > 0) {
			predicateList.add(builder.equal(invoice.get("invoiceState"), invoiceState));
		}
		for (String filterProperty : filters.keySet()) {

			Object filter = filters.get(filterProperty);

			if (filter == null || filter.toString().trim().length() < 2) {
				continue;
			}
			String filterValue = filter.toString().trim();
			if (filterProperty.equals("globalFilter")) {
				predicateList.add(builder.or(
				// INVOICE NUMBER
						builder.like(builder.upper(invoice.<String> get("invoiceNumber")),
								filterValue.toUpperCase() + "%"),
						// SUPPLIER
						builder.like(
								builder.upper(invoice.<Supplier> get("supplier").<String> get(
										"supplierName")), filterValue.toUpperCase() + "%"),
						// COMPANY
						builder.like(
								builder.upper(invoice.<Company> get("company").<String> get(
										"companyName")), filterValue.toUpperCase() + "%"),
						// INVOIEC STATE
						builder.like(builder.upper(invoice.<String> get("invoiceState")),
								filterValue.toUpperCase() + "%")));
			}
			if (filterProperty.equals("invoiceNumber")) {
				predicateList.add(builder.like(
						builder.upper(invoice.<String> get("invoiceNumber")),
						filterValue.toUpperCase() + "%"));
			}
			if (filterProperty.equals("invoiceState.stateName")) {
				predicateList.add(builder.like(builder.upper(invoice.<String> get("invoiceState")),
						filterValue.toUpperCase() + "%"));
			}
			if (filterProperty.equals("supplierBO.supplierName")) {
				predicateList.add(builder.like(
						builder.upper(invoice.<Supplier> get("supplier").<String> get(
								"supplierName")), filterValue.toUpperCase() + "%"));
			}
			if (filterProperty.equals("companyBO.companyName")) {
				predicateList
						.add(builder.like(
								builder.upper(invoice.<Company> get("company").<String> get(
										"companyName")), filterValue.toUpperCase() + "%"));
			}
		}

		query.where(predicateList.toArray(new Predicate[0])).distinct(true)
				.orderBy(builder.desc(invoice.get("invoiceDate")));

		return manager.createQuery(query);
	}

	@Transactional
	@Override
	public boolean createSupplier(SupplierBO supplierBO, int userID) throws CrownException {
		try {
			final Supplier supplier = helper.createSupplier(supplierBO);
			final GeneralService generalService = CrownServiceLocator.INSTANCE
					.getCrownService(GeneralService.class);
			supplier.setSupplierCode(SUPPLIER_PREFIX + GeneralHelper.getFormattedRunningDate()
					+ generalService.getRunningSequenceNumber("SUPPLIER", null));
			supplier.setStatusBean(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));
			manager.persist(supplier);

			auditLog(AuditActionEnum.CREATE, userID, "supplier", supplier.getSupplierName());
		} catch (Exception e) {
			throw new CrownException("Supplier cannot be created", e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean updateSupplier(SupplierBO supplierBO, int userID) throws CrownException {
		try {
			final Supplier supplier = helper.createSupplier(supplierBO,
					manager.find(Supplier.class, supplierBO.getId()));
			supplier.setVersion(supplierBO.getVersion());
			supplier.setStatusBean(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));
			manager.merge(supplier);

			auditLog(AuditActionEnum.UPDATE, userID, "supplier", supplier.getSupplierName());
		} catch (OptimisticLockException e) {
			throw new CrownException("Supplier has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean deleteSupplier(SupplierBO supplierBO, int userID) throws CrownException {
		try {
			final Supplier supplier = manager.find(Supplier.class, supplierBO.getId());
			supplier.setVersion(supplierBO.getVersion());
			supplier.setStatusBean(manager.find(Status.class, StatusBO.DISABLED.getStatusID()));
			manager.merge(supplier);

			auditLog(AuditActionEnum.DELETE, userID, "supplier", supplier.getSupplierName());
		} catch (OptimisticLockException e) {
			throw new CrownException("Supplier has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SupplierBO> findAllSuppliers() {
		final List<Supplier> suppliers = manager.createNamedQuery("Supplier.findActiveSuppliers")
				.getResultList();
		final List<SupplierBO> bos = new ArrayList<SupplierBO>();
		for (final Supplier supp : suppliers) {
			bos.add(helper.createSupplierBO(supp));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseInvoiceItemBO> findBulkInvoiceItems(Integer brand, Integer category) {
		final List<PurchaseInvoiceItemBO> bos = new ArrayList<PurchaseInvoiceItemBO>();
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
			final PurchaseInvoiceItemBO bo = new PurchaseInvoiceItemBO();
			bo.setItemBO(itemHelper.createItemBO(item));
			bos.add(bo);
		}
		return bos;
	}

	@Transactional(propagation = Propagation.NESTED)
	@Override
	public boolean updateInvoicePayments(PurchaseInvoiceBO bo,
			List<PurchaseInvoicePaymentBO> removedCheques, int userID) throws CrownException {
		try {
			final PurchaseInvoice invoice = manager.find(PurchaseInvoice.class, bo.getId());
			invoice.setVersion(bo.getVersion());
			createInvoicePayments(invoice, bo, true, userID);

			invoice.setPaymentStatusBean(manager.find(PaymentStatus.class, bo.getPaymentStatus()));

			// REMOVE CHEQUES FROM INVOICE PAYMENTS
			final Set<PurchaseInvoicePayment> payments = invoice.getInvoicePayments();
			for (PurchaseInvoicePaymentBO paymentBO : removedCheques) {
				PurchaseInvoicePayment payment = manager.find(PurchaseInvoicePayment.class,
						paymentBO.getId());
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

	@Transactional(propagation = Propagation.NESTED)
	private void createInvoicePayments(PurchaseInvoice invoice, PurchaseInvoiceBO bo,
			boolean isUpdate, int userID) throws CrownException {
		final List<PurchaseInvoicePaymentBO> paymentBOs = bo.getInvoicePayments();
		Set<PurchaseInvoicePayment> payments = invoice.getInvoicePayments();
		if (payments == null) {
			payments = new HashSet<PurchaseInvoicePayment>();
		}
		for (PurchaseInvoicePaymentBO paymentBO : paymentBOs) {
			PurchaseInvoicePayment payment = helper.createInvoicePayment(invoice.getInvoiceId(),
					paymentBO);
			payment.setInvoice(invoice);
			payment.setPaymentModeBean(manager.find(PaymentMode.class, paymentBO.getPaymentMode()));
			// will be in if Credit Note and Receipt are given by supplier
			// if (paymentBO.getPaymentMode() == PaymentModeBO.CREDIT_NOTE.getModeID()) {
			// updateCreditNoteUsage(paymentBO.getDraftNumber(), userID);
			// }
			// if (paymentBO.getPaymentMode() == PaymentModeBO.RECEIPT.getModeID()) {
			// updateReceiptUsage(paymentBO, userID);
			// }
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
}
