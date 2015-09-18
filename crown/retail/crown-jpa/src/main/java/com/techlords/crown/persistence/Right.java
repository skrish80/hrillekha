package com.techlords.crown.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the rights database table.
 * 
 */
@Entity
@Table(name = "rights")
public class Right implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "RIGHTS_RIGHTID_GENERATOR", sequenceName = "RIGHTS_RIGHT_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RIGHTS_RIGHTID_GENERATOR")
	@Column(name = "right_id", unique = true, nullable = false)
	private Integer rightId;

	@Column(length = 2147483647)
	private String description;

	@Column(name = "right_name", nullable = false, length = 50)
	private String rightName;

	// bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

	public Right() {
	}

	public Integer getRightId() {
		return this.rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRightName() {
		return this.rightName;
	}

	public void setRightName(String rightName) {
		this.rightName = rightName;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
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