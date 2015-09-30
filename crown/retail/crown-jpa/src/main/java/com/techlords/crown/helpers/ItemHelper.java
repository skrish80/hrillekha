/**
 * 
 */
package com.techlords.crown.helpers;

import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.ItemBrandBO;
import com.techlords.crown.business.model.ItemCategoryBO;
import com.techlords.crown.business.model.UomBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.persistence.Item;
import com.techlords.crown.persistence.ItemBrand;
import com.techlords.crown.persistence.ItemCategory;
import com.techlords.crown.persistence.UnitOfMeasure;

/**
 * @author gv
 * 
 */
public final class ItemHelper {

	public ItemCategoryBO createItemCategoryBO(ItemCategory category) {
		final ItemCategoryBO bo = new ItemCategoryBO();
		bo.setId(category.getCategoryId());
		bo.setVersion(category.getVersion());
		bo.setItemCategory(category.getCategoryName());
		bo.setCategoryCode(category.getCategoryCode());
		bo.setDescription(category.getDescription());
		bo.setStatus(StatusBO.valueOf(category.getStatusBean().getStatusId()));
		return bo;
	}

	public ItemCategory createItemCategory(ItemCategoryBO bo) {
		return createItemCategory(bo, null);
	}

	public ItemCategory createItemCategory(ItemCategoryBO bo,
			ItemCategory toEdit) {
		final ItemCategory category = (toEdit == null) ? new ItemCategory()
				: toEdit;
		category.setVersion(bo.getVersion());
		category.setCategoryName(bo.getItemCategory());
		category.setCategoryCode(bo.getCategoryCode());
		category.setDescription(bo.getDescription());
		return category;
	}

	public ItemBrandBO createItemBrandBO(ItemBrand brand) {
		final ItemBrandBO bo = new ItemBrandBO();
		bo.setId(brand.getBrandId());
		bo.setItemBrand(brand.getBrandName());
		bo.setBrandCode(brand.getBrandCode());
		bo.setDescription(brand.getDescription());
		bo.setVersion(brand.getVersion());
		bo.setStatus(StatusBO.valueOf(brand.getStatusBean().getStatusId()));
		return bo;
	}

	public ItemBrand createItemBrand(ItemBrandBO bo) {
		return createItemBrand(bo, null);
	}

	public ItemBrand createItemBrand(ItemBrandBO bo, ItemBrand toEdit) {
		final ItemBrand brand = (toEdit == null) ? new ItemBrand() : toEdit;
		brand.setBrandName(bo.getItemBrand());
		brand.setBrandCode(bo.getBrandCode());
		brand.setDescription(bo.getDescription());
		brand.setVersion(bo.getVersion());
		return brand;
	}

	public UomBO createUomBO(UnitOfMeasure uom) {
		final UomBO bo = new UomBO();
		bo.setId(uom.getUomId());
		bo.setVersion(uom.getVersion());
		bo.setUom(uom.getUomName());
		bo.setDescription(uom.getDescription());
		return bo;
	}

	public ItemBO createItemBO(Item item) {
		final ItemBO bo = new ItemBO();

		bo.setId(item.getItemId());
		bo.setVersion(item.getVersion());
		bo.setItemCode(item.getItemCode());
		bo.setItemName(item.getItemName());
		bo.setModelNumber(item.getModelNumber());

		bo.setItemPrice(item.getItemPrice());
		bo.setUomPrice(item.getUomPrice());
		bo.setVat(item.getVat());
		bo.setCostPrice(item.getCostPrice());
		bo.setReorderLevel(item.getReorderLevel());

		bo.setPiecesPerUOM(item.getPiecesPerUOM());

		ItemBrand brand = item.getItemBrandBean();
		bo.setBrand(brand.getBrandId());
		bo.setBrandBO(createItemBrandBO(brand));

		ItemCategory category = item.getItemCategoryBean();
		bo.setCategory(category.getCategoryId());
		bo.setCategoryBO(createItemCategoryBO(category));

		bo.setCurrency(item.getCurrencyBean().getCurrencyCode());

		UnitOfMeasure uom = item.getUnitOfMeasure();
		bo.setUom(uom.getUomId());
		bo.setUomBO(createUomBO(uom));

		bo.setStatus(StatusBO.valueOf(item.getStatusBean().getStatusId()));
		return bo;
	}

	public Item createItem(ItemBO bo) {
		return createItem(bo, null);
	}

	public Item createItem(ItemBO bo, Item toEdit) {
		final Item item = (toEdit == null) ? new Item() : toEdit;

		item.setVersion(bo.getVersion());
		item.setItemName(bo.getItemName());
		item.setItemCode(bo.getItemCode());
		item.setItemPrice(bo.getItemPrice());
		item.setUomPrice(bo.getUomPrice());
		item.setPiecesPerUOM(bo.getPiecesPerUOM());
		item.setModelNumber(bo.getModelNumber());
		item.setVat(bo.getVat());
		item.setCostPrice(bo.getCostPrice());
		item.setReorderLevel(bo.getReorderLevel());

		return item;
	}
}
