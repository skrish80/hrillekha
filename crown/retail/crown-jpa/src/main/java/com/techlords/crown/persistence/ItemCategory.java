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
 * The persistent class for the item_category database table.
 * 
 */
@Entity
@Table(name = "item_category")
public class ItemCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ITEM_CATEGORY_CATEGORYID_GENERATOR", sequenceName = "ITEM_CATEGORY_CATEGORY_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_CATEGORY_CATEGORYID_GENERATOR")
	@Column(name = "category_id", unique = true, nullable = false)
	private Integer categoryId;

	@Column(name = "category_name", nullable = false, length = 10)
	private String categoryName;
	
	@Column(name = "category_code", nullable = false, length = 10)
	private String categoryCode;

	@Column(name = "description", length = 50)
	private String description;

	// bi-directional many-to-one association to Item
	@OneToMany(mappedBy = "itemCategoryBean")
	private Set<Item> items;

	// bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name = "status", nullable = false)
	private Status statusBean;

	public ItemCategory() {
	}

	public Integer getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	/**
	 * @return the categoryCode
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	/**
	 * @param categoryCode the categoryCode to set
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

}