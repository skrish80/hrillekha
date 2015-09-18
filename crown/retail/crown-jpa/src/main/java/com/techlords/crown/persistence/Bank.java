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
 * The persistent class for the bank database table.
 * 
 */
@Entity
@Table(name = "bank")
@NamedQuery(name = "Bank.findByID", query = "select B from Bank B where B.bankId = ?1 order by B.bankName")
public class Bank implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "BANK_BANKID_GENERATOR", sequenceName = "BANK_BANK_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANK_BANKID_GENERATOR")
	@Column(name = "bank_id", unique = true, nullable = false)
	private Integer bankId;

	@Column(name = "bank_code", nullable = false, length = 10)
	private String bankCode;

	@Column(name = "bank_name", length = 50)
	private String bankName;

	@Column(name = "description", length = 250)
	private String description;

	// bi-directional many-to-one association to InvoicePayment
	@OneToMany(mappedBy = "bank")
	private Set<InvoicePayment> invoicePayments;

	public Bank() {
	}

	public Integer getBankId() {
		return this.bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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