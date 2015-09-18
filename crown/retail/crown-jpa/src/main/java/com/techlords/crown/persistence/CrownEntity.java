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
 * The persistent class for the crown_entity database table.
 * 
 */
@Entity
@Table(name = "crown_entity")
public class CrownEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CROWN_ENTITY_ENTITYID_GENERATOR", sequenceName = "CROWN_ENTITY_ENTITY_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CROWN_ENTITY_ENTITYID_GENERATOR")
	@Column(name = "entity_id", unique = true, nullable = false)
	private Integer entityId;

	@Column(name="description", length = 50)
	private String description;
	
	@Column(name="entity_type", length = 2)
	private String entityType;

	@Column(name = "entity_name", nullable = false, length = 25)
	private String entityName;

	// bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy = "crownEntity")
	private Set<Invoice> invoices;

	// bi-directional many-to-one association to StockAllocation
	@OneToMany(mappedBy = "crownEntity")
	private Set<StockAllocation> stockAllocations;

	// bi-directional many-to-one association to Warehouse
	@OneToMany(mappedBy = "crownEntity")
	private Set<Warehouse> warehouses;

	public CrownEntity() {
	}

	public Integer getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Set<Invoice> getInvoices() {
		return this.invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
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

	public final String getEntityType() {
		return entityType;
	}

	public final void setEntityType(String entityType) {
		this.entityType = entityType;
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