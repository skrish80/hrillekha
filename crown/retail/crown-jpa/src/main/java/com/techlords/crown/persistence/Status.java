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
 * The persistent class for the status database table.
 * 
 */
@Entity
@Table(name = "status")
public class Status implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "STATUS_STATUSID_GENERATOR", sequenceName = "STATUS_STATUS_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATUS_STATUSID_GENERATOR")
	@Column(name = "status_id", unique = true, nullable = false)
	private Integer statusId;

	@Column(length = 64)
	private String description;

	@Column(name = "status_code", nullable = false, length = 16)
	private String statusCode;

	// bi-directional many-to-one association to Agent
	@OneToMany(mappedBy = "status")
	private Set<Agent> agents;

	// bi-directional many-to-one association to Company
	@OneToMany(mappedBy = "statusBean")
	private Set<Company> companies;

	// bi-directional many-to-one association to CrownUser
	@OneToMany(mappedBy = "statusBean")
	private Set<CrownUser> crownUsers;

	// bi-directional many-to-one association to Customer
	@OneToMany(mappedBy = "statusBean")
	private Set<Customer> customers;

	// bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy = "statusBean")
	private Set<Invoice> invoices;

	// bi-directional many-to-one association to Item
	@OneToMany(mappedBy = "statusBean")
	private Set<Item> items;

	// bi-directional many-to-one association to ItemBrand
	@OneToMany(mappedBy = "statusBean")
	private Set<ItemBrand> itemBrands;

	// bi-directional many-to-one association to ItemCategory
	@OneToMany(mappedBy = "statusBean")
	private Set<ItemCategory> itemCategories;

	// bi-directional many-to-one association to StockAllocation
	@OneToMany(mappedBy = "statusBean")
	private Set<StockAllocation> stockAllocations;

	// bi-directional many-to-one association to Warehouse
	@OneToMany(mappedBy = "statusBean")
	private Set<Warehouse> warehouses;

	public Status() {
	}

	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Set<Agent> getAgents() {
		return this.agents;
	}

	public void setAgents(Set<Agent> agents) {
		this.agents = agents;
	}

	public Set<Company> getCompanies() {
		return this.companies;
	}

	public void setCompanies(Set<Company> companies) {
		this.companies = companies;
	}

	public Set<CrownUser> getCrownUsers() {
		return this.crownUsers;
	}

	public void setCrownUsers(Set<CrownUser> crownUsers) {
		this.crownUsers = crownUsers;
	}

	public Set<Customer> getCustomers() {
		return this.customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	public Set<Invoice> getInvoices() {
		return this.invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
	}

	public Set<Item> getItems() {
		return this.items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public Set<ItemBrand> getItemBrands() {
		return this.itemBrands;
	}

	public void setItemBrands(Set<ItemBrand> itemBrands) {
		this.itemBrands = itemBrands;
	}

	public Set<ItemCategory> getItemCategories() {
		return this.itemCategories;
	}

	public void setItemCategories(Set<ItemCategory> itemCategories) {
		this.itemCategories = itemCategories;
	}

	public Set<StockAllocation> getStockAllocations() {
		return this.stockAllocations;
	}

	public void setStockAllocations(Set<StockAllocation> stockAllocations) {
		this.stockAllocations = stockAllocations;
	}

	public Set<Warehouse> getWarehouses() {
		return this.warehouses;
	}

	public void setWarehouses(Set<Warehouse> warehouses) {
		this.warehouses = warehouses;
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