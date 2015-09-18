/**
 * 
 */
package com.techlords.crown.helpers;

import com.techlords.crown.business.model.AgentBO;
import com.techlords.crown.business.model.AgentTypeBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.persistence.Agent;
import com.techlords.crown.persistence.AgentType;

/**
 * @author gv
 * 
 */
public final class AgentHelper {

	public AgentTypeBO createAgentTypeBO(AgentType type) {
		final AgentTypeBO bo = new AgentTypeBO();
		bo.setId(type.getAgentTypeId());
		bo.setVersion(type.getVersion());
		bo.setAgentType(type.getAgentType());
		bo.setDescription(type.getDescription());
		return bo;
	}

	public AgentType createAgentType(AgentTypeBO bo, AgentType toEdit) {
		final AgentType type = (toEdit == null) ? new AgentType() : toEdit;
		type.setAgentType(type.getAgentType());
		type.setVersion(bo.getVersion());
		type.setDescription(type.getDescription());
		return type;
	}

	public AgentBO createAgentBO(Agent agent) {
		final AgentBO bo = new AgentBO();
		bo.setId(agent.getAgentId());
		bo.setVersion(agent.getVersion());
		bo.setAgentName(agent.getAgentName());
		bo.setAddress(agent.getAddress());
		bo.setPhone(agent.getPhone());

		bo.setAgentType(agent.getAgentTypeBean().getAgentTypeId());
		bo.setAgentTypeBO(createAgentTypeBO(agent.getAgentTypeBean()));

		bo.setLocation(agent.getLocation().getLocationId());
		bo.setLocationBO(new LocationHelper().createLocationBO(agent
				.getLocation()));

		bo.setStatus(StatusBO.valueOf(agent.getStatus().getStatusId()));
		return bo;
	}

	public Agent createAgent(AgentBO bo) {
		return createAgent(bo, null);
	}

	public Agent createAgent(AgentBO bo, Agent toEdit) {
		Agent agent = (toEdit == null) ? new Agent() : toEdit;
		if (toEdit == null) {
			agent.setAgentName(bo.getAgentName());
		}
		agent.setVersion(bo.getVersion());
		agent.setAgentName(bo.getAgentName());
		agent.setAddress(bo.getAddress());
		agent.setPhone(bo.getPhone());
		return agent;
	}
}
