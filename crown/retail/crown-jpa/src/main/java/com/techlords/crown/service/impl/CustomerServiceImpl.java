/**
 * 
 */
package com.techlords.crown.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.OptimisticLockException;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CustomerBO;
import com.techlords.crown.business.model.CustomerTypeBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.helpers.AuditActionEnum;
import com.techlords.crown.helpers.CustomerHelper;
import com.techlords.crown.helpers.GeneralHelper;
import com.techlords.crown.helpers.QueryBuilder;
import com.techlords.crown.persistence.Customer;
import com.techlords.crown.persistence.CustomerType;
import com.techlords.crown.persistence.Location;
import com.techlords.crown.persistence.Status;
import com.techlords.crown.service.CustomerService;
import com.techlords.crown.service.GeneralService;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
final class CustomerServiceImpl extends AbstractCrownService implements CustomerService {
	final CustomerHelper helper = new CustomerHelper();
	private int filteredItemCount;
	private static final String CUSTOMER_PREFIX = "CUS";

	private void setCustomerAttributes(Customer customer, CustomerBO bo) {
		customer.setLocationBean(manager.find(Location.class, bo.getLocationBO().getId()));
		customer.setCustomerTypeBean(manager.find(CustomerType.class, bo.getCustomerType()));
		customer.setStatusBean(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));
		customer.setVersion(bo.getVersion());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.CustomerService#createCustomer(com.techlords
	 * .crown.business.model.CustomerBO)
	 */
	@Transactional
	@Override
	public boolean createCustomer(CustomerBO customerBO, int userID) throws CrownException {
		try {
			final Customer customer = helper.createCustomer(customerBO);
			final GeneralService generalService = CrownServiceLocator.INSTANCE
					.getCrownService(GeneralService.class);
			customer.setCustomerCode(CUSTOMER_PREFIX + GeneralHelper.getFormattedRunningDate()
					+ generalService.getRunningSequenceNumber("CUSTOMER", null));

			setCustomerAttributes(customer, customerBO);
			manager.persist(customer);

			auditLog(AuditActionEnum.CREATE, userID, "customer", customer.getCustomerCode());
		} catch (Exception e) {
			throw new CrownException("Cannot create customer", e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.CustomerService#editCustomer(com.techlords
	 * .crown.business.model.CustomerBO)
	 */
	@Transactional
	@Override
	public boolean updateCustomer(CustomerBO customerBO, int userID) throws CrownException {
		try {
			final CustomerHelper helper = new CustomerHelper();
			final Customer customer = helper.createCustomer(customerBO,
					manager.find(Customer.class, customerBO.getId()));
			setCustomerAttributes(customer, customerBO);
			manager.merge(customer);

			auditLog(AuditActionEnum.UPDATE, userID, "customer", customer.getCustomerCode());
		} catch (OptimisticLockException e) {
			throw new CrownException("Customer has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.CustomerService#deleteCustomer(com.techlords
	 * .crown.business.model.CustomerBO)
	 */
	@Transactional
	@Override
	public boolean deleteCustomer(CustomerBO customerBO, int userID) throws CrownException {
		try {
			final Customer customer = manager.find(Customer.class, customerBO.getId());
			customer.setVersion(customerBO.getVersion());
			customer.setStatusBean(manager.find(Status.class, StatusBO.DISABLED.getStatusID()));
			manager.merge(customer);

			auditLog(AuditActionEnum.DELETE, userID, "customer", customer.getCustomerCode());
		} catch (OptimisticLockException e) {
			throw new CrownException("Customer has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.CustomerService#findAllCustomers()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerBO> findAllCustomers() {
		final List<Customer> customers = manager.createNamedQuery("Customer.findActiveCustomers")
				.getResultList();
		final List<CustomerBO> bos = new ArrayList<CustomerBO>();
		final CustomerHelper helper = new CustomerHelper();
		for (final Customer customer : customers) {
			bos.add(helper.createCustomerBO(customer));
		}
		return bos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.CustomerService#searchCustomerTypes()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerTypeBO> findAllCustomerTypes() {
		final List<CustomerType> customers = manager.createQuery("select ct from CustomerType ct")
				.getResultList();
		final List<CustomerTypeBO> bos = new ArrayList<CustomerTypeBO>();
		final CustomerHelper helper = new CustomerHelper();
		for (final CustomerType customer : customers) {
			bos.add(helper.createCustomerTypeBO(customer));
		}
		return bos;
	}

	@Transactional
	@Override
	public void updateCreditLimit(CustomerBO customerBO, double creditLimit, int userID)
			throws CrownException {
		try {
			final Customer customer = manager.find(Customer.class, customerBO.getId());
			customer.setVersion(customerBO.getVersion());
			customer.setCreditLimit(creditLimit);
			auditLog(AuditActionEnum.CREDIT_LIMIT, userID, "customer", customer.getCustomerCode()
					+ "credit limit - " + creditLimit);
		} catch (OptimisticLockException e) {
			throw new CrownException("Customer has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
	}

	/**
	 * FILTERING & LAZY LOADING
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerBO> findAllCustomers(int first, int pageSize) {
		Query query = manager
				.createQuery(
						"select C from Customer C where C.statusBean.statusId=1 order by C.customerName")
				.setFirstResult(first).setMaxResults(pageSize);
		final String countQuery = "SELECT COUNT(*) FROM CUSTOMER WHERE STATUS = 1";
		setFilteredItemCount(((Long) getNativeQuery(countQuery).getSingleResult()).intValue());
		final List<Customer> customers = query.getResultList();
		final List<CustomerBO> bos = new ArrayList<CustomerBO>();
		for (final Customer customer : customers) {
			bos.add(helper.createCustomerBO(customer));
		}
		return bos;
	}

	@Override
	public int getFilteredItemCount() {
		return filteredItemCount;
	}

	private void setFilteredItemCount(int filteredItemCount) {
		this.filteredItemCount = filteredItemCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerBO> findAllCustomers(int first, int pageSize, Map<String, String> filters) {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM CUSTOMER");

		final QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.addQuery("STATUS", 1);
		for (String filterProperty : filters.keySet()) {

			String filterValue = filters.get(filterProperty);
			if (filterValue == null || filterValue.trim().length() < 2) {
				continue;
			}
			if (filterProperty.equals("globalFilter")) {
				queryBuilder.addORLikeQuery(filterValue.trim().toUpperCase(),
						"UPPER(CUSTOMER_NAME)", "UPPER(CUSTOMER_CODE)");
			}
			if (filterProperty.equals("customerCode")) {
				queryBuilder.addLikeQuery("UPPER(CUSTOMER_CODE)", filterValue.trim().toUpperCase());
			}
			if (filterProperty.equals("customerName")) {
				queryBuilder.addLikeQuery("UPPER(CUSTOMER_NAME)", filterValue.trim().toUpperCase());
			}
		}

		queryBuilder.addWhereClause();
		builder.append(queryBuilder.getQuery());

		final String countQuery = "SELECT COUNT(*) FROM CUSTOMER" + queryBuilder.getQuery();
		setFilteredItemCount(((Long) getNativeQuery(countQuery).getSingleResult()).intValue());

		Query query = getNativeQuery(builder.toString(), Customer.class).setFirstResult(first)
				.setMaxResults(pageSize);
		final List<Customer> customers = query.getResultList();
		final List<CustomerBO> bos = new ArrayList<CustomerBO>();
		for (final Customer customer : customers) {
			bos.add(helper.createCustomerBO(customer));
		}
		return bos;
	}
}
