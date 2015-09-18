package com.techlords.crown.service;

import java.util.List;
import java.util.Map;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.ItemBrandBO;
import com.techlords.crown.business.model.ItemCategoryBO;

public interface ItemService extends CrownService {
	
	ItemBO getItemBO(int id);
	
	boolean createItemCategory(ItemCategoryBO bo, int userID) throws CrownException;

	boolean updateItemCategory(ItemCategoryBO bo, int userID) throws CrownException;

	boolean deleteItemCategory(ItemCategoryBO bo, int userID) throws CrownException;

	List<ItemCategoryBO> findAllItemCategories();

	boolean createItemBrand(ItemBrandBO bo, int userID) throws CrownException;

	boolean updateItemBrand(ItemBrandBO bo, int userID) throws CrownException;

	boolean deleteItemBrand(ItemBrandBO bo, int userID) throws CrownException;

	List<ItemBrandBO> findAllItemBrands();

	boolean createItem(ItemBO bo, int userID) throws CrownException;

	boolean updateItem(ItemBO bo, int userID) throws CrownException;

	boolean deleteItem(ItemBO bo, int userID) throws CrownException;

	List<ItemBO> findAllItems();

	List<ItemBO> findAllItems(int first, int pageSize, Map<String, Object> filters);
	
	List<ItemBO> findAllItems(int first, int pageSize);

	List<ItemBO> findByItemName(String itemName);

	List<ItemBO> findByItemCode(String itemCode);
	
	int findItemCount();
	
	int getFilteredItemCount();
}
