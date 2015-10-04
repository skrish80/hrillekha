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
 * The persistent class for the purchase_invoice database table.
 * 
 */
@Entity
@Table(name = "purchase_invoice")
@NamedQueries({
		@NamedQuery(name = "PurchaseInvoice.allInvoices", query = "select I from PurchaseInvoice I order by I.invoiceDate desc"),
		@NamedQuery(name = "PurchaseInvoice.countInvoices", query = "select COUNT(I) from PurchaseInvoice I"), })
public class PurchaseInvoice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PURCHASE_INVOICE_INVOICEID_GENERATOR", sequenceName = "PURCHASE_INVOICE_INVOICE_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PURCHASE_INVOICE_INVOICEID_GENERATOR")
	@Column(name = "invoice_id")
	private Integer invoiceId;

	@Column(name = "invoice_amount")
	private double invoiceAmount;

	@Temporal(TemporalType.DATE)
	@Column(name = "invoice_date")
	private Date invoiceDate;

	@Column(name = "invoice_number")
	private String invoiceNumber;

	@Column(name = "invoice_state")
	private String invoiceState;

	@Temporal(TemporalType.DATE)
	@Column(name = "received_date")
	private Date receivedDate;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "terms_conditions")
	private String termsConditions;

	@Column(name = "goods_receipt_number")
	private String goodsReceiptNumber;

	// bi-directional many-to-one association to Supplier
	@ManyToOne
	@JoinColumn(name = "supplier_id")
	private Supplier supplier;

	// bi-directional many-to-one association to Company
	@ManyToOne
	@JoinColumn(name = "company")
	private Company company;

	// bi-directional many-to-one association to CrownUser
	@ManyToOne
	@JoinColumn(name = "created_by", nullable = false)
	private CrownUser createdBy;

	// bi-directional many-to-one association to CrownUser
	@ManyToOne
	@JoinColumn(name = "received_by", nullable = false)
	private CrownUser receivedBy;

	// bi-directional many-to-one association to Currency
	@ManyToOne
	@JoinColumn(name = "currency")
	private Currency currencyBean;

	// bi-directional many-to-one association to PurchaseInvoiceItem
	@OneToMany(mappedBy = "purchaseInvoice")
	private Set<PurchaseInvoiceItem> purchaseInvoiceItems;

	// bi-directional many-to-one association to InvoicePayment
	@OneToMany(mappedBy = "invoice")
	private Set<PurchaseInvoicePayment> invoicePayments;

	// bi-directional many-to-one association to PaymentStatus
	@ManyToOne
	@JoinColumn(name = "payment_status", nullable = false)
	private PaymentStatus paymentStatusBean;

	public PurchaseInvoice() {
	}

	public Integer getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public double getInvoiceAmount() {
		return this.invoiceAmount;
	}

	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceState() {
		return this.invoiceState;
	}

	public void setInvoiceState(String invoiceState) {
		this.invoiceState = invoiceState;
	}

	public Date getReceivedDate() {
		return this.receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTermsConditions() {
		return this.termsConditions;
	}

	public void setTermsConditions(String termsConditions) {
		this.termsConditions = termsConditions;
	}

	public Supplier getSupplier() {
		return this.supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Set<PurchaseInvoiceItem> getPurchaseInvoiceItems() {
		return this.purchaseInvoiceItems;
	}

	public void setPurchaseInvoiceItems(Set<PurchaseInvoiceItem> purchaseInvoiceItems) {
		this.purchaseInvoiceItems = purchaseInvoiceItems;
	}

	public final Company getCompany() {
		return company;
	}

	public final void setCompany(Company company) {
		this.company = company;
	}

	public final CrownUser getCreatedBy() {
		return createdBy;
	}

	public final void setCreatedBy(CrownUser createdBy) {
		this.createdBy = createdBy;
	}

	public final CrownUser getReceivedBy() {
		return receivedBy;
	}

	public final void setReceivedBy(CrownUser receivedBy) {
		this.receivedBy = receivedBy;
	}

	public final Currency getCurrencyBean() {
		return currencyBean;
	}

	public final void setCurrencyBean(Currency currencyBean) {
		this.currencyBean = currencyBean;
	}

	public final String getGoodsReceiptNumber() {
		return goodsReceiptNumber;
	}

	public final void setGoodsReceiptNumber(String goodsReceiptNumber) {
		this.goodsReceiptNumber = goodsReceiptNumber;
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
	 * @return the invoicePayments
	 */
	public Set<PurchaseInvoicePayment> getInvoicePayments() {
		return invoicePayments;
	}

	/**
	 * @param invoicePayments
	 *            the invoicePayments to set
	 */
	public void setInvoicePayments(Set<PurchaseInvoicePayment> invoicePayments) {
		this.invoicePayments = invoicePayments;
	}

	/**
	 * @return the paymentStatusBean
	 */
	public PaymentStatus getPaymentStatusBean() {
		return paymentStatusBean;
	}

	/**
	 * @param paymentStatusBean the paymentStatusBean to set
	 */
	public void setPaymentStatusBean(PaymentStatus paymentStatusBean) {
		this.paymentStatusBean = paymentStatusBean;
	}

}