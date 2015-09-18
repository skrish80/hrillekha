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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the item database table.
 * 
 */
@Entity
@Table(name = "item")
@NamedQueries({
	@NamedQuery(name = "Item.findNameStartsWith", query = "select I from Item I where I.itemName LIKE ?1"),
	@NamedQuery(name = "Item.findCodeLike", query = "select I from Item I where upper(I.itemCode) LIKE upper(?1)"),
	})
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ITEM_ITEMID_GENERATOR", sequenceName = "ITEM_ITEM_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_ITEMID_GENERATOR")
	@Column(name = "item_id", unique = true, nullable = false)
	private Integer itemId;

	@Column(name = "item_code", nullable = false, length = 25)
	private String itemCode;

	@Column(name = "item_name", length = 50)
	private String itemName;

	@Column(name = "model_number", length = 50)
	private String modelNumber;

	@Column(name = "reorder_level")
	private int reorderLevel;

	@Column(name = "item_price")
	private double itemPrice;

	@Column(name = "uom_price")
	private double uomPrice;

	@Column(name = "cost_price")
	private double costPrice;

	@Column(name = "vat")
	private double vat;

	@Column(name = "pieces_per_uom")
	private int piecesPerUOM;

	// bi-directional many-to-one association to InvoiceItem
	@OneToMany(mappedBy = "item")
	private Set<InvoiceItem> invoiceItems;

	// bi-directional many-to-one association to Currency
	@ManyToOne
	@JoinColumn(name = "currency")
	private Currency currencyBean;

	// bi-directional many-to-one association to ItemBrand
	@ManyToOne
	@JoinColumn(name = "item_brand")
	private ItemBrand itemBrandBean;

	// bi-directional many-to-one association to ItemCategory
	@ManyToOne
	@JoinColumn(name = "item_category")
	private ItemCategory itemCategoryBean;

	// bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name = "status")
	private Status statusBean;

	// bi-directional many-to-one association to UnitOfMeasure
	@ManyToOne
	@JoinColumn(name = "uom")
	private UnitOfMeasure unitOfMeasure;

	// bi-directional many-to-one association to StockAllocation
	@OneToMany(mappedBy = "item")
	private Set<StockAllocation> stockAllocations;

	// bi-directional many-to-one association to StockMovementItem
	@OneToMany(mappedBy = "item")
	private Set<StockMovementItem> stockMovementItems;

	// bi-directional many-to-one association to StolenStock
	@OneToMany(mappedBy = "item")
	private Set<StolenStock> stolenStocks;

	// bi-directional many-to-one association to WarehouseStock
	@OneToMany(mappedBy = "item")
	private Set<WarehouseStock> warehouseStocks;

	// bi-directional many-to-one association to InvoiceReturn
	@OneToMany(mappedBy = "item")
	private Set<InvoiceReturn> invoiceReturns;

	public Item() {
	}

	public Integer getItemId() {
		return this.itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getItemPrice() {
		return this.itemPrice;
	}

	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public double getUomPrice() {
		return this.uomPrice;
	}

	public void setUomPrice(double uomPrice) {
		this.uomPrice = uomPrice;
	}

	public Set<InvoiceItem> getInvoiceItems() {
		return this.invoiceItems;
	}

	public void setInvoiceItems(Set<InvoiceItem> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}

	public Currency getCurrencyBean() {
		return this.currencyBean;
	}

	public void setCurrencyBean(Currency currencyBean) {
		this.currencyBean = currencyBean;
	}

	public ItemBrand getItemBrandBean() {
		return this.itemBrandBean;
	}

	public void setItemBrandBean(ItemBrand itemBrandBean) {
		this.itemBrandBean = itemBrandBean;
	}

	public ItemCategory getItemCategoryBean() {
		return this.itemCategoryBean;
	}

	public void setItemCategoryBean(ItemCategory itemCategoryBean) {
		this.itemCategoryBean = itemCategoryBean;
	}

	public Status getStatusBean() {
		return this.statusBean;
	}

	public void setStatusBean(Status statusBean) {
		this.statusBean = statusBean;
	}

	public UnitOfMeasure getUnitOfMeasure() {
		return this.unitOfMeasure;
	}

	public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
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

	public Set<StolenStock> getStolenStocks() {
		return this.stolenStocks;
	}

	public void setStolenStocks(Set<StolenStock> stolenStocks) {
		this.stolenStocks = stolenStocks;
	}

	public Set<WarehouseStock> getWarehouseStocks() {
		return this.warehouseStocks;
	}

	public void setWarehouseStocks(Set<WarehouseStock> warehouseStocks) {
		this.warehouseStocks = warehouseStocks;
	}

	public Set<InvoiceReturn> getInvoiceReturns() {
		return this.invoiceReturns;
	}

	public void setInvoiceReturns(Set<InvoiceReturn> invoiceReturns) {
		this.invoiceReturns = invoiceReturns;
	}

	public final String getModelNumber() {
		return modelNumber;
	}

	public final void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public final int getPiecesPerUOM() {
		return piecesPerUOM;
	}

	public final void setPiecesPerUOM(int piecesPerUOM) {
		this.piecesPerUOM = piecesPerUOM;
	}

	public final double getVat() {
		return vat;
	}

	public final void setVat(double vat) {
		this.vat = vat;
	}

	public final double getCostPrice() {
		return costPrice;
	}

	public final void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
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

	public final int getReorderLevel() {
		return reorderLevel;
	}

	public final void setReorderLevel(int reorderLevel) {
		this.reorderLevel = reorderLevel;
	}

}