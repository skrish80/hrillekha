package com.techlords.crown.persistence;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the currency database table.
 * 
 */
@Entity
@Table(name = "currency")
@NamedQuery(name = "Currency.findByCode", query = "select C from Currency C where C.currencyCode=?1 order by C.currencyCode")
public class Currency implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CURRENCY_CURRENCYCODE_GENERATOR", sequenceName = "CURRENCY_CURRENCY_CODE_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CURRENCY_CURRENCYCODE_GENERATOR")
	@Column(name = "currency_code", unique = true, nullable = false, length = 3)
	private String currencyCode;

	@Column(length = 50)
	private String description;

	// bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy = "currencyBean")
	private Set<Invoice> invoices;

	// bi-directional many-to-one association to Item
	@OneToMany(mappedBy = "currencyBean")
	private Set<Item> items;

	public Currency() {
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Invoice> getInvoices() {
		return this.invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
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