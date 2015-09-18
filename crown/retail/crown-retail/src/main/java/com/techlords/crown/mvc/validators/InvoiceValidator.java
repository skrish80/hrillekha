package com.techlords.crown.mvc.validators;

import java.util.ArrayList;
import java.util.List;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CreditNoteBO;
import com.techlords.crown.business.model.InvoiceBO;
import com.techlords.crown.business.model.InvoiceItemBO;
import com.techlords.crown.business.model.InvoicePaymentBO;
import com.techlords.crown.business.model.InvoiceReturnBO;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.TotalStockBO;
import com.techlords.crown.business.model.WarehouseStockBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.business.model.enums.PaymentModeBO;
import com.techlords.crown.business.model.enums.PaymentStatusBO;
import com.techlords.crown.service.InvoiceService;

public class InvoiceValidator {

	public void validateInvoiceOnDeliver(InvoiceBO invoiceBO,
			List<WarehouseStockBO> warehouseStocks) throws CrownException {
		if (invoiceBO.getDeliveryWarehouse() <= 0) {
			throw new CrownException("Select a Delivery Warehouse");
		}
		if (warehouseStocks.isEmpty()) {
			throw new CrownException(
					"Did you ever check availability of items in warehouse?");
		}
		final boolean isFullDelivery = (invoiceBO.getInvoiceState() == InvoiceStateBO.DELIVERED
				.getStateID());
		final List<InvoiceItemBO> undeliverableItems = new ArrayList<InvoiceItemBO>();
		boolean noItemsDelivered = true;
		for (InvoiceItemBO itemBO : invoiceBO.getInvoiceItems()) {
			if (itemBO.getDeliveredQty() == 0) {
				continue; // don't deliver any item
			}
			noItemsDelivered = false;
			int totalDel = itemBO.getDeliveredQty()
					+ itemBO.getAlreadyDeliveredQty();
			// getTotalDeliveredQty(itemBO, alreadyDeliveredItems);
			if (totalDel > itemBO.getItemQty()) {
				throw new CrownException(
						"You cannot deliver more than the invoice item Qty for \""
								+ itemBO.getItemBO().getItemName() + "\".");
			}
			if (isFullDelivery && totalDel != itemBO.getItemQty()) {
				throw new CrownException(
						"Full Delivery shall have delivered qty equal to item qty. Select Partial Delivery");
			}
			if (!isFullDelivery && totalDel == itemBO.getItemQty()) {
				throw new CrownException(
						"Item Qty equal to delivered qty. This is a Full Delivery");
			}
			boolean itemExists = false;
			for (WarehouseStockBO bo : warehouseStocks) {
				if (bo.getItemID() == itemBO.getItem()) {
					itemExists = true;
					int qtyToCompare = (itemBO.getAllocationType() == AllocationTypeBO.ITEM
							.getAllocationTypeID()) ? bo.getItemQty() : bo
							.getUomQty();
					if (itemBO.getDeliveredQty() > qtyToCompare) {
						throw new CrownException(
								"You cannot deliver more than Available Qty for \""
										+ itemBO.getItemBO().getItemName()
										+ "\". Try again");
					}
				}
			}
			// Remove Item from delivery
			if (!itemExists) {
				if (itemBO.getDeliveredQty() > 0) {
					throw new CrownException(
							"\""
									+ itemBO.getItemBO().getItemName()
									+ "\" does not exist in Delivery Warehouse. Try again");
				}
				undeliverableItems.add(itemBO);
			}
		}
		if (noItemsDelivered) {
			throw new CrownException("Deliver at least one item");
		}
		invoiceBO.getInvoiceItems().removeAll(undeliverableItems);
	}

	@SuppressWarnings("unused")
	private int getInvoiceReturnQty(int item, int retType,
			List<InvoiceReturnBO> alreadyReturned) {
		int qty = 0;
		for (final InvoiceReturnBO bo : alreadyReturned) {
			if (bo.getItem() == item && bo.getReturnType() == retType) {
				qty += bo.getReturnQty();
			}
		}
		return qty;
	}

	/**
	 * Always returns the qty in items (not UOM)
	 * 
	 * @param itemID
	 * @param alreadyReturned
	 * @return
	 */
	private int getAlreadyReturnedQty(int itemID,
			List<InvoiceReturnBO> alreadyReturned) {
		int qty = 0;
		for (final InvoiceReturnBO bo : alreadyReturned) {
			if (bo.getItem() == itemID) {
				if (bo.getReturnType() == AllocationTypeBO.UOM
						.getAllocationTypeID()) {
					ItemBO itm = bo.getItemBO();
					qty += (bo.getReturnQty() * itm.getPiecesPerUOM());
				} else {
					qty += bo.getReturnQty();
				}
			}
		}
		return qty;
	}

	public void validateInvoiceOnReturn(InvoiceBO invoiceBO,
			List<InvoiceReturnBO> alreadyReturnedItems) throws CrownException {
		final List<InvoiceReturnBO> returns = invoiceBO.getInvoiceReturns();
		if (returns.isEmpty()) {
			throw new CrownException("No Returns added");
		}
		boolean noItemsReturned = true;
		final List<InvoiceItemBO> items = invoiceBO.getInvoiceItems();
		for (InvoiceReturnBO bo : returns) {
			if (bo.getReturnQty() == 0) {
				continue; // don't deliver any item
			}
			noItemsReturned = false;
			for (InvoiceItemBO it : items) {
				if (bo.getItem() == it.getItem()) {

					int qty = bo.getReturnQty();
					int qtyDelivered = it.getAlreadyDeliveredQty();

					if (bo.getReturnType() == AllocationTypeBO.UOM
							.getAllocationTypeID()) {
						qty = (it.getItemBO().getPiecesPerUOM() * bo
								.getReturnQty());
					}
					if (it.getAllocationType() == AllocationTypeBO.UOM
							.getAllocationTypeID()) {
						qtyDelivered = (it.getItemBO().getPiecesPerUOM() * it
								.getAlreadyDeliveredQty());
					}

					qty += getAlreadyReturnedQty(bo.getItem(),
							alreadyReturnedItems);

					if (qty > qtyDelivered) {
						throw new CrownException(
								"Return Qty cannot be greater than Delivered Qty for "
										+ it.getItemBO().getItemName());
					}
				}
			}
		}
		if (noItemsReturned) {
			throw new CrownException("Return at least one item");
		}
	}

	public void validateInvoiceOnCreate(InvoiceBO invoiceBO,
			List<TotalStockBO> totalStocks) throws CrownException {
		if (totalStocks.isEmpty()) {
			throw new CrownException(
					"Did you ever check availability of items?");
		}
		if (invoiceBO.getPaymentStatus() == PaymentStatusBO.FULL_PAYMENT
				.getStatusID()
				&& invoiceBO.getTotalPaidAmount() < invoiceBO
						.getFinalInvoiceAmount()) {
			throw new CrownException("Please check the total paid amount");
		}
		if ((invoiceBO.getDiscountType() > 0)
				&& (invoiceBO.getDiscountAmount() <= 0)) {
			throw new CrownException("Please enter the Discount Values");
		}
		final List<InvoiceItemBO> items = invoiceBO.getInvoiceItems();

		if (totalStocks.size() < items.size()) {
			throw new CrownException("Either Items duplicated or Unavailable");
		}

		for (InvoiceItemBO itemBO : items) {
			if (itemBO.getItem() < 1) {
				throw new CrownException(
						"Item not selected for movement; Please try again...");
			}
			if (itemBO.getAllocationType() < 1) {
				throw new CrownException(
						"Allocation Type (UOM/ITEM) not selected for Invoice Item; Please try again...");
			}
			if (itemBO.getItemQty() == null || itemBO.getItemQty() < 1) {
				throw new CrownException(
						"Item Quantity not selected for invoice; Please try again...");
			}
			if (isDuplicated(itemBO, items)) {
				throw new CrownException("Invoice Item \""
						+ itemBO.getItemBO().getItemName()
						+ "\" duplicated with same allocation type. Try again");
			}
			for (TotalStockBO bo : totalStocks) {
				if (bo.getItemID() == itemBO.getItem()) {
					Long qtyToCompare = (itemBO.getAllocationType() == AllocationTypeBO.ITEM
							.getAllocationTypeID()) ? bo.getItemQty() : bo
							.getUomQty();
					if (itemBO.getItemQty() > qtyToCompare) {
						throw new CrownException(
								"Invoice Qty cannot be greater than Available Qty for \""
										+ bo.getItemBO().getItemName()
										+ "\". Try again");
					}
				}
			}
		}
		validateInvoicePayment(0, invoiceBO);
	}

	private boolean isDuplicated(InvoiceItemBO bo, List<InvoiceItemBO> items) {
		int count = 0;
		for (InvoiceItemBO itemBO : items) {
			if (bo.getItem() == itemBO.getItem()
					&& bo.getAllocationType() == itemBO.getAllocationType()) {
				count++;
			}
		}

		return (count > 1);
	}

	public void validateInvoicePayment(double alreadyPaidAmount,
			InvoiceBO invoiceBO) throws CrownException {
		final List<InvoicePaymentBO> payments = invoiceBO.getInvoicePayments();

		invoiceBO.setPaymentStatusBO(PaymentStatusBO.valueOf(invoiceBO
				.getPaymentStatus()));
		switch (invoiceBO.getPaymentStatusBO()) {
		case FULL_PAYMENT:
			if (Math.round(invoiceBO.getTotalPaidAmount() + alreadyPaidAmount) < Math
					.round(invoiceBO.getFinalInvoiceAmount())) {
				throw new CrownException(
						"Payment Status cannot be \"FULL PAYMENT\"; Check the paid amount");
			}
			break;
		case PARTIAL_PAYMENT:
			if (Math.round(invoiceBO.getTotalPaidAmount() + alreadyPaidAmount) >= Math
					.round(invoiceBO.getFinalInvoiceAmount())) {
				throw new CrownException(
						"Payment Status cannot be \"PARTIAL PAYMENT\"; Check the paid amount");
			}
			break;
		case CREDIT_SALES:
			if (payments.size() > 0 || alreadyPaidAmount > 0) {
				throw new CrownException(
						"Payment Status cannot be \"CREDIT SALES\"; Check the paid amount");
			}
			break;
		default:
			throw new CrownException("Select Payment Status first");
		}

		if (payments.isEmpty()) {
			return;
		}
		for (InvoicePaymentBO pmt : payments) {
			if (pmt.getAmount() <= 0.0) {
				throw new CrownException(
						"Payment Amount cannot be zero. Check it");
			}
			if (pmt.getPaymentMode() != PaymentModeBO.CREDIT_NOTE.getModeID()) {
				continue;
			}
			final CreditNoteBO status = getCreditNoteStatus(pmt);
			if (status == null || status.isUtilized()) {
				throw new CrownException("Credit Note " + pmt.getDraftNumber()
						+ " Not Available");
			}
		}
	}

	public CreditNoteBO getCreditNoteStatus(InvoicePaymentBO pmt) {
		final String draftNumber = pmt.getDraftNumber();
		final InvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(InvoiceService.class);
		List<CreditNoteBO> bos = service.isCreditNoteUtilized(draftNumber);
		if (bos.size() != 1) {
			return null;
		}
		return bos.get(0);
	}

	public void validateCreditNoteCreation(InvoiceBO bo) {

	}
}
