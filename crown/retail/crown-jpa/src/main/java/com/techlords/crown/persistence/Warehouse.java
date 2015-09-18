package com.techlords.crown.persistence;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the warehouse database table.
 * 
 */
@Entity
@Table(name = "warehouse")
public class Warehouse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "WAREHOUSE_WAREHOUSEID_GENERATOR", sequenceName = "WAREHOUSE_WAREHOUSE_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WAREHOUSE_WAREHOUSEID_GENERATOR")
	@Column(name = "warehouse_id", unique = true, nullable = false)
	private Integer warehouseId;

	@Column(name="address", nullable = false, length = 2147483647)
	private String address;

	@Column(name="description", length = 50)
	private String description;

	@Column(name="is_retail_shop")
	private boolean isRetailShop;

	@Column(name = "warehouse_name", nullable = false, length = 25)
	private String warehouseName;

	// bi-directional many-to-one association to StockMovement
	@OneToMany(mappedBy="fromWarehouse")
	private Set<StockMovement> stockMovementsFrom;

	//bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy="deliveryWarehouse")
	private Set<Invoice> invoices;

	// bi-directional many-to-one association to StockMovement
	@OneToMany(mappedBy="toWarehouse")
	private Set<StockMovement> stockMovementsTo;

	// bi-directional many-to-one association to StolenStock
	@OneToMany(mappedBy = "warehouseBean")
	private Set<StolenStock> stolenStocks;

	// bi-directional many-to-one association to CrownEntity
	@ManyToOne
	@JoinColumn(name = "entity", nullable = false)
	private CrownEntity crownEntity;

	// bi-directional many-to-one association to CrownUser
	@ManyToOne
	@JoinColumn(name = "incharge", nullable = false)
	private CrownUser crownUser;

	// bi-directional many-to-one association to Location
	@ManyToOne
	@JoinColumn(name = "location", nullable = false)
	private Location locationBean;

	// bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name = "status")
	private Status statusBean;

	// bi-directional many-to-one association to WarehouseStock
	@OneToMany(mappedBy = "warehouse")
	private Set<WarehouseStock> warehouseStocks;

	public Warehouse() {
	}

	public Integer getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWarehouseName() {
		return this.warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Set<StolenStock> getStolenStocks() {
		return this.stolenStocks;
	}

	public void setStolenStocks(Set<StolenStock> stolenStocks) {
		this.stolenStocks = stolenStocks;
	}

	public CrownEntity getCrownEntity() {
		return this.crownEntity;
	}

	public void setCrownEntity(CrownEntity crownEntity) {
		this.crownEntity = crownEntity;
	}

	public CrownUser getCrownUser() {
		return this.crownUser;
	}

	public void setCrownUser(CrownUser crownUser) {
		this.crownUser = crownUser;
	}

	public Location getLocationBean() {
		return this.locationBean;
	}

	public void setLocationBean(Location locationBean) {
		this.locationBean = locationBean;
	}

	public Status getStatusBean() {
		return this.statusBean;
	}

	public void setStatusBean(Status statusBean) {
		this.statusBean = statusBean;
	}

	public Set<WarehouseStock> getWarehouseStocks() {
		return this.warehouseStocks;
	}

	public void setWarehouseStocks(Set<WarehouseStock> warehouseStocks) {
		this.warehouseStocks = warehouseStocks;
	}

	public final Set<StockMovement> getStockMovementsFrom() {
		return stockMovementsFrom;
	}

	public final void setStockMovementsFrom(
			Set<StockMovement> stockMovementsFrom) {
		this.stockMovementsFrom = stockMovementsFrom;
	}

	public final Set<StockMovement> getStockMovementsTo() {
		return stockMovementsTo;
	}

	public final void setStockMovementsTo(Set<StockMovement> stockMovementsTo) {
		this.stockMovementsTo = stockMovementsTo;
	}

	public Set<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
	}

	public final boolean isRetailShop() {
		return isRetailShop;
	}

	public final void setRetailShop(boolean isRetail) {
		this.isRetailShop = isRetail;
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