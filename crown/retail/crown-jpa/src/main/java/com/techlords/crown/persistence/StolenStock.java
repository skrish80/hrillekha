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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the stolen_stock database table.
 * 
 */
@Entity
@Table(name = "stolen_stock")
public class StolenStock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "STOLEN_STOCK_STOLENID_GENERATOR", sequenceName = "STOLEN_STOCK_STOLEN_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOLEN_STOCK_STOLENID_GENERATOR")
	@Column(name = "stolen_id", unique = true, nullable = false)
	private Integer stolenId;

	@Temporal(TemporalType.DATE)
	@Column(name = "stolen_date", nullable = false)
	private Date stolenDate;

	@Column(name = "stolen_item_qty")
	private Integer stolenItemQty = 0;

	@Column(name = "stolen_uom_qty")
	private Integer stolenUomQty = 0;

	@Column(name = "remarks")
	private String remarks;

	// bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	// bi-directional many-to-one association to Warehouse
	@ManyToOne
	@JoinColumn(name = "warehouse")
	private Warehouse warehouseBean;

	// bi-directional many-to-one association to Warehouse
	@ManyToOne
	@JoinColumn(name = "created_by")
	private CrownUser createdBy;

	public StolenStock() {
	}

	public Integer getStolenId() {
		return this.stolenId;
	}

	public void setStolenId(Integer stolenId) {
		this.stolenId = stolenId;
	}

	public Date getStolenDate() {
		return this.stolenDate;
	}

	public void setStolenDate(Date stolenDate) {
		this.stolenDate = stolenDate;
	}

	public Integer getStolenItemQty() {
		return this.stolenItemQty;
	}

	public void setStolenItemQty(Integer stolenItemQty) {
		this.stolenItemQty = stolenItemQty;
	}

	public Integer getStolenUomQty() {
		return this.stolenUomQty;
	}

	public void setStolenUomQty(Integer stolenUomQty) {
		this.stolenUomQty = stolenUomQty;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Warehouse getWarehouseBean() {
		return this.warehouseBean;
	}

	public void setWarehouseBean(Warehouse warehouseBean) {
		this.warehouseBean = warehouseBean;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public CrownUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(CrownUser createdBy) {
		this.createdBy = createdBy;
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