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
 * The persistent class for the company_type database table.
 * 
 */
@Entity
@Table(name = "company_type")
public class CompanyType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "COMPANY_TYPE_COMPANYTYPEID_GENERATOR", sequenceName = "COMPANY_TYPE_COMPANY_TYPE_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_TYPE_COMPANYTYPEID_GENERATOR")
	@Column(name = "company_type_id", unique = true, nullable = false)
	private Integer companyTypeId;

	@Column(name = "company_type", nullable = false, length = 25)
	private String companyType;

	@Column(length = 100)
	private String description;

	// bi-directional many-to-one association to Company
	@OneToMany(mappedBy = "companyTypeBean")
	private Set<Company> companies;

	public CompanyType() {
	}

	public Integer getCompanyTypeId() {
		return this.companyTypeId;
	}

	public void setCompanyTypeId(Integer companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	public String getCompanyType() {
		return this.companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Company> getCompanies() {
		return this.companies;
	}

	public void setCompanies(Set<Company> companies) {
		this.companies = companies;
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
