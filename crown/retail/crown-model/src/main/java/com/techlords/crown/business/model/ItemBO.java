package com.techlords.crown.business.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class ItemBO extends AppModel {

	@NotEmpty(message = "Item Code cannot be empty")
	@Size(min = 5, max = 25, message = "Item Code shall be from 5 to 25 chars")
	private String itemCode;
	@NotEmpty(message = "Item Name cannot be empty")
	@Size(min = 2, max = 25, message = "Item Name shall be from 2 to 25 chars")
	private String itemName;

	@NotEmpty(message = "Model Number cannot be empty")
	@Size(min = 2, max = 25, message = "Model Number shall be from 2 to 25 chars")
	private String modelNumber;
	
	private Double itemPrice = 0D;
	private Double uomPrice = 0D;
	private Double costPrice = 0D;
	
	@Min(value = 1, message = "Enter Pieces per UOM")
	private int piecesPerUOM;

	@Min(value = 1, message = "Select a Brand")
	private int brand;
	@Min(value = 1, message = "Select a Category")
	private int category;
	@NotEmpty(message = "Select a Currency")
	private String currency;
	@Min(value = 1, message = "Select a UOM")
	private int uom;
	private double vat;

	private ItemCategoryBO categoryBO;
	private ItemBrandBO brandBO;
	private UomBO uomBO;

	private StatusBO status;
	
	private int reorderLevel;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the itemPrice
	 */
	public final Double getItemPrice() {
		return itemPrice;
	}
	
	/**
	 * @return the itemPrice
	 */
	@JsonIgnore
	public final Double getItemPriceWithVAT() {
		return itemPrice + (itemPrice * (vat/100));
	}
	
	/**
	 * @return the itemPrice
	 */
	@JsonIgnore
	public final Double getUOMPriceWithVAT() {
		return uomPrice + (uomPrice * (vat/100));
	}

	/**
	 * @param itemPrice
	 *            the itemPrice to set
	 */
	public final void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	/**
	 * @return the uomPrice
	 */
	public final Double getUomPrice() {
		return uomPrice;
	}

	/**
	 * @param uomPrice
	 *            the uomPrice to set
	 */
	public final void setUomPrice(Double uomPrice) {
		this.uomPrice = uomPrice;
	}

	/**
	 * @return the brand
	 */
	public final int getBrand() {
		return brand;
	}

	/**
	 * @param brand
	 *            the brand to set
	 */
	public final void setBrand(int brand) {
		this.brand = brand;
	}

	/**
	 * @return the category
	 */
	public final int getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public final void setCategory(int category) {
		this.category = category;
	}

	/**
	 * @return the uom
	 */
	public final int getUom() {
		return uom;
	}

	/**
	 * @param uom
	 *            the uom to set
	 */
	public final void setUom(int uom) {
		this.uom = uom;
	}

	/**
	 * @return the categoryBO
	 */
	public final ItemCategoryBO getCategoryBO() {
		return categoryBO;
	}

	/**
	 * @param categoryBO
	 *            the categoryBO to set
	 */
	public final void setCategoryBO(ItemCategoryBO categoryBO) {
		this.categoryBO = categoryBO;
	}

	/**
	 * @return the brandBO
	 */
	public final ItemBrandBO getBrandBO() {
		return brandBO;
	}

	/**
	 * @param brandBO
	 *            the brandBO to set
	 */
	public final void setBrandBO(ItemBrandBO brandBO) {
		this.brandBO = brandBO;
	}

	/**
	 * @return the uomBO
	 */
	public final UomBO getUomBO() {
		return uomBO;
	}

	/**
	 * @param uomBO
	 *            the uomBO to set
	 */
	public final void setUomBO(UomBO uomBO) {
		this.uomBO = uomBO;
	}

	/**
	 * @return the status
	 */
	public final StatusBO getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public final void setStatus(StatusBO status) {
		this.status = status;
	}

	/**
	 * @return the currency
	 */
	public final String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public final void setCurrency(String currency) {
		this.currency = currency;
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

	public final Double getCostPrice() {
		return costPrice;
	}

	public final void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public final int getReorderLevel() {
		return reorderLevel;
	}

	public final void setReorderLevel(int reorderLevel) {
		this.reorderLevel = reorderLevel;
	}
}