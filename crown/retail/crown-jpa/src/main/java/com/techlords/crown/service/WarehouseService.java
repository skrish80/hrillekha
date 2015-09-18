package com.techlords.crown.service;

import java.util.List;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.StolenStockBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.business.model.WarehouseStockBO;

public interface WarehouseService extends CrownService {
	boolean createWarehouse(WarehouseBO bo, int userID) throws CrownException;

	boolean updateWarehouse(WarehouseBO bo, int userID) throws CrownException;

	boolean deleteWarehouse(WarehouseBO bo, int userID) throws CrownException;

	List<WarehouseBO> findAllWarehouses();

	List<WarehouseBO> findAllRetailShops();

	List<WarehouseStockBO> findAllWarehouseStock();

	boolean createWarehouseStock(WarehouseStockBO bo, int userID)
			throws CrownException;

	boolean updateWarehouseStock(WarehouseStockBO bo, int userID)
			throws CrownException;

	List<ItemBO> getWarehouseStock(WarehouseBO bo);

	List<WarehouseStockBO> findWarehouseStock(int warehouseID, Object... itemID);

	boolean updateWarehouseStock(int warehouseID, int itemID,
			int allocationType, Integer quantity, boolean isDeduction)
			throws CrownException;

	// /////////STOLEN ITEMS
	List<StolenStockBO> findAllStolenItems();

	boolean createStolenStock(StolenStockBO bo, int userID)
			throws CrownException;

	boolean updateStolenStock(StolenStockBO bo, int userID)
			throws CrownException;

	List<WarehouseBO> findAllRetailWarehouses();
}
