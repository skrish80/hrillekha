package com.techlords.crown.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the stock_movement database table.
 * 
 */
@Entity
@Table(name = "stock_movement")
public class StockMovement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "STOCK_MOVEMENT_MOVEMENTID_GENERATOR", sequenceName = "STOCK_MOVEMENT_MOVEMENT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_MOVEMENT_MOVEMENTID_GENERATOR")
	@Column(name = "movement_id", unique = true, nullable = false)
	private Integer movementId;

	@Column(length = 2147483647)
	private String comments;

	@Column(name = "move_receipt_id", unique = true, nullable = false, length = 20)
	private String moveReceiptId;

	@Temporal(TemporalType.DATE)
	@Column(name = "moved_date")
	private Date movedDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "received_date")
	private Date receivedDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "returned_date")
	private Date returnedDate;

	// bi-directional many-to-one association to MoveStatus
	@ManyToOne
	@JoinColumn(name = "move_status")
	private MoveStatus moveStatusBean;

	// bi-directional many-to-one association to Warehouse
	@ManyToOne
	@JoinColumn(name = "from_warehouse")
	private Warehouse fromWarehouse;

	// bi-directional many-to-one association to Warehouse
	@ManyToOne
	@JoinColumn(name = "to_warehouse")
	private Warehouse toWarehouse;

	// bi-directional many-to-one association to StockMovementItem
	@OneToMany(mappedBy = "stockMovement")
	private Set<StockMovementItem> stockMovementItems;

	public StockMovement() {
	}

	public Integer getMovementId() {
		return this.movementId;
	}

	public void setMovementId(Integer movementId) {
		this.movementId = movementId;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getMoveReceiptId() {
		return this.moveReceiptId;
	}

	public void setMoveReceiptId(String moveReceiptId) {
		this.moveReceiptId = moveReceiptId;
	}

	public Date getMovedDate() {
		return this.movedDate;
	}

	public void setMovedDate(Date movedDate) {
		this.movedDate = movedDate;
	}

	public Date getReceivedDate() {
		return this.receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Date getReturnedDate() {
		return this.returnedDate;
	}

	public void setReturnedDate(Date returnedDate) {
		this.returnedDate = returnedDate;
	}

	public MoveStatus getMoveStatusBean() {
		return this.moveStatusBean;
	}

	public void setMoveStatusBean(MoveStatus moveStatusBean) {
		this.moveStatusBean = moveStatusBean;
	}

	public Set<StockMovementItem> getStockMovementItems() {
		return this.stockMovementItems;
	}

	public void setStockMovementItems(Set<StockMovementItem> stockMovementItems) {
		this.stockMovementItems = stockMovementItems;
	}

	public Warehouse getFromWarehouse() {
		return fromWarehouse;
	}

	public void setFromWarehouse(Warehouse fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}

	public Warehouse getToWarehouse() {
		return toWarehouse;
	}

	public void setToWarehouse(Warehouse toWarehouse) {
		this.toWarehouse = toWarehouse;
	}
	
	@Version
	@Column(name = "version", unique = true, nullable = false)
	private long version;

	public final long getVersion() {
		return version;
	}

	public final void setVersion(long version) {
		this.version = version;
	}

}