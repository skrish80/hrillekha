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
 * The persistent class for the audit_action database table.
 * 
 */
@Entity
@Table(name = "audit_action")
public class AuditAction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "AUDIT_ACTION_ACTIONID_GENERATOR", sequenceName = "AUDIT_ACTION_ACTION_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUDIT_ACTION_ACTIONID_GENERATOR")
	@Column(name = "action_id", unique = true, nullable = false)
	private Integer actionId;

	@Column(name = "action_code", nullable = false, length = 16)
	private String actionCode;

	@Column(length = 64)
	private String description;

	// bi-directional many-to-one association to CrownAudit
	@OneToMany(mappedBy = "auditAction")
	private Set<CrownAudit> crownAudits;

	public AuditAction() {
	}

	public Integer getActionId() {
		return this.actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public String getActionCode() {
		return this.actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<CrownAudit> getCrownAudits() {
		return this.crownAudits;
	}

	public void setCrownAudits(Set<CrownAudit> crownAudits) {
		this.crownAudits = crownAudits;
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