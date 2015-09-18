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
 * The persistent class for the payment_status database table.
 * 
 */
@Entity
@Table(name = "payment_status")
public class PaymentStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PAYMENT_STATUS_PAYMENTSTATUSID_GENERATOR", sequenceName = "PAYMENT_STATUS_PAYMENT_STATUS_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_STATUS_PAYMENTSTATUSID_GENERATOR")
	@Column(name = "payment_status_id", unique = true, nullable = false)
	private Integer paymentStatusId;

	@Column(length = 25)
	private String description;

	@Column(name = "payment_status", nullable = false, length = 25)
	private String paymentStatus;

	// bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy = "paymentStatusBean")
	private Set<Invoice> invoices;

	public PaymentStatus() {
	}

	public Integer getPaymentStatusId() {
		return this.paymentStatusId;
	}

	public void setPaymentStatusId(Integer paymentStatusId) {
		this.paymentStatusId = paymentStatusId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPaymentStatus() {
		return this.paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
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