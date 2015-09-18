package com.techlords.crown.persistence;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the crown_audit database table.
 * 
 */
@Entity
@Table(name = "crown_audit")
@NamedQueries({
@NamedQuery(name = "CrownAudit.findAllAudit", query = "select CA from CrownAudit CA where CA.tableName <> ?1 order by CA.tableName"),
@NamedQuery(name = "CrownAudit.findAllAuditRange", query = "select CA from CrownAudit CA where CA.tableName <> ?1 and CA.actionTime > ?2 and CA.actionTime < ?3 order by CA.tableName")})
public class CrownAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CROWN_AUDIT_AUDITID_GENERATOR", sequenceName = "CROWN_AUDIT_AUDIT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CROWN_AUDIT_AUDITID_GENERATOR")
	@Column(name = "audit_id", unique = true, nullable = false)
	private Integer auditId;

	@Column(name = "action_time", nullable = false)
	private Timestamp actionTime;

	@Column(name = "description", length = 2147483647)
	private String description;

	@Column(name = "table_name", nullable = false, length = 32)
	private String tableName;

	// bi-directional many-to-one association to AuditAction
	@ManyToOne
	@JoinColumn(name = "action", nullable = false)
	private AuditAction auditAction;

	// bi-directional many-to-one association to CrownUser
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private CrownUser crownUser;

	public CrownAudit() {
	}

	public Integer getAuditId() {
		return this.auditId;
	}

	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}

	public Timestamp getActionTime() {
		return this.actionTime;
	}

	public void setActionTime(Timestamp actionTime) {
		this.actionTime = actionTime;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public AuditAction getAuditAction() {
		return this.auditAction;
	}

	public void setAuditAction(AuditAction auditAction) {
		this.auditAction = auditAction;
	}

	public CrownUser getCrownUser() {
		return this.crownUser;
	}

	public void setCrownUser(CrownUser crownUser) {
		this.crownUser = crownUser;
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