package com.techlords.crown.service;

import java.util.List;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CrownEntityBO;
import com.techlords.crown.business.model.SearchCriteria;
import com.techlords.crown.business.model.StockAllocationBO;
import com.techlords.crown.business.model.StockMovementBO;

public interface StockService extends CrownService {

	boolean createStockAllocation(StockAllocationBO bo, int userID)
			throws CrownException;

	boolean updateStockAllocation(StockAllocationBO bo, int userID)
			throws CrownException;

	boolean updateStockAllocationOnDelivery(int entity, int item,
			int allocationType, int qty) throws CrownException;

	boolean releaseAllocation(StockAllocationBO bo, int userID)
			throws CrownException;

	List<StockAllocationBO> findAllAllocatedStock();

	List<StockAllocationBO> findAllocatedStock(CrownEntityBO entityBO);

	List<StockAllocationBO> searchAllocatedStock(SearchCriteria searchCriteria);

	List<StockAllocationBO> findUnavailableStockForEntity(Integer entityID,
			Object... itemIDs);

	// STOCK MOVEMENT
	boolean createStockMovement(StockMovementBO bo, int userID)
			throws CrownException;

	boolean updateStockMovement(StockMovementBO bo, int userID)
			throws CrownException;

	boolean cancelStockMovement(StockMovementBO bo, int userID)
			throws CrownException;

	List<StockMovementBO> findAllMovementStock();

	boolean updateStockAllocationOnStolen(int item, int allocationType, int qty)
			throws CrownException;
}
