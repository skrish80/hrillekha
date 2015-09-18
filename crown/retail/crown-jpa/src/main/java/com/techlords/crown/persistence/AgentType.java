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
 * The persistent class for the agent_type database table.
 * 
 */
@Entity
@Table(name = "agent_type")
public class AgentType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "AGENT_TYPE_AGENTTYPEID_GENERATOR", sequenceName = "AGENT_TYPE_AGENT_TYPE_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGENT_TYPE_AGENTTYPEID_GENERATOR")
	@Column(name = "agent_type_id", unique = true, nullable = false)
	private Integer agentTypeId;

	@Column(name = "agent_type", nullable = false, length = 25)
	private String agentType;

	@Column(length = 100)
	private String description;

	// bi-directional many-to-one association to Agent
	@OneToMany(mappedBy = "agentTypeBean")
	private Set<Agent> agents;

	public AgentType() {
	}

	public Integer getAgentTypeId() {
		return this.agentTypeId;
	}

	public void setAgentTypeId(Integer agentTypeId) {
		this.agentTypeId = agentTypeId;
	}

	public String getAgentType() {
		return this.agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Agent> getAgents() {
		return this.agents;
	}

	public void setAgents(Set<Agent> agents) {
		this.agents = agents;
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