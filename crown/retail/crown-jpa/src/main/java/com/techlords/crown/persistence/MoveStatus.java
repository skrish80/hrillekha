package com.techlords.crown.persistence;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the move_status database table.
 * 
 */
@Entity
@Table(name = "move_status")
public class MoveStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "MOVE_STATUS_MOVESTATUSID_GENERATOR", sequenceName = "MOVE_STATUS_MOVE_STATUS_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVE_STATUS_MOVESTATUSID_GENERATOR")
	@Column(name = "move_status_id", unique = true, nullable = false)
	private Integer moveStatusId;

	@Column(length = 50)
	private String description;

	@Column(name = "move_status", nullable = false, length = 20)
	private String moveStatus;

	// bi-directional many-to-one association to StockMovement
	@OneToMany(mappedBy = "moveStatusBean")
	private Set<StockMovement> stockMovements;

	public MoveStatus() {
	}

	public Integer getMoveStatusId() {
		return this.moveStatusId;
	}

	public void setMoveStatusId(Integer moveStatusId) {
		this.moveStatusId = moveStatusId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMoveStatus() {
		return this.moveStatus;
	}

	public void setMoveStatus(String moveStatus) {
		this.moveStatus = moveStatus;
	}

	public Set<StockMovement> getStockMovements() {
		return this.stockMovements;
	}

	public void setStockMovements(Set<StockMovement> stockMovements) {
		this.stockMovements = stockMovements;
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