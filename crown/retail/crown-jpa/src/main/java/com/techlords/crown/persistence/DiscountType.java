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
 * The persistent class for the discount_type database table.
 * 
 */
@Entity
@Table(name = "discount_type")
public class DiscountType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "DISCOUNT_TYPE_DISCOUNTTYPEID_GENERATOR", sequenceName = "DISCOUNT_TYPE_DISCOUNT_TYPE_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DISCOUNT_TYPE_DISCOUNTTYPEID_GENERATOR")
	@Column(name = "discount_type_id", unique = true, nullable = false)
	private Integer discountTypeId;

	@Column(length = 25)
	private String description;

	@Column(nullable = false, length = 10)
	private String discount;

	// bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy = "discountTypeBean")
	private Set<Invoice> invoices;

	public DiscountType() {
	}

	public Integer getDiscountTypeId() {
		return this.discountTypeId;
	}

	public void setDiscountTypeId(Integer discountTypeId) {
		this.discountTypeId = discountTypeId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDiscount() {
		return this.discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Set<Invoice> getInvoices() {
		return this.invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
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