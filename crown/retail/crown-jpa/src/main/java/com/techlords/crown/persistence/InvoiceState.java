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
 * The persistent class for the invoice_state database table.
 * 
 */
@Entity
@Table(name = "invoice_state")
public class InvoiceState implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "INVOICE_STATE_STATEID_GENERATOR", sequenceName = "INVOICE_STATE_STATE_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICE_STATE_STATEID_GENERATOR")
	@Column(name = "state_id", unique = true, nullable = false)
	private Integer stateId;

	@Column(length = 64)
	private String description;

	@Column(nullable = false, length = 16)
	private String state;

	// bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy = "invoiceStateBean")
	private Set<Invoice> invoices;

	public InvoiceState() {
	}

	public Integer getStateId() {
		return this.stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
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