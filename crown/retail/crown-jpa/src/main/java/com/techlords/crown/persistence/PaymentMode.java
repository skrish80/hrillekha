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
 * The persistent class for the payment_mode database table.
 * 
 */
@Entity
@Table(name = "payment_mode")
public class PaymentMode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PAYMENT_MODE_PAYMENTMODEID_GENERATOR", sequenceName = "PAYMENT_MODE_PAYMENT_MODE_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_MODE_PAYMENTMODEID_GENERATOR")
	@Column(name = "payment_mode_id", unique = true, nullable = false)
	private Integer paymentModeId;

	@Column(length = 25)
	private String description;

	@Column(name = "payment_mode", nullable = false, length = 25)
	private String paymentMode;

	// bi-directional many-to-one association to InvoicePayment
	@OneToMany(mappedBy = "paymentModeBean")
	private Set<InvoicePayment> invoicePayments;

	public PaymentMode() {
	}

	public Integer getPaymentModeId() {
		return this.paymentModeId;
	}

	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPaymentMode() {
		return this.paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Set<InvoicePayment> getInvoicePayments() {
		return this.invoicePayments;
	}

	public void setInvoicePayments(Set<InvoicePayment> invoicePayments) {
		this.invoicePayments = invoicePayments;
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