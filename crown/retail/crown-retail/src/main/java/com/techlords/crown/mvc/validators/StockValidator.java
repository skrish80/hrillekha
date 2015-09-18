package com.techlords.crown.mvc.validators;

import java.util.List;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.PurchaseInvoiceBO;
import com.techlords.crown.business.model.PurchaseInvoiceItemBO;
import com.techlords.crown.business.model.StockAllocationBO;
import com.techlords.crown.business.model.StockMovementBO;
import com.techlords.crown.business.model.StockMovementItemBO;
import com.techlords.crown.business.model.StolenStockBO;
import com.techlords.crown.business.model.TotalStockBO;
import com.techlords.crown.business.model.WarehouseStockBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.MoveStatusBO;
import com.techlords.crown.service.WarehouseService;

public class StockValidator {

	private boolean isDuplicated(StockMovementItemBO bo,
			List<StockMovementItemBO> items) {
		int count = 0;
		for (StockMovementItemBO itemBO : items) {
			if (bo.getItem() == itemBO.getItem()) {
				count++;
			}
		}
		return (count > 1);
	}
	
	public void validateStockOnMove(StockMovementBO stockMovementBO,
			List<WarehouseStockBO> warehouseStocks) throws CrownException {
		if(stockMovementBO.getFromWarehouse() == stockMovementBO.getToWarehouse()) {
			throw new CrownException("\"From Warehouse\" cannot be the same as \"To Warehouse\"");
		}
		if (warehouseStocks.isEmpty() && stockMovementBO.isNew()) {
			throw new CrownException(
					"Did you ever check availability of items in warehouse?");
		}
		final List<StockMovementItemBO> items = stockMovementBO
				.getStockMovementItems();
		MoveStatusBO mvStatus = stockMovementBO.getMoveStatusBO();

		if (items.size() < 1) {
			throw new CrownException("No Items for move");
		}
		for (StockMovementItemBO itemBO : items) {
			if (itemBO.getItem() < 1) {
				throw new CrownException(
						"Item not selected for movement; Please try again...");
			}
			if (itemBO.getAllocationType() < 1) {
				throw new CrownException(
						"Move Type (UOM/ITEM) not selected for movement; Please try again...");
			}
			if (itemBO.getMovedQty() == null || itemBO.getMovedQty() < 1) {
				throw new CrownException(
						"Move Quantity not selected for movement; Please try again...");
			}
			if (isDuplicated(itemBO, items)) {
				throw new CrownException("Movement Item \""
						+ itemBO.getItemBO().getItemName()
						+ "\" duplicated. Try again");
			}
			switch (mvStatus) {
			case ACCEPTED:
				if (!itemBO.getMovedQty().equals(itemBO.getReceivedQty())) {
					throw new CrownException(
							"For Accepted State, Moved Qty should be equal to Received Qty for "
									+ itemBO.getItemBO().getItemName());
				}
				break;
			case PARTIAL_ACCEPT:
				if (itemBO.getMovedQty() <= itemBO.getReceivedQty()) {
					throw new CrownException(
							"For Partial Acceptance, Received Qty should be less than Moved Qty for "
									+ itemBO.getItemBO().getItemName());
				}
				break;
			case RETURNED:
				if (itemBO.getMovedQty() != (itemBO.getReceivedQty() + itemBO
						.getReturnedQty())) {
					throw new CrownException(
							"For Return, Moved Qty should be equal to Return Qty + Received Qty for "
									+ itemBO.getItemBO().getItemName());
				}
				break;
			default:
				break;

			}
			for (WarehouseStockBO bo : warehouseStocks) {
				if (bo.getItemID() == itemBO.getItem()) {
					int qtyToCompare = (itemBO.getAllocationType() == AllocationTypeBO.ITEM
							.getAllocationTypeID()) ? bo.getItemQty() : bo
							.getUomQty();
					if (itemBO.getMovedQty() > qtyToCompare) {
						throw new CrownException(
								"Moved Qty cannot be greater than Available Qty for \""
										+ bo.getItemBO().getItemName()
										+ "\". Try again");
					}
				}
			}
		}
	}

	public void validateStockAllocation(StockAllocationBO bo,
			TotalStockBO totalStock) throws CrownException {
		if (totalStock == null) {
			throw new CrownException(
					"Did you ever check availability of item(s)");
		}
		final AllocationTypeBO type = AllocationTypeBO.valueOf(bo
				.getAllocationType());
		if (type == AllocationTypeBO.ITEM) {
			if (bo.getAllocatedQty() > totalStock.getItemQty()) {
				throw new CrownException(
						"Allocated Item Qty cannot be greater than Available ITEM Qty");
			}
		} else {
			if (bo.getAllocatedQty() > totalStock.getUomQty()) {
				throw new CrownException(
						"Allocated UOM Qty cannot be greater than Available UOM Qty");
			}
		}
	}

	public void validateStolenStock(StolenStockBO bo) throws CrownException {
		if (bo.getStolenItemQty() <= 0 && bo.getStolenUOMQty() <= 0) {
			throw new CrownException("Enter either Item Qty or UOM Qty");
		}
		final WarehouseService service = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);
		List<WarehouseStockBO> stocks = service.findWarehouseStock(
				bo.getWarehouse(), bo.getItem());
		boolean stockExists = false;
		for (WarehouseStockBO stock : stocks) {
			if (bo.getStolenUOMQty() > stock.getUomQty()) {
				throw new CrownException(
						"You cannot add Stolen UOM Qty more than available for \""
								+ stock.getItemName() + "\".");
			}
			if (bo.getStolenItemQty() > stock.getItemQty()) {
				throw new CrownException(
						"You cannot add Stolen Item Qty more than available for \""
								+ stock.getItemName() + "\".");
			}
			stockExists = true;
		}
		if (!stockExists) {
			throw new CrownException(
					"You cannot add Stolen Item Qty that doesn't exist in Warehouse.");
		}
	}

	public void validateItemCreation(ItemBO bo) throws CrownException {
		if (bo.getPiecesPerUOM() < 1) {
			throw new CrownException("Enter Pieces per UOM");
		}
		if(bo.getVat() > 100.0) {
			throw new CrownException("VAT cannot exceed 100%");
		}
		if (bo.getItemPrice() <= 0.0 && bo.getUomPrice() <= 0.0) {
			throw new CrownException("Enter UOM Price");
		}
	}
	
	public void validatePurchaseInvoice(PurchaseInvoiceBO bo) throws CrownException {
		final List<PurchaseInvoiceItemBO> items = bo.getInvoiceItems();
		if(items.isEmpty()) {
			throw new CrownException("Add Items to Purchase Invoice");
		}
		for(PurchaseInvoiceItemBO item : items) {
			if(item.getItemQty() < 1) {
				throw new CrownException("Enter Qty for " +item.getItemBO().getItemName());
			}
			if(item.getPrice() <= 0.0 ) {
				throw new CrownException("Enter Item Price for " +item.getItemBO().getItemName());
			}
			if(isDuplicated(item, items)) {
				throw new CrownException("Item " +item.getItemBO().getItemName()+ " duplicated");
			}
		}
	}
	
	public void validatePurchaseInvoiceOnReceive(PurchaseInvoiceBO bo) throws CrownException {
		final List<PurchaseInvoiceItemBO> items = bo.getInvoiceItems();
		if(items.isEmpty()) {
			throw new CrownException("Add Items to Purchase Invoice");
		}
		for(PurchaseInvoiceItemBO item : items) {
			if(item.getItemQty() < item.getReceivedQty()) {
				throw new CrownException("Received Qty cannot be greater than Item Qty for " +item.getItemBO().getItemName());
			}
			if(item.getPrice() <= 0.0 ) {
				throw new CrownException("Enter Item Price for " +item.getItemBO().getItemName());
			}
			if(isDuplicated(item, items)) {
				throw new CrownException("Item " +item.getItemBO().getItemName()+ " duplicated");
			}
		}
	}
	
	private boolean isDuplicated(PurchaseInvoiceItemBO bo,
			List<PurchaseInvoiceItemBO> items) {
		int count = 0;
		for (PurchaseInvoiceItemBO itemBO : items) {
			if (bo.getItem() == itemBO.getItem()) {
				count++;
			}
		}
		return (count > 1);
	}
}
