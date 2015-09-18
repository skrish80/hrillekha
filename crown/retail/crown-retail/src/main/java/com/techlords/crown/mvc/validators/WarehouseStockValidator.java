package com.techlords.crown.mvc.validators;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.WarehouseStockBO;

public class WarehouseStockValidator {

	public void validateStockCreation(WarehouseStockBO bo)
			throws CrownException {
		if (bo.getWarehouseBO() == null) {
			throw new CrownException("Select Warehouse");
		}
		if (bo.getItemBO() == null) {
			throw new CrownException("Select Item");
		}
		if (bo.getItemQty() == 0 && bo.getUomQty() == 0) {
			throw new CrownException("Enter Item Qty or UOM Qty");
		}
	}

	public void validateStockUpdate(WarehouseStockBO bo) throws CrownException {
		if (bo.getWarehouseBO() == null) {
			throw new CrownException("Select Warehouse");
		}
		if (bo.getItemBO() == null) {
			throw new CrownException("Select Item");
		}
		if (bo.getUpdItemQty() == 0 && bo.getUpdUomQty() == 0) {
			throw new CrownException("Enter Item Qty or UOM Qty");
		}
	}
}
