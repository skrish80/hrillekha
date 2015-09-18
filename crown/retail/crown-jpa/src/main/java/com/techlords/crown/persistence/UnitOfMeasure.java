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
 * The persistent class for the unit_of_measure database table.
 * 
 */
@Entity
@Table(name = "unit_of_measure")
public class UnitOfMeasure implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "UNIT_OF_MEASURE_UOMID_GENERATOR", sequenceName = "UNIT_OF_MEASURE_UOM_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UNIT_OF_MEASURE_UOMID_GENERATOR")
	@Column(name = "uom_id", unique = true, nullable = false)
	private Integer uomId;

	@Column(length = 50)
	private String description;

	@Column(name = "uom_name", nullable = false, length = 25)
	private String uomName;

	// bi-directional many-to-one association to Item
	@OneToMany(mappedBy = "unitOfMeasure")
	private Set<Item> items;

	public UnitOfMeasure() {
	}

	public Integer getUomId() {
		return this.uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUomName() {
		return this.uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public Set<Item> getItems() {
		return this.items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
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