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
 * The persistent class for the invoice database table.
 * 
 */
@Entity
@Table(name = "invoice")
@NamedQueries({
		@NamedQuery(name = "Invoice.paymentPendingInvoices", query = "select I from Invoice I where I.invoiceStateBean.stateId <> :invState AND I.paymentStatusBean.paymentStatusId IN :paymentStatuses order by I.invoiceDate desc"),
		@NamedQuery(name = "Invoice.invoiceByStates", query = "select I from Invoice I where I.invoiceStateBean.stateId IN :invStates"),
		@NamedQuery(name = "Invoice.allInvoices", query = "select I from Invoice I order by I.invoiceDate desc"),
		@NamedQuery(name = "Invoice.countInvoices", query = "select COUNT(I) from Invoice I"),
})
public class Invoice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "INVOICE_INVOICEID_GENERATOR", sequenceName = "INVOICE_INVOICE_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICE_INVOICEID_GENERATOR")
	@Column(name = "invoice_id", unique = true, nullable = false)
	private Integer invoiceId;

	@Temporal(TemporalType.DATE)
	@Column(name = "delivered_date")
	private Date deliveredDate;
	
	@Column(name = "invoice_type", nullable = false, length = 2)
	private String invoiceType;

	@Column(name = "discount_amount")
	private double discountAmount;

	@Column(name = "invoice_amount")
	private double invoiceAmount;

	@Temporal(TemporalType.DATE)
	@Column(name = "invoice_date")
	private Date invoiceDate;

	@Column(name = "invoice_number", nullable = false, length = 25)
	private String invoiceNumber;

	@Column(name="remarks", length = 2147483647)
	private String remarks;

	@Column(name="terms_conditions", length = 2147483647)
	private String termsConditions;
	
	@Column(name="other_price")
	private String otherPrice;

	@Column(name = "other_price_amount")
	private double otherPriceAmount;
	
	@Column(name = "return_amount")
	private double returnAmount;

	@Temporal(TemporalType.DATE)
	@Column(name = "return_date")
	private Date returnDate;

	// bi-directional many-to-one association to AgentCommission
	@OneToMany(mappedBy = "invoice")
	private Set<AgentCommission> agentCommissions;

	// bi-directional many-to-one association to CreditNote
	@OneToMany(mappedBy = "invoice")
	private Set<CreditNote> creditNotes;

	// bi-directional many-to-one association to Agent
	@ManyToOne
	@JoinColumn(name = "agent_id")
	private Agent agent;
	
	// bi-directional many-to-one association to Company
	@ManyToOne
	@JoinColumn(name = "company")
	private Company company;
	
	// bi-directional many-to-one association to CrownUser
	@ManyToOne
	@JoinColumn(name = "created_by", nullable = false)
	private CrownUser createdBy;

	// bi-directional many-to-one association to CrownEntity
	@ManyToOne
	@JoinColumn(name = "entity_id", nullable = false)
	private CrownEntity crownEntity;

	// bi-directional many-to-one association to Currency
	@ManyToOne
	@JoinColumn(name = "currency")
	private Currency currencyBean;

	// bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	// bi-directional many-to-one association to DiscountType
	@ManyToOne
	@JoinColumn(name = "discount_type")
	private DiscountType discountTypeBean;

	// bi-directional many-to-one association to InvoiceState
	@ManyToOne
	@JoinColumn(name = "invoice_state")
	private InvoiceState invoiceStateBean;

	// bi-directional many-to-one association to PaymentStatus
	@ManyToOne
	@JoinColumn(name = "payment_status", nullable = false)
	private PaymentStatus paymentStatusBean;

	// bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name = "status")
	private Status statusBean;

	// bi-directional many-to-one association to Warehouse
	@ManyToOne
	@JoinColumn(name = "delivery_warehouse", nullable = false)
	private Warehouse deliveryWarehouse;

	// bi-directional many-to-one association to InvoiceItem
	@OneToMany(mappedBy = "invoice")
	private Set<InvoiceItem> invoiceItems;

	// bi-directional many-to-one association to InvoicePayment
	@OneToMany(mappedBy = "invoice")
	private Set<InvoicePayment> invoicePayments;

	// bi-directional many-to-one association to InvoiceReturn
	@OneToMany(mappedBy = "invoice")
	private Set<InvoiceReturn> invoiceReturns;

	public Invoice() {
	}

	public Integer getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Date getDeliveredDate() {
		return this.deliveredDate;
	}

	public void setDeliveredDate(Date deliveredDate) {
		this.deliveredDate = deliveredDate;
	}

	public double getDiscountAmount() {
		return this.discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
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

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public double getReturnAmount() {
		return this.returnAmount;
	}

	public void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public Date getReturnDate() {
		return this.returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Set<AgentCommission> getAgentCommissions() {
		return this.agentCommissions;
	}

	public void setAgentCommissions(Set<AgentCommission> agentCommissions) {
		this.agentCommissions = agentCommissions;
	}

	public Set<CreditNote> getCreditNotes() {
		return this.creditNotes;
	}

	public void setCreditNotes(Set<CreditNote> creditNotes) {
		this.creditNotes = creditNotes;
	}

	public Agent getAgent() {
		return this.agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public CrownEntity getCrownEntity() {
		return this.crownEntity;
	}

	public void setCrownEntity(CrownEntity crownEntity) {
		this.crownEntity = crownEntity;
	}

	public Currency getCurrencyBean() {
		return this.currencyBean;
	}

	public void setCurrencyBean(Currency currencyBean) {
		this.currencyBean = currencyBean;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public DiscountType getDiscountTypeBean() {
		return this.discountTypeBean;
	}

	public void setDiscountTypeBean(DiscountType discountTypeBean) {
		this.discountTypeBean = discountTypeBean;
	}

	public InvoiceState getInvoiceStateBean() {
		return this.invoiceStateBean;
	}

	public void setInvoiceStateBean(InvoiceState invoiceStateBean) {
		this.invoiceStateBean = invoiceStateBean;
	}

	public PaymentStatus getPaymentStatusBean() {
		return this.paymentStatusBean;
	}

	public void setPaymentStatusBean(PaymentStatus paymentStatusBean) {
		this.paymentStatusBean = paymentStatusBean;
	}

	public Status getStatusBean() {
		return this.statusBean;
	}

	public void setStatusBean(Status statusBean) {
		this.statusBean = statusBean;
	}

	public Set<InvoiceItem> getInvoiceItems() {
		return this.invoiceItems;
	}

	public void setInvoiceItems(Set<InvoiceItem> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}

	public Set<InvoicePayment> getInvoicePayments() {
		return this.invoicePayments;
	}

	public void setInvoicePayments(Set<InvoicePayment> invoicePayments) {
		this.invoicePayments = invoicePayments;
	}

	public Warehouse getDeliveryWarehouse() {
		return deliveryWarehouse;
	}

	public void setDeliveryWarehouse(Warehouse deliveryWarehouse) {
		this.deliveryWarehouse = deliveryWarehouse;
	}

	public Set<InvoiceReturn> getInvoiceReturns() {
		return this.invoiceReturns;
	}

	public void setInvoiceReturns(Set<InvoiceReturn> invoiceReturns) {
		this.invoiceReturns = invoiceReturns;
	}

	public final String getInvoiceType() {
		return invoiceType;
	}

	public final void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public final String getTermsConditions() {
		return termsConditions;
	}

	public final void setTermsConditions(String termsConditions) {
		this.termsConditions = termsConditions;
	}

	public final String getOtherPrice() {
		return otherPrice;
	}

	public final void setOtherPrice(String otherPrice) {
		this.otherPrice = otherPrice;
	}

	public final double getOtherPriceAmount() {
		return otherPriceAmount;
	}

	public final void setOtherPriceAmount(double otherPriceAmount) {
		this.otherPriceAmount = otherPriceAmount;
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