package com.techlords.crown.persistence;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the department database table.
 * 
 */
@Entity
@Table(name = "department")
@NamedNativeQuery(name = "findActiveDepartments", query = "select d.* from department d where d.status = ?", resultClass = Department.class)
public class Department implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "DEPARTMENT_DEPARTMENTID_GENERATOR", sequenceName = "DEPARTMENT_DEPARTMENT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTMENT_DEPARTMENTID_GENERATOR")
	@Column(name = "department_id", unique = true, nullable = false)
	private Integer departmentId;

	@Column(name = "department_name", nullable = false, length = 10)
	private String departmentName;

	@Column(name = "description", length = 25)
	private String description;

	// bi-directional many-to-one association to CrownUser
	@OneToMany(mappedBy = "departmentBean")
	private Set<CrownUser> crownUsers;

	public Department() {
	}

	public Integer getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return this.departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<CrownUser> getCrownUsers() {
		return this.crownUsers;
	}

	public void setCrownUsers(Set<CrownUser> crownUsers) {
		this.crownUsers = crownUsers;
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