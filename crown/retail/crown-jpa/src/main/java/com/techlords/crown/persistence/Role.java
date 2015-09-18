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
 * The persistent class for the role database table.
 * 
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ROLE_ROLEID_GENERATOR", sequenceName = "ROLE_ROLE_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_ROLEID_GENERATOR")
	@Column(name = "role_id", unique = true, nullable = false)
	private Integer roleId;

	@Column(name = "role_description", nullable = false, length = 32)
	private String roleDescription;

	@Column(name = "role_name", nullable = false, length = 32)
	private String roleName;

	// bi-directional many-to-one association to CrownUser
	@OneToMany(mappedBy = "roleBean")
	private Set<CrownUser> crownUsers;

	// bi-directional many-to-one association to Right
	@OneToMany(mappedBy = "role")
	private Set<Right> rights;

	public Role() {
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleDescription() {
		return this.roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<CrownUser> getCrownUsers() {
		return this.crownUsers;
	}

	public void setCrownUsers(Set<CrownUser> crownUsers) {
		this.crownUsers = crownUsers;
	}

	public Set<Right> getRights() {
		return this.rights;
	}

	public void setRights(Set<Right> rights) {
		this.rights = rights;
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