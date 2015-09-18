/**
 * 
 */
package com.techlords.crown.service;

import java.util.List;
import java.util.Map;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CustomerBO;
import com.techlords.crown.business.model.CustomerTypeBO;

/**
 * @author gv
 * 
 */
public interface CustomerService extends CrownService {

	public boolean createCustomer(CustomerBO customerBO, int userID)
			throws CrownException;

	public boolean updateCustomer(CustomerBO customerBO, int userID)
			throws CrownException;

	public boolean deleteCustomer(CustomerBO customerBO, int userID)
			throws CrownException;

	public List<CustomerBO> findAllCustomers();

	public List<CustomerTypeBO> findAllCustomerTypes();

	public void updateCreditLimit(CustomerBO customerBO, double creditLimit,
			int userID) throws CrownException;

	List<CustomerBO> findAllCustomers(int first, int pageSize,
			Map<String, String> filters);

	List<CustomerBO> findAllCustomers(int first, int pageSize);

	int getFilteredItemCount();

}
