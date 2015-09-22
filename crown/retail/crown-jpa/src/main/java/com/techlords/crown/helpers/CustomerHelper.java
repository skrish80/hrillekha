/**
 * 
 */
package com.techlords.crown.helpers;

import com.techlords.crown.business.model.CustomerBO;
import com.techlords.crown.business.model.CustomerTypeBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.persistence.Customer;
import com.techlords.crown.persistence.CustomerType;

/**
 * @author gv
 * 
 */
public final class CustomerHelper {

	public CustomerTypeBO createCustomerTypeBO(CustomerType type) {
		final CustomerTypeBO bo = new CustomerTypeBO();
		bo.setId(type.getCustomerTypeId());
		bo.setVersion(type.getVersion());
		bo.setCustomerType(type.getCustomerType());
		bo.setDescription(type.getDescription());
		return bo;
	}

	public CustomerType createCustomerType(CustomerTypeBO bo,
			CustomerType toEdit) {
		final CustomerType type = (toEdit == null) ? new CustomerType()
				: toEdit;
		type.setVersion(bo.getVersion());
		type.setCustomerType(type.getCustomerType());
		type.setDescription(type.getDescription());
		return type;
	}

	public CustomerBO createCustomerBO(Customer customer) {
		final CustomerBO bo = new CustomerBO();
		bo.setId(customer.getCustomerId());
		bo.setVersion(customer.getVersion());
		bo.setCustomerCode(customer.getCustomerCode());
		bo.setCustomerName(customer.getCustomerName());
		bo.setAddress(customer.getAddress());
		bo.setPhone(customer.getPhone());
		bo.setRemarks(customer.getRemarks());
		bo.setDateOfBirth(customer.getDateOfBirth());
		bo.setAnniversary(customer.getAnniversary());

		bo.setCustomerType(customer.getCustomerTypeBean().getCustomerTypeId());
		bo.setCustomerTypeBO(createCustomerTypeBO(customer
				.getCustomerTypeBean()));

		bo.setStatusBO(StatusBO.valueOf(customer.getStatusBean().getStatusId()));
		
		return bo;
	}

	public Customer createCustomer(CustomerBO bo) {
		return createCustomer(bo, null);
	}

	public Customer createCustomer(CustomerBO bo, Customer toEdit) {
		Customer customer = (toEdit == null) ? new Customer() : toEdit;
		
		customer.setVersion(bo.getVersion());
		customer.setCustomerName(bo.getCustomerName());
		customer.setAddress(bo.getAddress());
		customer.setPhone(bo.getPhone());
		customer.setRemarks(bo.getRemarks());
		customer.setDateOfBirth(bo.getDateOfBirth());
		customer.setAnniversary(bo.getAnniversary());
		return customer;
	}
}
