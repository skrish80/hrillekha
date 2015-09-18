/**
 * 
 */
package com.techlords.crown.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.OptimisticLockException;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CrownEntityBO;
import com.techlords.crown.business.model.SearchCriteria;
import com.techlords.crown.business.model.StockAllocationBO;
import com.techlords.crown.business.model.StockMovementBO;
import com.techlords.crown.business.model.StockMovementItemBO;
import com.techlords.crown.business.model.enums.AllocationStateBO;
import com.techlords.crown.business.model.enums.MoveStatusBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.helpers.AuditActionEnum;
import com.techlords.crown.helpers.GeneralHelper;
import com.techlords.crown.helpers.StockHelper;
import com.techlords.crown.persistence.AllocationState;
import com.techlords.crown.persistence.AllocationType;
import com.techlords.crown.persistence.CrownEntity;
import com.techlords.crown.persistence.Item;
import com.techlords.crown.persistence.MoveStatus;
import com.techlords.crown.persistence.Status;
import com.techlords.crown.persistence.StockAllocation;
import com.techlords.crown.persistence.StockMovement;
import com.techlords.crown.persistence.StockMovementItem;
import com.techlords.crown.persistence.StockMovementItemPK;
import com.techlords.crown.persistence.TotalStock;
import com.techlords.crown.persistence.Warehouse;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.StockService;
import com.techlords.crown.service.WarehouseService;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
final class StockServiceImpl extends AbstractCrownService implements StockService {
	private final StockHelper helper = new StockHelper();
	private static final String ALLOC_PREFIX = "ALL";
	private static final String MOV_PREFIX = "MOV";

	private void updateAssociations(StockAllocationBO bo, StockAllocation allocation) {
		allocation
				.setAllocationTypeBean(manager.find(AllocationType.class, bo.getAllocationType()));
		allocation.setCrownEntity(manager.find(CrownEntity.class, bo.getEntity()));
		allocation.setItem(manager.find(Item.class, bo.getItem()));
		allocation.setStatusBean(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));
		allocation.setVersion(bo.getVersion());
	}

	@Transactional
	@Override
	public boolean createStockAllocation(StockAllocationBO bo, int userID) throws CrownException {
		try {
			final StockAllocation allocation = helper.createStockAllocation(bo);
			final GeneralService generalService = CrownServiceLocator.INSTANCE
					.getCrownService(GeneralService.class);
			final String allocCode = ALLOC_PREFIX + GeneralHelper.getFormattedRunningDate()
					+ generalService.getRunningSequenceNumber("STOCK_ALLOCATION", "ALLOCATED_DATE");
			allocation.setAllocationCode(allocCode);
			updateAssociations(bo, allocation);
			allocation.setAllocationState(manager.find(AllocationState.class,
					AllocationStateBO.ALLOCATED.getAllocationStateID()));
			manager.persist(allocation);
			manager.getEntityManagerFactory().getCache().evict(TotalStock.class);
			auditLog(AuditActionEnum.CREATE, userID, "stockAllocation",
					allocation.getAllocationCode());
		} catch (Exception e) {
			throw new CrownException("Allocation cannot be created", e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean updateStockAllocation(StockAllocationBO bo, int userID) throws CrownException {
		try {
			final StockAllocation allocation = helper.createStockAllocation(bo,
					manager.find(StockAllocation.class, bo.getId()));
			updateAssociations(bo, allocation);
			manager.merge(allocation);
			auditLog(AuditActionEnum.UPDATE, userID, "stockAllocation",
					allocation.getAllocationCode());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Allocation has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean releaseAllocation(StockAllocationBO bo, int userID) throws CrownException {
		try {
			final StockAllocation allocation = manager.find(StockAllocation.class, bo.getId());
			allocation.setVersion(bo.getVersion());
			allocation.setReleasedDate(new Date());
			allocation.setAllocationState(manager.find(AllocationState.class,
					AllocationStateBO.RELEASED.getAllocationStateID()));
			manager.merge(allocation);
			auditLog(AuditActionEnum.DELETE, userID, "stockAllocation",
					allocation.getAllocationCode());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Allocation has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StockAllocationBO> findAllAllocatedStock() {
		final List<StockAllocation> allocations = manager.createQuery(
				"select SA from StockAllocation SA order by SA.allocatedDate desc").getResultList();

		final List<StockAllocationBO> bos = new ArrayList<StockAllocationBO>();
		for (final StockAllocation allocation : allocations) {
			bos.add(helper.createStockAllocationBO(allocation));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StockAllocationBO> searchAllocatedStock(SearchCriteria searchCriteria) {
		final GeneralHelper generalHelper = new GeneralHelper();
		final String stockAllocationQuery = generalHelper.buildStockAllocationQuery(searchCriteria);
		Query query = getNativeQuery(stockAllocationQuery, StockAllocation.class);

		final List<StockAllocationBO> bos = new ArrayList<StockAllocationBO>();

		final List<StockAllocation> allocations = query.getResultList();
		for (final StockAllocation allocation : allocations) {
			bos.add(helper.createStockAllocationBO(allocation));
		}
		return bos;
	}

	@Override
	public List<StockAllocationBO> findAllocatedStock(CrownEntityBO entityBO) {
		return null;
	}

	// ////////////////STOCK MOVEMENT//////////////////////////////////
	private void updateMovementAssociations(StockMovement movement, StockMovementBO bo) {
		movement.setFromWarehouse(manager.find(Warehouse.class, bo.getFromWarehouse()));
		movement.setToWarehouse(manager.find(Warehouse.class, bo.getToWarehouse()));
	}

	@Transactional(propagation = Propagation.NESTED)
	private void createStockMovementItems(StockMovement movement, StockMovementBO bo) {
		final List<StockMovementItemBO> itemBOs = bo.getStockMovementItems();
		final Set<StockMovementItem> items = new HashSet<StockMovementItem>();
		for (StockMovementItemBO itemBO : itemBOs) {
			StockMovementItem item = helper.createStockMovementItem(movement.getMovementId(),
					itemBO);
			item.setAllocationTypeBean(manager.find(AllocationType.class,
					itemBO.getAllocationType()));
			// PERSIST THE ITEM
			manager.persist(item);
			items.add(item);
		}
		movement.setStockMovementItems(items);
		manager.merge(movement);
	}

	@Transactional
	private void updateMovementOnReceive(StockMovement movement, StockMovementBO bo)
			throws CrownException {
		final List<StockMovementItemBO> itemBOs = bo.getStockMovementItems();
		final WarehouseService warehouseService = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);
		final int moveStatusID = bo.getMoveStatus();
		for (StockMovementItemBO itemBO : itemBOs) {
			StockMovementItemPK pk = new StockMovementItemPK();
			pk.setMovementId(bo.getId());
			pk.setItemId(itemBO.getItem());

			StockMovementItem item = manager.find(StockMovementItem.class, pk);

			if (moveStatusID == MoveStatusBO.ACCEPTED.getMoveStatusID()
					|| moveStatusID == MoveStatusBO.PARTIAL_ACCEPT.getMoveStatusID()) {
				final Integer recdQty = itemBO.getReceivedQty();
				item.setReceivedQty(recdQty);
				// UPDATE WAREHOUSE STOCK
				warehouseService.updateWarehouseStock(bo.getToWarehouse(), pk.getItemId(),
						itemBO.getAllocationType(), recdQty, false);
				warehouseService.updateWarehouseStock(bo.getFromWarehouse(), pk.getItemId(),
						itemBO.getAllocationType(), recdQty, true);

			} else if (moveStatusID == MoveStatusBO.RETURNED.getMoveStatusID()) {
				final Integer returnedQty = itemBO.getReturnedQty();
				item.setReturnedQty(returnedQty);
				// // UPDATE WAREHOUSE STOCK
				// NO NEED TO UPDATE WAREHOUSE STOCK ON RETURN
				// warehouseService.updateWarehouseStock(bo.getFromWarehouse(),
				// pk.getItemId(), itemBO.getAllocationType(),
				// returnedQty, false);
			}
			manager.merge(item);
		}
	}

	@Transactional(propagation = Propagation.NESTED)
	@Override
	public boolean createStockMovement(StockMovementBO bo, int userID) throws CrownException {
		try {
			StockMovement movement = helper.createStockMovement(bo);
			final GeneralService generalService = CrownServiceLocator.INSTANCE
					.getCrownService(GeneralService.class);
			final String moveReceipt = MOV_PREFIX + GeneralHelper.getFormattedRunningDate()
					+ generalService.getRunningSequenceNumber("STOCK_MOVEMENT", "MOVED_DATE");
			movement.setMoveReceiptId(moveReceipt);
			updateMovementAssociations(movement, bo);
			movement.setMoveStatusBean(manager.find(MoveStatus.class,
					MoveStatusBO.MOVED.getMoveStatusID()));
			manager.persist(movement);

			// ASSOCIATE THE MOVEMENT ITEMS
			createStockMovementItems(movement, bo);

			auditLog(AuditActionEnum.CREATE, userID, "stockMovement", movement.getMoveReceiptId());
		} catch (Exception e) {
			throw new CrownException("Movement cannot be created", e);
		}
		return true;
	}

	@Transactional(propagation = Propagation.NESTED)
	@Override
	public boolean updateStockMovement(StockMovementBO bo, int userID) throws CrownException {
		try {
			final StockMovement movement = helper.createStockMovement(bo,
					manager.find(StockMovement.class, bo.getId()));
			movement.setVersion(bo.getVersion());
			movement.setMoveStatusBean(manager.find(MoveStatus.class, bo.getMoveStatus()));
			updateMovementOnReceive(movement, bo);
			manager.merge(movement);
			AuditActionEnum auditAction = AuditActionEnum.UPDATE;
			switch (MoveStatusBO.valueOf(bo.getMoveStatus())) {
				case ACCEPTED:
					auditAction = AuditActionEnum.ACCEPT;
					break;
				case PARTIAL_ACCEPT:
					auditAction = AuditActionEnum.PARTIAL_ACCEPT;
					break;
				case RETURNED:
					auditAction = AuditActionEnum.RETURN;
					break;
				case CANCELLED:
					auditAction = AuditActionEnum.CANCEL;
					break;
				case CLOSED:
					auditAction = AuditActionEnum.CLOSED;
					break;
				default:
					break;
			}
			auditLog(auditAction, userID, "stockMovement", movement.getMoveReceiptId());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Stock Movement has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean cancelStockMovement(StockMovementBO bo, int userID) throws CrownException {
		try {
			final StockMovement movement = manager.find(StockMovement.class, bo.getId());
			movement.setVersion(bo.getVersion());
			movement.setMoveStatusBean(manager.find(MoveStatus.class,
					MoveStatusBO.CANCELLED.getMoveStatusID()));
			manager.merge(movement);
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Stock Movement has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StockMovementBO> findAllMovementStock() {
		final List<StockMovement> allocations = manager.createQuery(
				"select SM from StockMovement SM order by SM.movedDate desc").getResultList();

		final List<StockMovementBO> bos = new ArrayList<StockMovementBO>();
		for (final StockMovement move : allocations) {
			bos.add(helper.createStockMovementBO(move));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StockAllocationBO> findUnavailableStockForEntity(Integer entityID,
			Object... itemIDs) {

		final GeneralHelper generalHelper = new GeneralHelper();
		final String stockAllocationQuery = generalHelper.buildUnavailableEntityStockQuery(
				entityID, itemIDs);
		// SELECT ITEM_ID, SUM(ALLOCATION_QTY) AS ALLOCATION_QTY,
		// ALLOCATION_TYPE FROM STOCK_ALLOCATION WHERE ENTITY <> 1 AND ITEM_ID
		// IN (itemIDs) AND STATE = 1 GROUP BY ITEM_ID,ALLOCATION_TYPE
		// TODO no other go
		final Query query = getNativeQuery(stockAllocationQuery);
		final List<StockAllocationBO> bos = new ArrayList<StockAllocationBO>();
		final List<Object[]> list = query.getResultList();
		for (Object[] obj : list) {
			final StockAllocationBO bo = new StockAllocationBO();
			bo.setItem((Integer) obj[0]);
			bo.setAllocatedQty(((Long) obj[1]).intValue());
			bo.setAllocationType((Integer) obj[2]);
			bos.add(bo);
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public boolean updateStockAllocationOnDelivery(int entity, int item, int allocationType, int qty)
			throws CrownException {
		try {
			List<StockAllocation> allocations = manager
					.createNamedQuery("StockAllocation.findAllocatedStock").setParameter(1, item)
					.setParameter(2, entity).getResultList();
			final AllocationState deliveredState = manager.find(AllocationState.class,
					AllocationStateBO.DELIVERED.getAllocationStateID());
			for (StockAllocation alloc : allocations) {
				int allocatedQty = alloc.getAllocationQty();

				if (alloc.getAllocationTypeBean().getAllocationTypeId() == allocationType) {
					allocatedQty -= qty;
					alloc.setAllocationQty(allocatedQty);
				}
				if (allocatedQty <= 0) {
					alloc.setAllocationQty(0);
					alloc.setAllocationState(deliveredState);
				}
				manager.merge(alloc);
			}
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Allocation has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public boolean updateStockAllocationOnStolen(int item, int allocationType, int qty)
			throws CrownException {
		try {
			List<StockAllocation> allocations = manager
					.createNamedQuery("StockAllocation.findAllocationByItem").setParameter(1, item)
					.getResultList();
			final AllocationState stolenSate = manager.find(AllocationState.class,
					AllocationStateBO.STOLEN_DAMAGED.getAllocationStateID());
			for (StockAllocation alloc : allocations) {
				int allocatedQty = alloc.getAllocationQty();

				if (alloc.getAllocationTypeBean().getAllocationTypeId() == allocationType) {
					allocatedQty -= qty;
					alloc.setAllocationQty(allocatedQty);
				}
				if (allocatedQty <= 0) {
					alloc.setAllocationQty(0);
					alloc.setAllocationState(stolenSate);
				}
				manager.merge(alloc);
			}
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Allocation has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return false;
	}
}
