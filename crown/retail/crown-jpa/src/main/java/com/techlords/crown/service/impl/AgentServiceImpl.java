/**
 * 
 */
package com.techlords.crown.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.springframework.transaction.annotation.Transactional;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.AgentBO;
import com.techlords.crown.business.model.AgentTypeBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.helpers.AgentHelper;
import com.techlords.crown.helpers.AuditActionEnum;
import com.techlords.crown.persistence.Agent;
import com.techlords.crown.persistence.AgentType;
import com.techlords.crown.persistence.Location;
import com.techlords.crown.persistence.Status;
import com.techlords.crown.service.AgentService;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
final class AgentServiceImpl extends AbstractCrownService implements AgentService {

	private void setAgentAttributes(Agent agent, AgentBO bo) {
		agent.setLocation(manager.find(Location.class, bo.getLocationBO().getId()));
		agent.setAgentTypeBean(manager.find(AgentType.class, bo.getAgentType()));
		agent.setStatus(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));
		agent.setVersion(bo.getVersion());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.AgentService#createAgent(com.techlords
	 * .crown.business.model.AgentBO)
	 */
	@Transactional
	@Override
	public boolean createAgent(AgentBO agentBO, int userID) throws CrownException {
		try {
			final AgentHelper helper = new AgentHelper();
			final Agent agent = helper.createAgent(agentBO);
			setAgentAttributes(agent, agentBO);
			manager.persist(agent);

			auditLog(AuditActionEnum.CREATE, userID, "agent", agent.getAgentName());
		} catch (OptimisticLockException e) {
			throw new CrownException("Agent has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.AgentService#editAgent(com.techlords
	 * .crown.business.model.AgentBO)
	 */
	@Transactional
	@Override
	public boolean updateAgent(AgentBO agentBO, int userID) throws CrownException {
		try {
			final AgentHelper helper = new AgentHelper();
			final Agent agent = helper.createAgent(agentBO,
					manager.find(Agent.class, agentBO.getId()));
			setAgentAttributes(agent, agentBO);
			manager.merge(agent);

			auditLog(AuditActionEnum.UPDATE, userID, "agent", agent.getAgentName());
		} catch (OptimisticLockException e) {
			throw new CrownException("Agent has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.AgentService#deleteAgent(com.techlords
	 * .crown.business.model.AgentBO)
	 */
	@Transactional
	@Override
	public boolean deleteAgent(AgentBO agentBO, int userID) throws CrownException {
		try {
			final Agent agent = manager.find(Agent.class, agentBO.getId());
			agent.setVersion(agentBO.getVersion());
			agent.setStatus(manager.find(Status.class, StatusBO.DISABLED.getStatusID()));
			manager.merge(agent);
			auditLog(AuditActionEnum.DELETE, userID, "agent", agent.getAgentName());
		} catch (OptimisticLockException e) {
			throw new CrownException("Agent has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.AgentService#findAllAgents()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AgentBO> findAllAgents() {
		final List<Agent> agents = manager.createNamedQuery("findActiveAgents").getResultList();
		final List<AgentBO> bos = new ArrayList<AgentBO>();
		final AgentHelper helper = new AgentHelper();
		for (final Agent agent : agents) {
			bos.add(helper.createAgentBO(agent));
		}
		return bos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.AgentService#searchAgentTypes()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AgentTypeBO> findAllAgentTypes() {
		final List<AgentType> agents = manager.createQuery("select ct from AgentType ct")
				.getResultList();
		final List<AgentTypeBO> bos = new ArrayList<AgentTypeBO>();
		final AgentHelper helper = new AgentHelper();
		for (final AgentType agent : agents) {
			bos.add(helper.createAgentTypeBO(agent));
		}
		return bos;
	}
}
