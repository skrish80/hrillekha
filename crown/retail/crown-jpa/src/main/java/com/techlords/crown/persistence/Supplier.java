package com.techlords.crown.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Set;

/**
 * The persistent class for the supplier database table.
 * 
 */
@Entity
@Table(name = "supplier")
@NamedQueries({
	@NamedQuery(name = "Supplier.findActiveSuppliers", query = "select S from Supplier S where S.statusBean.statusId=1 order by S.supplierName") })
public class Supplier implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SUPPLIER_SUPPLIERID_GENERATOR", sequenceName = "SUPPLIER_SUPPLIER_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPPLIER_SUPPLIERID_GENERATOR")
	@Column(name = "supplier_id")
	private Integer supplierId;

	@Column(name = "address", length = 2147483647)
	private String address;

	@Column(name = "phone", length = 50)
	private String phone;

	@Column(name = "remarks", length = 2147483647)
	private String remarks;

	@Column(name = "tin", length = 25)
	private String tin;
	@Column(name = "poc", length = 50)
	private String poc;

	// bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name = "status")
	private Status statusBean;

	@Column(name = "supplier_code", unique=true, nullable=false)
	private String supplierCode;

	@Column(name = "supplier_name", unique=true, nullable=false)
	private String supplierName;

	// bi-directional many-to-one association to PurchaseInvoice
	@OneToMany(mappedBy = "supplier")
	private Set<PurchaseInvoice> purchaseInvoices;

	public Supplier() {
	}

	public Integer getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Set<PurchaseInvoice> getPurchaseInvoices() {
		return this.purchaseInvoices;
	}

	public void setPurchaseInvoices(Set<PurchaseInvoice> purchaseInvoices) {
		this.purchaseInvoices = purchaseInvoices;
	}

	public final Status getStatusBean() {
		return statusBean;
	}

	public final void setStatusBean(Status statusBean) {
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
	 * @return the tin
	 */
	public String getTin() {
		return tin;
	}

	/**
	 * @param tin the tin to set
	 */
	public void setTin(String tin) {
		this.tin = tin;
	}

	/**
	 * @return the poc
	 */
	public String getPoc() {
		return poc;
	}

	/**
	 * @param poc the poc to set
	 */
	public void setPoc(String poc) {
		this.poc = poc;
	}

}