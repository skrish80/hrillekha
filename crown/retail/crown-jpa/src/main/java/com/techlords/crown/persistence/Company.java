package com.techlords.crown.persistence;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the company database table.
 * 
 */
@Entity
@Table(name = "company")
@NamedQuery(name = "Company.findActiveCompanies", query = "select C from Company C where C.statusBean.statusId=1 order by C.companyName")
public class Company implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "COMPANY_COMPANYID_GENERATOR", sequenceName = "COMPANY_COMPANY_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_COMPANYID_GENERATOR")
	@Column(name = "company_id", unique = true, nullable = false, columnDefinition="BIGSERIAL")
	private Integer companyId;

	@Column(name = "address", length = 2147483647)
	private String address;

	@Column(name = "child_licence", length = 25)
	private String childLicence;

	@Column(name = "company_name", nullable = false, length = 50)
	private String companyName;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

	@Column(name = "fax", length = 25)
	private String fax;

	@Column(name = "incorporation", length = 25)
	private String incorporation;

	@Column(name = "parent_licence", length = 25)
	private String parentLicence;

	@Column(name = "phone", length = 25)
	private String phone;

	@Column(name = "poc", length = 25)
	private String poc;

	@Column(name = "sbn", length = 25)
	private String sbn;

	@Column(name = "tin", length = 25)
	private String tin;

	// bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy = "company")
	private Set<Invoice> invoices;

	// bi-directional many-to-one association to CompanyType
	@ManyToOne
	@JoinColumn(name = "company_type")
	private CompanyType companyTypeBean;

	// bi-directional many-to-one association to Location
	@ManyToOne
	@JoinColumn(name = "location", nullable = false)
	private Location locationBean;

	// bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name = "status")
	private Status statusBean;

	public Company() {
	}

	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getChildLicence() {
		return this.childLicence;
	}

	public void setChildLicence(String childLicence) {
		this.childLicence = childLicence;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getIncorporation() {
		return this.incorporation;
	}

	public void setIncorporation(String incorporation) {
		this.incorporation = incorporation;
	}

	public String getParentLicence() {
		return this.parentLicence;
	}

	public void setParentLicence(String parentLicence) {
		this.parentLicence = parentLicence;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPoc() {
		return this.poc;
	}

	public void setPoc(String poc) {
		this.poc = poc;
	}

	public String getSbn() {
		return this.sbn;
	}

	public void setSbn(String sbn) {
		this.sbn = sbn;
	}

	public String getTin() {
		return this.tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public CompanyType getCompanyTypeBean() {
		return this.companyTypeBean;
	}

	public void setCompanyTypeBean(CompanyType companyTypeBean) {
		this.companyTypeBean = companyTypeBean;
	}

	public Location getLocationBean() {
		return this.locationBean;
	}

	public void setLocationBean(Location locationBean) {
		this.locationBean = locationBean;
	}

	public Status getStatusBean() {
		return this.statusBean;
	}

	public void setStatusBean(Status statusBean) {
		this.statusBean = statusBean;
	}

	public final Set<Invoice> getInvoices() {
		return invoices;
	}

	public final void setInvoices(Set<Invoice> invoices) {
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