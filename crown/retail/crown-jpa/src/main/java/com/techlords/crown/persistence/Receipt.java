package com.techlords.crown.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the receipt database table.
 * 
 */
@Entity
@Table(name = "receipt")
@NamedQueries({
		@NamedQuery(name = "ReceiptPayment.findByCustomer", query = "select R from Receipt R where R.customerBean=?1 order by R.receiptDate desc"),
		@NamedQuery(name = "ReceiptPayment.findUnusedByCustomer", query = "select R from Receipt R where R.customerBean=?1 and R.usedState in ('PU', 'UU') order by R.receiptDate desc"),
		@NamedQuery(name = "Receipt.findByNumber", query = "select R from Receipt R where R.receiptNumber = ?1")

})
public class Receipt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "RECEIPT_RECEIPTID_GENERATOR", sequenceName = "RECEIPT_RECEIPT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECEIPT_RECEIPTID_GENERATOR")
	@Column(name = "receipt_id")
	private Integer receiptId;

	@Column(name = "amount")
	private double amount;

	@Column(name = "used_amount")
	private double usedAmount;

	@Column(name = "receipt_number", unique = true, nullable = false, length = 30)
	private String receiptNumber;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "used_state", nullable = false, length = 2)
	private String usedState;

	@Temporal(TemporalType.DATE)
	@Column(name = "receipt_date")
	private Date receiptDate;

	@Column(name = "issued")
	private boolean issued;

	// bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name = "customer")
	private Customer customerBean;

	@ManyToOne
	@JoinColumn(name = "issuing_company")
	private Company issuingCompany;

	// bi-directional many-to-one association to ReceiptPayment
	@OneToMany(mappedBy = "receipt")
	private Set<ReceiptPayment> receiptPayments;

	public Receipt() {
	}

	public Integer getReceiptId() {
		return this.receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Company getIssuingCompany() {
		return this.issuingCompany;
	}

	public void setIssuingCompany(Company issuingCompany) {
		this.issuingCompany = issuingCompany;
	}

	public Date getReceiptDate() {
		return this.receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Customer getCustomerBean() {
		return this.customerBean;
	}

	public void setCustomerBean(Customer customerBean) {
		this.customerBean = customerBean;
	}

	public Set<ReceiptPayment> getReceiptPayments() {
		return this.receiptPayments;
	}

	public void setReceiptPayments(Set<ReceiptPayment> receiptPayments) {
		this.receiptPayments = receiptPayments;
	}

	public final String getReceiptNumber() {
		return receiptNumber;
	}

	public final void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public final boolean isIssued() {
		return issued;
	}

	public final void setIssued(boolean issued) {
		this.issued = issued;
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

	public final double getUsedAmount() {
		return usedAmount;
	}

	public final void setUsedAmount(double usedAmount) {
		this.usedAmount = usedAmount;
	}

	public final String getUsedState() {
		return usedState;
	}

	public final void setUsedState(String usedState) {
		this.usedState = usedState;
	}

	public final String getRemarks() {
		return remarks;
	}

	public final void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}