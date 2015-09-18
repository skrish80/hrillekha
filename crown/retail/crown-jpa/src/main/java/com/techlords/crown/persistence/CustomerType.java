package com.techlords.crown.persistence;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the customer_type database table.
 * 
 */
@Entity
@Table(name = "customer_type")
public class CustomerType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CUSTOMER_TYPE_CUSTOMERTYPEID_GENERATOR", sequenceName = "CUSTOMER_TYPE_CUSTOMER_TYPE_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_TYPE_CUSTOMERTYPEID_GENERATOR")
	@Column(name = "customer_type_id", unique = true, nullable = false)
	private Integer customerTypeId;

	@Column(name = "customer_type", nullable = false, length = 25)
	private String customerType;

	@Column(length = 100)
	private String description;

	// bi-directional many-to-one association to Customer
	@OneToMany(mappedBy = "customerTypeBean")
	private Set<Customer> customers;

	public CustomerType() {
	}

	public Integer getCustomerTypeId() {
		return this.customerTypeId;
	}

	public void setCustomerTypeId(Integer customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	public String getCustomerType() {
		return this.customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Customer> getCustomers() {
		return this.customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}
	
	@Version
	@Column(name = "version", unique = true, nullable = false)
	private long version;

	public final long getVersion() {
		return version;
	}

	public final void setVersion(long version) {
		this.version = version;
	}

}