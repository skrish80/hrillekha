package com.techlords.crown.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the agent_commission database table.
 * 
 */
@Embeddable
public class AgentCommissionPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "invoice_id", unique = true, nullable = false)
	private Integer invoiceId;

	@Column(name = "agent_id", unique = true, nullable = false)
	private Integer agentId;

	public AgentCommissionPK() {
	}

	public Integer getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getAgentId() {
		return this.agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AgentCommissionPK)) {
			return false;
		}
		AgentCommissionPK castOther = (AgentCommissionPK) other;
		return this.invoiceId.equals(castOther.invoiceId)
				&& this.agentId.equals(castOther.agentId);

	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.invoiceId.hashCode();
		hash = hash * prime + this.agentId.hashCode();

		return hash;
	}
}