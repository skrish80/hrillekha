/**
 * 
 */
package com.techlords.crown.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.OptimisticLockException;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.StolenStockBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.business.model.WarehouseStockBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.helpers.AuditActionEnum;
import com.techlords.crown.helpers.GeneralHelper;
import com.techlords.crown.helpers.WarehouseHelper;
import com.techlords.crown.persistence.CrownEntity;
import com.techlords.crown.persistence.CrownUser;
import com.techlords.crown.persistence.Item;
import com.techlords.crown.persistence.Location;
import com.techlords.crown.persistence.Status;
import com.techlords.crown.persistence.StockMovementItem;
import com.techlords.crown.persistence.StolenStock;
import com.techlords.crown.persistence.TotalStock;
import com.techlords.crown.persistence.Warehouse;
import com.techlords.crown.persistence.WarehouseStock;
import com.techlords.crown.persistence.WarehouseStockPK;
import com.techlords.crown.service.StockService;
import com.techlords.crown.service.WarehouseService;
import com.techlords.infra.CrownConstants;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
final class WarehouseServiceImpl extends AbstractCrownService implements WarehouseService {
	private final WarehouseHelper helper = new WarehouseHelper();

	private void setWarehouseAssociations(WarehouseBO bo, Warehouse warehouse) {
		warehouse.setCrownEntity(manager.find(CrownEntity.class, bo.getEntity()));
		warehouse.setLocationBean(manager.find(Location.class, bo.getLocation()));
		warehouse.setCrownUser(manager.find(CrownUser.class, bo.getIncharge()));
		warehouse.setStatusBean(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));
		warehouse.setVersion(bo.getVersion());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.WarehouseService#createWarehouse(com.techlords
	 * .crown.business.model.WarehouseBO, int)
	 */
	@Transactional(propagation = Propagation.NESTED)
	@Override
	public boolean createWarehouse(WarehouseBO bo, int userID) throws CrownException {
		try {
			final Warehouse warehouse = helper.createWarehouse(bo);
			if (bo.isRetailShop()) {
				createRetailShopEntity(bo);
			}
			// // ONLY IF THE WAREHOUSE BELONGS TO RETAIL ENTITY
			// if (bo.getEntity() == 1) {
			// warehouse.setRetailShop(true);
			// }
			setWarehouseAssociations(bo, warehouse);
			manager.persist(warehouse);
			auditLog(AuditActionEnum.CREATE, userID,
					bo.isRetailShop() ? "retailshop" : "warehouse", warehouse.getWarehouseName());
		} catch (Exception e) {
			throw new CrownException("Warehouse/Shop cannot be created", e);
		}
		return true;
	}

	/**
	 * Create an entity for the shop
	 * 
	 * @param bo
	 */
	@Transactional
	private void createRetailShopEntity(WarehouseBO bo) {
		final CrownEntity entity = new CrownEntity();
		entity.setEntityName(bo.getWarehouseName());
		entity.setEntityType(CrownConstants.RETAIL);
		entity.setDescription(bo.getDescription());
		manager.persist(entity);
		bo.setEntity(entity.getEntityId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.WarehouseService#updateWarehouse(com.techlords
	 * .crown.business.model.WarehouseBO, int)
	 */
	@Transactional
	@Override
	public boolean updateWarehouse(WarehouseBO bo, int userID) throws CrownException {
		try {
			final Warehouse warehouse = helper.createWarehouse(bo,
					manager.find(Warehouse.class, bo.getId()));
			setWarehouseAssociations(bo, warehouse);
			manager.merge(warehouse);
			auditLog(AuditActionEnum.UPDATE, userID,
					bo.isRetailShop() ? "retailshop" : "warehouse", warehouse.getWarehouseName());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Warehouse has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.WarehouseService#deleteWarehouse(com.techlords
	 * .crown.business.model.WarehouseBO, int)
	 */
	@Transactional
	@Override
	public boolean deleteWarehouse(WarehouseBO bo, int userID) throws CrownException {
		try {
			final Warehouse warehouse = manager.find(Warehouse.class, bo.getId());
			warehouse.setVersion(bo.getVersion());
			warehouse.setStatusBean(manager.find(Status.class, StatusBO.DISABLED.getStatusID()));
			manager.merge(warehouse);

			auditLog(AuditActionEnum.DELETE, userID,
					bo.isRetailShop() ? "retailshop" : "warehouse", warehouse.getWarehouseName());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Warehouse has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.WarehouseService#findAllWarehouses()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WarehouseBO> findAllWarehouses() {
		final List<Warehouse> warehouses = manager
				.createQuery(
						"select W from Warehouse W where W.statusBean.statusId=1 and W.isRetailShop=false order by W.warehouseName")
				.getResultList();

		final List<WarehouseBO> bos = new ArrayList<WarehouseBO>();
		for (final Warehouse house : warehouses) {
			bos.add(helper.createWarehouseBO(house));
		}
		return bos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.WarehouseService#findAllWarehouses()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WarehouseBO> findAllRetailShops() {
		// Get the retail shops and the retail warehouses
		final List<Warehouse> warehouses = manager
				.createQuery(
						"select W from Warehouse W where W.statusBean.statusId=1 and W.isRetailShop=true order by W.warehouseName")
				.getResultList();

		final List<WarehouseBO> bos = new ArrayList<WarehouseBO>();
		for (final Warehouse house : warehouses) {
			bos.add(helper.createWarehouseBO(house));
		}
		return bos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.WarehouseService#findAllWarehouses()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WarehouseBO> findAllRetailWarehouses() {
		// Get the retail warehouses
		final List<Warehouse> warehouses = manager
				.createQuery(
						"select W from Warehouse W where W.crownEntity.entityId=1 order by W.warehouseName")
				.getResultList();

		final List<WarehouseBO> bos = new ArrayList<WarehouseBO>();
		for (final Warehouse house : warehouses) {
			bos.add(helper.createWarehouseBO(house));
		}
		return bos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.WarehouseService#updateWarehouseStock(com
	 * .techlords.crown.business.model.WarehouseStockBO, int)
	 */
	@Transactional
	@Override
	public boolean createWarehouseStock(WarehouseStockBO bo, int userID) throws CrownException {
		try {
			final WarehouseStock warehouseStock = helper.createWarehouseStock(bo);
			warehouseStock.setItem(manager.find(Item.class, bo.getItemID()));
			warehouseStock.setWarehouse(manager.find(Warehouse.class, bo.getWarehouseID()));
			manager.persist(warehouseStock);
			auditLog(
					AuditActionEnum.CREATE,
					userID,
					"warehouseStock",
					warehouseStock.getWarehouse().getWarehouseName() + " "
							+ warehouseStock.getItem().getItemName() + "-"
							+ warehouseStock.getItemQuantity() + "-"
							+ warehouseStock.getUomQuantity());
			manager.getEntityManagerFactory().getCache().evict(TotalStock.class);
		} catch (Exception e) {
			throw new CrownException("Warehouse Stock cannot be created", e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.WarehouseService#updateWarehouseStock(com
	 * .techlords.crown.business.model.WarehouseStockBO, int)
	 */
	@Transactional
	@Override
	public boolean updateWarehouseStock(WarehouseStockBO bo, int userID) throws CrownException {
		try {
			final WarehouseStockPK pk = new WarehouseStockPK();
			pk.setItemId(bo.getItemID());
			pk.setWarehouseId(bo.getWarehouseID());

			final WarehouseStock toEdit = manager.find(WarehouseStock.class, pk);
			// To create a warehouse stock during stock movement when the item
			// doesn't exist in this warehouse
			if (toEdit == null) {
				return createWarehouseStock(bo, userID);
			}
			final WarehouseStock warehouseStock = helper.createWarehouseStock(bo, toEdit);
			manager.merge(warehouseStock);
			manager.getEntityManagerFactory().getCache().evict(TotalStock.class);
			auditLog(
					AuditActionEnum.UPDATE,
					userID,
					"warehouseStock",
					warehouseStock.getWarehouse().getWarehouseName() + " "
							+ warehouseStock.getItem().getItemName() + "-"
							+ warehouseStock.getItemQuantity() + "-"
							+ warehouseStock.getUomQuantity());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Warehouse Stock has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.WarehouseService#getWarehouseStok(com.techlords
	 * .crown.business.model.WarehouseBO)
	 */
	@Override
	public List<ItemBO> getWarehouseStock(WarehouseBO bo) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WarehouseStockBO> findAllWarehouseStock() {
		final List<WarehouseStock> warehouseItems = manager.createQuery(
				"select W from WarehouseStock W order by W.warehouse.warehouseName")
				.getResultList();

		final List<WarehouseStockBO> bos = new ArrayList<WarehouseStockBO>();
		for (final WarehouseStock warehouseItem : warehouseItems) {
			bos.add(helper.createWarehouseStockBO(warehouseItem));
		}
		return bos;
	}

	@Transactional(propagation = Propagation.NESTED)
	@Override
	public boolean createStolenStock(StolenStockBO bo, int userID) throws CrownException {
		try {
			final StolenStock stolen = helper.createStolenStock(bo);
			setStolenAssociations(bo, stolen);
			stolen.setCreatedBy(manager.find(CrownUser.class, userID));
			manager.persist(stolen);
			updateWarehouseStockOnStolen(stolen, 0, 0);

			final StockService stockService = CrownServiceLocator.INSTANCE
					.getCrownService(StockService.class);

			// REMOVE ALLOCATION FOR ALL ALLOCATIONS WITH THIS ITEM
			int stolenQty = bo.getStolenUOMQty();
			if (stolenQty > 0) {
				stockService.updateStockAllocationOnStolen(bo.getItem(),
						AllocationTypeBO.UOM.getAllocationTypeID(), stolenQty);
			}
			stolenQty = bo.getStolenItemQty();
			if (stolenQty > 0) {
				stockService.updateStockAllocationOnStolen(bo.getItem(),
						AllocationTypeBO.ITEM.getAllocationTypeID(), stolenQty);
			}

			final String auditItem = stolen.getItem().getItemName() + " "
					+ stolen.getWarehouseBean().getWarehouseName() + "-"
					+ stolen.getStolenItemQty() + "-" + stolen.getStolenUomQty();
			auditLog(AuditActionEnum.CREATE, userID, "stolenStock", auditItem);

			manager.getEntityManagerFactory().getCache().evict(TotalStock.class);
		} catch (Exception e) {
			throw new CrownException("Stolen Stock cannot be created", e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean updateStolenStock(StolenStockBO bo, int userID) throws CrownException {
		try {
			final StolenStock old = manager.find(StolenStock.class, bo.getId());
			final Integer oldItemQty = old.getStolenItemQty();
			final Integer oldUOMQty = old.getStolenUomQty();

			final StolenStock stolen = helper.createStolenStock(bo, old);
			setStolenAssociations(bo, stolen);
			manager.merge(stolen);

			updateWarehouseStockOnStolen(stolen, oldItemQty, oldUOMQty);
			manager.getEntityManagerFactory().getCache().evict(TotalStock.class);

			final String auditItem = stolen.getItem().getItemCode() + " "
					+ stolen.getWarehouseBean().getWarehouseName() + "-"
					+ stolen.getStolenItemQty() + "-" + stolen.getStolenUomQty();
			auditLog(AuditActionEnum.UPDATE, userID, "stolenStock", auditItem);
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Stolen Stock has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	private void setStolenAssociations(StolenStockBO bo, StolenStock stolen) {
		stolen.setItem(manager.find(Item.class, bo.getItem()));
		stolen.setWarehouseBean(manager.find(Warehouse.class, bo.getWarehouse()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StolenStockBO> findAllStolenItems() {
		final List<StolenStockBO> bos = new ArrayList<StolenStockBO>();

		final List<StolenStock> stolenItems = manager.createQuery(
				"select S from StolenStock S order by S.stolenDate desc").getResultList();
		for (final StolenStock stolen : stolenItems) {
			bos.add(helper.createStolenStockBO(stolen));
		}
		return bos;
	}

	private void updateWarehouseStockOnStolen(StolenStock stolen, Integer oldItemQty,
			Integer oldUOMQty) throws CrownException {
		try {
			WarehouseStockPK pk = new WarehouseStockPK();
			pk.setItemId(stolen.getItem().getItemId());
			pk.setWarehouseId(stolen.getWarehouseBean().getWarehouseId());

			final WarehouseStock stock = manager.find(WarehouseStock.class, pk);
			stock.setItemQuantity(stock.getItemQuantity() - stolen.getStolenItemQty() + oldItemQty);
			stock.setUomQuantity(stock.getUomQuantity() - stolen.getStolenUomQty() + oldUOMQty);
			manager.merge(stock);
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Warehouse Stock has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WarehouseStockBO> findWarehouseStock(int warehouseID, Object... itemIDs) {
		final GeneralHelper generalHelper = new GeneralHelper();
		final String stockAllocationQuery = generalHelper.buildWarehouseStockQuery(warehouseID,
				itemIDs);
		Query query = getNativeQuery(stockAllocationQuery, WarehouseStock.class);
		final List<WarehouseStockBO> bos = new ArrayList<WarehouseStockBO>();

		final List<WarehouseStock> stocks = query.getResultList();

		final String moveQuery = generalHelper.buildMovedStockQuery(warehouseID, itemIDs);
		Query blockedForMoveQuery = getNativeQuery(moveQuery, StockMovementItem.class);
		List<StockMovementItem> items = blockedForMoveQuery.getResultList();
		for (final WarehouseStock stock : stocks) {
			WarehouseStockBO bo = helper.createWarehouseStockBO(stock);
			int itemQty = bo.getItemQty();
			int uomQty = bo.getUomQty();
			for (StockMovementItem itm : items) {
				if (stock.getId().getItemId().equals(itm.getId().getItemId())) {
					if (itm.getAllocationTypeBean().getAllocationTypeId() == AllocationTypeBO.ITEM
							.getAllocationTypeID()) {
						itemQty -= itm.getMovedQty();
					} else {
						uomQty -= itm.getMovedQty();
					}
				}
			}
			bo.setItemQty(itemQty);
			bo.setUomQty(uomQty);
			bos.add(bo);
		}
		return bos;
	}

	@Override
	@Transactional
	public boolean updateWarehouseStock(int warehouseID, int itemID, int allocationType,
			Integer quantity, boolean isDeduction) throws CrownException {

		try {
			final WarehouseStockPK pk = new WarehouseStockPK();
			pk.setItemId(itemID);
			pk.setWarehouseId(warehouseID);
			final WarehouseStock stock = manager.find(WarehouseStock.class, pk);
			if (stock == null) {
				return doCreateWarehouseStock(pk, allocationType, quantity);
			}
			return doUpdateWarehouseStock(stock, allocationType, quantity, isDeduction);
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Warehouse Stock has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
	}

	@Transactional
	private boolean doCreateWarehouseStock(WarehouseStockPK pk, int allocationType, Integer quantity) {
		final WarehouseStock stock = new WarehouseStock();
		stock.setId(pk);
		if (allocationType == AllocationTypeBO.UOM.getAllocationTypeID()) {
			stock.setUomQuantity(quantity);
		} else {
			stock.setItemQuantity(quantity);
		}
		stock.setItem(manager.find(Item.class, pk.getItemId()));
		stock.setWarehouse(manager.find(Warehouse.class, pk.getWarehouseId()));
		manager.persist(stock);
		return true;
	}

	@Transactional
	private boolean doUpdateWarehouseStock(WarehouseStock stock, int allocationType,
			Integer quantity, boolean isDeduction) {
		if (allocationType == AllocationTypeBO.UOM.getAllocationTypeID()) {
			Integer qty = stock.getUomQuantity();
			if (qty == null) {
				qty = 0;
			}
			qty += (isDeduction) ? -quantity : quantity;
			stock.setUomQuantity(qty);
		} else {
			Integer qty = stock.getItemQuantity();
			if (qty == null) {
				qty = 0;
			}
			qty += (isDeduction) ? -quantity : quantity;
			stock.setItemQuantity(qty);
		}
		manager.merge(stock);
		return true;
	}
}
