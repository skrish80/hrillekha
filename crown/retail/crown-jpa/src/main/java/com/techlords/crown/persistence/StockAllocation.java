package com.techlords.crown.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the stock_allocation database table.
 * 
 */
@Entity
@Table(name = "stock_allocation")
@NamedNativeQueries({
		@NamedNativeQuery(name = "StockAllocation.findByItemID", resultClass = StockAllocation.class, query = "select SA.* from stock_allocation SA where SA.item_id=?1"),
		@NamedNativeQuery(name = "StockAllocation.findByAllocatedItemID", resultClass = StockAllocation.class, query = "select SA.* from stock_allocation SA where SA.item_id=?1 and SA.state=?2"), })
@NamedQueries({
		@NamedQuery(name = "StockAllocation.findUnavailableEntityStock", query = "select SA.item, SUM(SA.allocationQty), SA.allocationTypeBean from StockAllocation SA where SA.allocationState=?1 group by SA.item, SA.allocationTypeBean"),
		@NamedQuery(name = "StockAllocation.findAllocatedStock", query = "select SA from StockAllocation SA where SA.allocationState.stateId=1 and SA.item.itemId=?1 and SA.crownEntity.entityId=?2"),
		@NamedQuery(name = "StockAllocation.findAllocationByItem", query = "select SA from StockAllocation SA where SA.allocationState.stateId=1 and SA.item.itemId=?1"),

})
public class StockAllocation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "STOCK_ALLOCATION_ALLOCATIONID_GENERATOR", sequenceName = "STOCK_ALLOCATION_ALLOCATION_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_ALLOCATION_ALLOCATIONID_GENERATOR")
	@Column(name = "allocation_id", unique = true, nullable = false)
	private Integer allocationId;

	@Temporal(TemporalType.DATE)
	@Column(name = "allocated_date")
	private Date allocatedDate;

	@Column(name = "allocation_code", nullable = false, length = 25)
	private String allocationCode;

	@Column(name = "allocation_qty")
	private Integer allocationQty = 0;

	@Temporal(TemporalType.DATE)
	@Column(name = "released_date")
	private Date releasedDate;

	// bi-directional many-to-one association to AllocationState
	@ManyToOne
	@JoinColumn(name = "state")
	private AllocationState allocationState;

	// bi-directional many-to-one association to AllocationType
	@ManyToOne
	@JoinColumn(name = "allocation_type")
	private AllocationType allocationTypeBean;

	// bi-directional many-to-one association to CrownEntity
	@ManyToOne
	@JoinColumn(name = "entity")
	private CrownEntity crownEntity;

	// bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	// bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name = "status")
	private Status statusBean;

	public StockAllocation() {
	}

	public Integer getAllocationId() {
		return this.allocationId;
	}

	public void setAllocationId(Integer allocationId) {
		this.allocationId = allocationId;
	}

	public Date getAllocatedDate() {
		return this.allocatedDate;
	}

	public void setAllocatedDate(Date allocatedDate) {
		this.allocatedDate = allocatedDate;
	}

	public String getAllocationCode() {
		return this.allocationCode;
	}

	public void setAllocationCode(String allocationCode) {
		this.allocationCode = allocationCode;
	}

	public Integer getAllocationQty() {
		return this.allocationQty;
	}

	public void setAllocationQty(Integer allocationQty) {
		this.allocationQty = allocationQty;
	}

	public Date getReleasedDate() {
		return this.releasedDate;
	}

	public void setReleasedDate(Date releasedDate) {
		this.releasedDate = releasedDate;
	}

	public AllocationState getAllocationState() {
		return this.allocationState;
	}

	public void setAllocationState(AllocationState allocationState) {
		this.allocationState = allocationState;
	}

	public AllocationType getAllocationTypeBean() {
		return this.allocationTypeBean;
	}

	public void setAllocationTypeBean(AllocationType allocationTypeBean) {
		this.allocationTypeBean = allocationTypeBean;
	}

	public CrownEntity getCrownEntity() {
		return this.crownEntity;
	}

	public void setCrownEntity(CrownEntity crownEntity) {
		this.crownEntity = crownEntity;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Status getStatusBean() {
		return this.statusBean;
	}

	public void setStatusBean(Status statusBean) {
		this.statusBean = statusBean;
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