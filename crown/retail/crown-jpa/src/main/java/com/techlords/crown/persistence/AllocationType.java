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
 * The persistent class for the allocation_type database table.
 * 
 */
@Entity
@Table(name = "allocation_type")
public class AllocationType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ALLOCATION_TYPE_ALLOCATIONTYPEID_GENERATOR", sequenceName = "ALLOCATION_TYPE_ALLOCATION_TYPE_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALLOCATION_TYPE_ALLOCATIONTYPEID_GENERATOR")
	@Column(name = "allocation_type_id", unique = true, nullable = false)
	private Integer allocationTypeId;

	@Column(name = "allocation_type", nullable = false, length = 25)
	private String allocationType;

	@Column(length = 50)
	private String description;

	// bi-directional many-to-one association to InvoiceItem
	@OneToMany(mappedBy = "allocationTypeBean")
	private Set<InvoiceItem> invoiceItems;

	// bi-directional many-to-one association to StockAllocation
	@OneToMany(mappedBy = "allocationTypeBean")
	private Set<StockAllocation> stockAllocations;

	// bi-directional many-to-one association to StockMovementItem
	@OneToMany(mappedBy = "allocationTypeBean")
	private Set<StockMovementItem> stockMovementItems;

	// bi-directional many-to-one association to InvoiceReturn
	@OneToMany(mappedBy = "returnType")
	private Set<InvoiceReturn> invoiceReturns;

	public AllocationType() {
	}

	public Integer getAllocationTypeId() {
		return this.allocationTypeId;
	}

	public void setAllocationTypeId(Integer allocationTypeId) {
		this.allocationTypeId = allocationTypeId;
	}

	public String getAllocationType() {
		return this.allocationType;
	}

	public void setAllocationType(String allocationType) {
		this.allocationType = allocationType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<InvoiceItem> getInvoiceItems() {
		return this.invoiceItems;
	}

	public void setInvoiceItems(Set<InvoiceItem> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}

	public Set<StockAllocation> getStockAllocations() {
		return this.stockAllocations;
	}

	public void setStockAllocations(Set<StockAllocation> stockAllocations) {
		this.stockAllocations = stockAllocations;
	}

	public Set<StockMovementItem> getStockMovementItems() {
		return this.stockMovementItems;
	}

	public void setStockMovementItems(Set<StockMovementItem> stockMovementItems) {
		this.stockMovementItems = stockMovementItems;
	}

	public Set<InvoiceReturn> getInvoiceReturns() {
		return this.invoiceReturns;
	}

	public void setInvoiceReturns(Set<InvoiceReturn> invoiceReturns) {
		this.invoiceReturns = invoiceReturns;
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