package com.techlords.crown.helpers;

import java.util.Date;
import java.util.Set;

import com.techlords.crown.business.model.StockAllocationBO;
import com.techlords.crown.business.model.StockMovementBO;
import com.techlords.crown.business.model.StockMovementItemBO;
import com.techlords.crown.business.model.enums.AllocationStateBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.MoveStatusBO;
import com.techlords.crown.persistence.AllocationType;
import com.techlords.crown.persistence.CrownEntity;
import com.techlords.crown.persistence.Item;
import com.techlords.crown.persistence.StockAllocation;
import com.techlords.crown.persistence.StockMovement;
import com.techlords.crown.persistence.StockMovementItem;
import com.techlords.crown.persistence.StockMovementItemPK;
import com.techlords.crown.persistence.Warehouse;

public final class StockHelper {

	public StockAllocation createStockAllocation(final StockAllocationBO bo) {
		return createStockAllocation(bo, null);
	}

	public StockAllocation createStockAllocation(final StockAllocationBO bo,
			final StockAllocation toEdit) {
		final StockAllocation allocation = (toEdit == null) ? new StockAllocation()
				: toEdit;
		if(toEdit == null) {
			allocation.setAllocationCode(bo.getAllocationCode());
		}
		allocation.setVersion(bo.getVersion());
		allocation.setAllocatedDate(new Date());
		allocation.setAllocationQty(bo.getAllocatedQty());
		return allocation;
	}

	public StockAllocationBO createStockAllocationBO(
			final StockAllocation allocation) {
		final StockAllocationBO bo = new StockAllocationBO();
		bo.setId(allocation.getAllocationId());
		bo.setVersion(allocation.getVersion());
		bo.setAllocationCode(allocation.getAllocationCode());
		bo.setAllocatedQty(allocation.getAllocationQty());
		bo.setAllocatedDate(allocation.getAllocatedDate());
		bo.setReleasedDate(allocation.getReleasedDate());

		final int stateID = allocation.getAllocationState().getStateId();
		bo.setAllocationState(stateID);
		bo.setAllocationStateBO(AllocationStateBO.valueOf(stateID));

		final AllocationType type = allocation.getAllocationTypeBean();
		bo.setAllocationTypeBO(AllocationTypeBO.valueOf(type
				.getAllocationTypeId()));
		bo.setAllocationType(type.getAllocationTypeId());

		final CrownEntity ent = allocation.getCrownEntity();
		bo.setEntityBO(new GeneralHelper().createCrownEntityBO(ent));
		bo.setEntity(ent.getEntityId());

		final Item item = allocation.getItem();
		bo.setItem(item.getItemId());
		bo.setItemBO(new ItemHelper().createItemBO(item));
		return bo;
	}

	public StockMovement createStockMovement(final StockMovementBO bo) {
		return createStockMovement(bo, null);
	}

	public StockMovement createStockMovement(final StockMovementBO bo,
			final StockMovement toEdit) {
		final StockMovement movement = (toEdit == null) ? new StockMovement()
				: toEdit;
		movement.setVersion(bo.getVersion());
		final Date actionDate = new Date();
		switch (bo.getMoveStatusBO()) {
			case MOVED:
				movement.setMovedDate(actionDate);
				break;
			case ACCEPTED:
			case PARTIAL_ACCEPT:
				movement.setReceivedDate(actionDate);
				break;
			case RETURNED:
				movement.setReturnedDate(actionDate);
				break;
			default:
				break;
		}

		movement.setComments(bo.getComments());
		return movement;
	}

	public StockMovementItem createStockMovementItem(int movementID,
			StockMovementItemBO bo) {
		final StockMovementItem item = new StockMovementItem();

		final StockMovementItemPK pk = new StockMovementItemPK();
		pk.setMovementId(movementID);
		pk.setItemId(bo.getItem());

		item.setId(pk);
		item.setMovedQty(bo.getMovedQty());
		return item;
	}

	public StockMovementBO createStockMovementBO(final StockMovement movement) {
		final StockMovementBO bo = new StockMovementBO();
		bo.setId(movement.getMovementId());
		bo.setVersion(movement.getVersion());
		bo.setMoveReceiptID(movement.getMoveReceiptId());
		bo.setMoveDate(movement.getMovedDate());
		bo.setReceiveDate(movement.getReceivedDate());

		final int moveStatus = movement.getMoveStatusBean().getMoveStatusId();
		bo.setMoveStatus(moveStatus);
		bo.setMoveStatusBO(MoveStatusBO.valueOf(moveStatus));

		final WarehouseHelper warehouseHelper = new WarehouseHelper();
		final Warehouse fromWarehouse = movement.getFromWarehouse();
		bo.setFromWarehouse(fromWarehouse.getWarehouseId());
		bo.setFromWarehouseBO(warehouseHelper.createWarehouseBO(fromWarehouse));

		final Warehouse toWarehouse = movement.getToWarehouse();
		bo.setToWarehouse(toWarehouse.getWarehouseId());
		bo.setToWarehouseBO(warehouseHelper.createWarehouseBO(toWarehouse));

		bo.setComments(movement.getComments());

		Set<StockMovementItem> moveItems = movement.getStockMovementItems();
		for (StockMovementItem moveItem : moveItems) {
			bo.addStockMovementItem(createStockMovementItemBO(moveItem));
		}

		return bo;
	}

	public StockMovementItemBO createStockMovementItemBO(StockMovementItem item) {
		final StockMovementItemBO bo = new StockMovementItemBO();

		final StockMovementItemPK pk = item.getId();
		bo.setId(pk.getMovementId());
		bo.setItem(pk.getItemId());

		final AllocationType type = item.getAllocationTypeBean();
		bo.setAllocationTypeBO(AllocationTypeBO.valueOf(type
				.getAllocationTypeId()));
		bo.setAllocationType(type.getAllocationTypeId());
		bo.setMovedQty(item.getMovedQty());
		bo.setReceivedQty(item.getReceivedQty());
		bo.setReturnedQty(item.getReturnedQty());

		return bo;
	}
}
