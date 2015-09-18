package com.techlords.crown.persistence;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the agent_commission database table.
 * 
 */
@Entity
@Table(name = "agent_commission")
public class AgentCommission implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AgentCommissionPK id;

	private double commission;

	private Integer status;

	// bi-directional many-to-one association to Agent
	@ManyToOne
	@JoinColumn(name = "agent_id", nullable = false, insertable = false, updatable = false)
	private Agent agent;

	// bi-directional many-to-one association to Invoice
	@ManyToOne
	@JoinColumn(name = "invoice_id", nullable = false, insertable = false, updatable = false)
	private Invoice invoice;

	public AgentCommission() {
	}

	public AgentCommissionPK getId() {
		return this.id;
	}

	public void setId(AgentCommissionPK id) {
		this.id = id;
	}

	public double getCommission() {
		return this.commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Agent getAgent() {
		return this.agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public Invoice getInvoice() {
		return this.invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

}