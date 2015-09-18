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
 * The persistent class for the agent database table.
 * 
 */
@Entity
@Table(name = "agent")
@NamedQuery(name = "findActiveAgents", query = "select A from Agent A where A.status.statusId=1 order by A.agentName")
public class Agent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "AGENT_AGENTID_GENERATOR", sequenceName = "AGENT_AGENT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGENT_AGENTID_GENERATOR")
	@Column(name = "agent_id", unique = true, nullable = false)
	private Integer agentId;

	@Column(name = "address", length = 2147483647)
	private String address;

	@Column(name = "agent_name", length = 50)
	private String agentName;

	@Column(name = "phone", length = 25)
	private String phone;

	// bi-directional many-to-one association to AgentType
	@ManyToOne
	@JoinColumn(name = "agent_type")
	private AgentType agentTypeBean;

	// bi-directional many-to-one association to Location
	@ManyToOne
	@JoinColumn(name = "location_id")
	private Location location;

	// bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status status;

	// bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy = "agent")
	private Set<Invoice> invoices;

	// bi-directional many-to-one association to AgentCommission
	@OneToMany(mappedBy = "agent")
	private Set<AgentCommission> agentCommissions;

	public Agent() {
	}

	public Integer getAgentId() {
		return this.agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAgentName() {
		return this.agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public AgentType getAgentTypeBean() {
		return this.agentTypeBean;
	}

	public void setAgentTypeBean(AgentType agentTypeBean) {
		this.agentTypeBean = agentTypeBean;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Set<AgentCommission> getAgentCommissions() {
		return this.agentCommissions;
	}

	public void setAgentCommissions(Set<AgentCommission> agentCommissions) {
		this.agentCommissions = agentCommissions;
	}

	public Set<Invoice> getInvoices() {
		return this.invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
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