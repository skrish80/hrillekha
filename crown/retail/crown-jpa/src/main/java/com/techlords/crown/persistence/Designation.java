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
 * The persistent class for the designation database table.
 * 
 */
@Entity
@Table(name = "designation")
public class Designation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "DESIGNATION_DESIGNATIONID_GENERATOR", sequenceName = "DESIGNATION_DESIGNATION_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DESIGNATION_DESIGNATIONID_GENERATOR")
	@Column(name = "designation_id", unique = true, nullable = false)
	private Integer designationId;

	@Column(length = 25)
	private String description;

	@Column(nullable = false, length = 10)
	private String designation;

	// bi-directional many-to-one association to CrownUser
	@OneToMany(mappedBy = "designationBean")
	private Set<CrownUser> crownUsers;

	public Designation() {
	}

	public Integer getDesignationId() {
		return this.designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
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