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
 * The persistent class for the allocation_state database table.
 * 
 */
@Entity
@Table(name = "allocation_state")
public class AllocationState implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ALLOCATION_STATE_STATEID_GENERATOR", sequenceName = "ALLOCATION_STATE_STATE_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALLOCATION_STATE_STATEID_GENERATOR")
	@Column(name = "state_id", unique = true, nullable = false)
	private Integer stateId;

	@Column(length = 64)
	private String description;

	@Column(nullable = false, length = 16)
	private String state;

	// bi-directional many-to-one association to StockAllocation
	@OneToMany(mappedBy = "allocationState")
	private Set<StockAllocation> stockAllocations;

	public AllocationState() {
	}

	public Integer getStateId() {
		return this.stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Set<StockAllocation> getStockAllocations() {
		return this.stockAllocations;
	}

	public void setStockAllocations(Set<StockAllocation> stockAllocations) {
		this.stockAllocations = stockAllocations;
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