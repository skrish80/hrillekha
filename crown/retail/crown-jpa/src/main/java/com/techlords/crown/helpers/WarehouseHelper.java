/**
 * 
 */
package com.techlords.crown.helpers;

import java.util.Date;

import com.techlords.crown.business.model.StolenStockBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.business.model.WarehouseStockBO;
import com.techlords.crown.persistence.CrownEntity;
import com.techlords.crown.persistence.CrownUser;
import com.techlords.crown.persistence.Item;
import com.techlords.crown.persistence.Location;
import com.techlords.crown.persistence.StolenStock;
import com.techlords.crown.persistence.Warehouse;
import com.techlords.crown.persistence.WarehouseStock;
import com.techlords.crown.persistence.WarehouseStockPK;

/**
 * @author gv
 * 
 */
public final class WarehouseHelper {

	public WarehouseBO createWarehouseBO(Warehouse warehouse) {
		final WarehouseBO bo = new WarehouseBO();
		bo.setId(warehouse.getWarehouseId());
		bo.setVersion(warehouse.getVersion());
		bo.setWarehouseName(warehouse.getWarehouseName());
		bo.setDescription(warehouse.getDescription());
		bo.setAddress(warehouse.getAddress());
		bo.setRetailShop(warehouse.isRetailShop());

		final Location loc = warehouse.getLocationBean();
		bo.setLocation(loc.getLocationId());
		bo.setLocationBO(new LocationHelper().createLocationBO(loc));

		final CrownEntity ent = warehouse.getCrownEntity();
		bo.setEntity(ent.getEntityId());
		bo.setEntityBO(new GeneralHelper().createCrownEntityBO(ent));

		final CrownUser incharge = warehouse.getCrownUser();
		bo.setIncharge(incharge.getUserId());
		bo.setInchargeBO(new CrownUserHelper().createCrownUserBO(incharge));

		return bo;
	}

	public Warehouse createWarehouse(WarehouseBO bo) {
		return createWarehouse(bo, null);
	}

	public Warehouse createWarehouse(WarehouseBO bo, Warehouse toEdit) {
		final Warehouse warehouse = (toEdit == null) ? new Warehouse() : toEdit;
		warehouse.setVersion(bo.getVersion());
		warehouse.setWarehouseName(bo.getWarehouseName());
		warehouse.setDescription(bo.getDescription());
		warehouse.setAddress(bo.getAddress());
		warehouse.setRetailShop(bo.isRetailShop());
		return warehouse;
	}

	public WarehouseStockBO createWarehouseStockBO(WarehouseStock warehouseItem) {
		final WarehouseStockBO bo = new WarehouseStockBO();
		final ItemHelper helper = new ItemHelper();

		final Item item = warehouseItem.getItem();
		bo.setItemID(item.getItemId());
		bo.setItemBO(helper.createItemBO(item));

		final Warehouse warehouse = warehouseItem.getWarehouse();
		bo.setWarehouseID(warehouse.getWarehouseId());
		bo.setWarehouseBO(createWarehouseBO(warehouse));

		Integer itemQty = warehouseItem.getItemQuantity();
		if (itemQty == null) {
			itemQty = 0;
		}
		Integer uomQty = warehouseItem.getUomQuantity();
		if (uomQty == null) {
			uomQty = 0;
		}
		bo.setItemQty(itemQty);
		bo.setUomQty(uomQty);

		return bo;
	}

	public WarehouseStock createWarehouseStock(WarehouseStockBO bo) {
		return createWarehouseStock(bo, null);
	}

	public WarehouseStock createWarehouseStock(WarehouseStockBO bo,
			WarehouseStock toEdit) {
		final WarehouseStock warehouseItem = (toEdit == null) ? new WarehouseStock()
				: toEdit;
		if (toEdit == null) {
			WarehouseStockPK pk = new WarehouseStockPK();
			pk.setItemId(bo.getItemID());
			pk.setWarehouseId(bo.getWarehouseID());
			warehouseItem.setId(pk);
		}
		int itemQty = bo.getItemQty();
		int uomQty = bo.getUomQty();
		if (toEdit != null) {
			itemQty += warehouseItem.getItemQuantity();
			uomQty += warehouseItem.getUomQuantity();
		}
		warehouseItem.setItemQuantity(itemQty);
		warehouseItem.setUomQuantity(uomQty);

		return warehouseItem;
	}

	public StolenStockBO createStolenStockBO(StolenStock stolen) {
		final StolenStockBO bo = new StolenStockBO();
		bo.setId(stolen.getStolenId());
		bo.setVersion(stolen.getVersion());
		bo.setStolenDate(stolen.getStolenDate());
		bo.setStolenItemQty(stolen.getStolenItemQty());
		bo.setStolenUOMQty(stolen.getStolenUomQty());

		Item item = stolen.getItem();
		bo.setItem(item.getItemId());
		bo.setItemBO(new ItemHelper().createItemBO(item));

		Warehouse warehouse = stolen.getWarehouseBean();
		bo.setWarehouse(warehouse.getWarehouseId());
		bo.setWarehouseBO(createWarehouseBO(warehouse));

		CrownUser user = stolen.getCreatedBy();
		bo.setCreatedBy(user.getFirstName() + " " + user.getLastName());

		bo.setRemarks(stolen.getRemarks());
		return bo;
	}

	public StolenStock createStolenStock(StolenStockBO bo) {
		return createStolenStock(bo, null);
	}

	public StolenStock createStolenStock(StolenStockBO bo, StolenStock toEdit) {
		final StolenStock stolen = (toEdit == null) ? new StolenStock()
				: toEdit;
		stolen.setVersion(bo.getVersion());
		stolen.setStolenDate(new Date());
		stolen.setStolenItemQty(bo.getStolenItemQty());
		stolen.setStolenUomQty(bo.getStolenUOMQty());
		stolen.setRemarks(bo.getRemarks());
		return stolen;
	}

}
