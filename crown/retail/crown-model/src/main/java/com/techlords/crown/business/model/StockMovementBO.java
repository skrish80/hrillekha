package com.techlords.crown.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.techlords.crown.business.model.enums.MoveStatusBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class StockMovementBO extends AppModel {

	private String moveReceiptID;
	
	@Min(value = 1, message = "Please select \"From Warehouse\"")
	private int fromWarehouse;
	private WarehouseBO fromWarehouseBO;
	private Date moveDate;
	private Date returnDate;

	@Min(value = 1, message = "Please select \"To Warehouse\"")
	private int toWarehouse;
	private WarehouseBO toWarehouseBO;
	private Date receiveDate;

	@Min(value = 1, message = "Please select \"Move Status\"")
	private int moveStatus;
	private MoveStatusBO moveStatusBO;

	private String comments;

	@Size(min = 1, message = "Please add at least one item")
	private List<StockMovementItemBO> stockMovementItems = new ArrayList<StockMovementItemBO>();

	// new ArrayList<StockMovementItemBO>();

	public final int getFromWarehouse() {
		return fromWarehouse;
	}

	public final void setFromWarehouse(int fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}

	public final WarehouseBO getFromWarehouseBO() {
		return fromWarehouseBO;
	}

	public final void setFromWarehouseBO(WarehouseBO fromWarehouseBO) {
		this.fromWarehouseBO = fromWarehouseBO;
	}

	public final Date getMoveDate() {
		return moveDate;
	}

	public final void setMoveDate(Date moveDate) {
		this.moveDate = moveDate;
	}

	public final int getToWarehouse() {
		return toWarehouse;
	}

	public final void setToWarehouse(int toWarehouse) {
		this.toWarehouse = toWarehouse;
	}

	public final WarehouseBO getToWarehouseBO() {
		return toWarehouseBO;
	}

	public final void setToWarehouseBO(WarehouseBO toWarehouseBO) {
		this.toWarehouseBO = toWarehouseBO;
	}

	public final Date getReceiveDate() {
		return receiveDate;
	}

	public final void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public final int getMoveStatus() {
		return moveStatus;
	}

	public final void setMoveStatus(int moveStatus) {
		this.moveStatus = moveStatus;
	}

	public final MoveStatusBO getMoveStatusBO() {
		return moveStatusBO;
	}

	public final void setMoveStatusBO(MoveStatusBO moveStatusBO) {
		this.moveStatusBO = moveStatusBO;
	}

	public final String getComments() {
		return comments;
	}

	public final void setComments(String comments) {
		this.comments = comments;
	}

	public final void addStockMovementItem(StockMovementItemBO item) {
		stockMovementItems.add(item);
	}

	public final void removeStockMovementItem(StockMovementItemBO item) {
		stockMovementItems.remove(item);
	}

	public final List<StockMovementItemBO> getStockMovementItems() {
		return stockMovementItems;
	}

	public final void setStockMovementItems(
			List<StockMovementItemBO> stockMovementItems) {
		this.stockMovementItems = stockMovementItems;
	}

	public final String getMoveReceiptID() {
		return moveReceiptID;
	}

	public final void setMoveReceiptID(String moveReceiptID) {
		this.moveReceiptID = moveReceiptID;
	}

	public final Date getReturnDate() {
		return returnDate;
	}

	public final void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
}