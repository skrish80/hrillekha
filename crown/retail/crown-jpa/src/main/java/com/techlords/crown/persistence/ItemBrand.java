package com.techlords.crown.persistence;

import java.io.Serializable;
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
import javax.persistence.Version;


/**
 * The persistent class for the item_brand database table.
 * 
 */
@Entity
@Table(name = "item_brand")
public class ItemBrand implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ITEM_BRAND_BRANDID_GENERATOR", sequenceName = "ITEM_BRAND_BRAND_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_BRAND_BRANDID_GENERATOR")
	@Column(name = "brand_id", unique = true, nullable = false)
	private Integer brandId;

	@Column(name = "brand_name", nullable = false, length = 25)
	private String brandName;

	@Column(name = "description", length = 50)
	private String description;

	// bi-directional many-to-one association to Item
	@OneToMany(mappedBy = "itemBrandBean")
	private Set<Item> items;

	// bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name = "status", nullable = false)
	private Status statusBean;

	public ItemBrand() {
	}

	public Integer getBrandId() {
		return this.brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return this.brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Item> getItems() {
		return this.items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public Status getStatusBean() {
		return this.statusBean;
	}

	public void setStatusBean(Status statusBean) {
		this.statusBean = statusBean;
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