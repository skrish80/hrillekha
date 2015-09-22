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
 * The persistent class for the crown_user database table.
 * 
 */
@Entity
@Table(name = "crown_user")
@NamedQueries({
		@NamedQuery(name = "CrownUser.findByUsername", query = "select CU from CrownUser CU where CU.username=?1"),
		@NamedQuery(name = "CrownUser.findActiveUsers", query = "select CU from CrownUser CU where CU.statusBean.statusId=1 and CU.userId <> 1 order by CU.username") })
//and CU.userId <> 1
public class CrownUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CROWN_USER_USERID_GENERATOR", sequenceName = "CROWN_USER_USER_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CROWN_USER_USERID_GENERATOR")
	@Column(name = "user_id", unique = true, nullable = false, columnDefinition="BIGSERIAL")
	private Integer userId;

	@Column(name = "address", length = 50)
	private String address;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth", nullable = false)
	private Date dateOfBirth;

	@Column(name = "email", nullable = false, length = 255)
	private String email;

	@Column(name = "employee_no", nullable = false, length = 10)
	private String employeeNo;

	@Column(name = "first_name", nullable = false, length = 32)
	private String firstName;

	@Temporal(TemporalType.DATE)
	@Column(name = "joining_date")
	private Date joiningDate;

	@Column(name = "last_name", nullable = false, length = 32)
	private String lastName;

	@Column(name = "mobile", length = 25)
	private String mobile;

	@Column(name = "password", nullable = false, length = 32)
	private String password;

	@Column(name = "username", nullable = false, length = 32)
	private String username;

	// bi-directional many-to-one association to CrownAudit
	@OneToMany(mappedBy = "crownUser")
	private Set<CrownAudit> crownAudits;

	// bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy = "createdBy")
	private Set<Invoice> invoices;

	// bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy = "createdBy")
	private Set<PurchaseInvoice> purchaseInvoices;

	// bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy = "receivedBy")
	private Set<PurchaseInvoice> receivedInvoices;

	// bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name = "role")
	private Role roleBean;

	// bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name = "status")
	private Status statusBean;

	// bi-directional many-to-one association to Warehouse
	@OneToMany(mappedBy = "crownUser")
	private Set<Warehouse> warehouses;

	// bi-directional many-to-one association to StolenStock
	@OneToMany(mappedBy = "createdBy")
	private Set<StolenStock> stolenStocks;

	public CrownUser() {
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmployeeNo() {
		return this.employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getJoiningDate() {
		return this.joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<CrownAudit> getCrownAudits() {
		return this.crownAudits;
	}

	public void setCrownAudits(Set<CrownAudit> crownAudits) {
		this.crownAudits = crownAudits;
	}

	public Role getRoleBean() {
		return this.roleBean;
	}

	public void setRoleBean(Role roleBean) {
		this.roleBean = roleBean;
	}

	public Status getStatusBean() {
		return this.statusBean;
	}

	public void setStatusBean(Status statusBean) {
		this.statusBean = statusBean;
	}

	public Set<Warehouse> getWarehouses() {
		return this.warehouses;
	}

	public void setWarehouses(Set<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}

	public Set<StolenStock> getStolenStocks() {
		return stolenStocks;
	}

	public void setStolenStocks(Set<StolenStock> stolenStocks) {
		this.stolenStocks = stolenStocks;
	}

	public final Set<Invoice> getInvoices() {
		return invoices;
	}

	public final void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
	}

	public final Set<PurchaseInvoice> getPurchaseInvoices() {
		return purchaseInvoices;
	}

	public final void setPurchaseInvoices(Set<PurchaseInvoice> purchaseInvoices) {
		this.purchaseInvoices = purchaseInvoices;
	}

	public final Set<PurchaseInvoice> getReceivedInvoices() {
		return receivedInvoices;
	}

	public final void setReceivedInvoices(Set<PurchaseInvoice> receivedInvoices) {
		this.receivedInvoices = receivedInvoices;
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