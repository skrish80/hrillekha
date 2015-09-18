/**
 * 
 */
package com.techlords.crown.service;

import java.util.List;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.AgentBO;
import com.techlords.crown.business.model.AgentTypeBO;

/**
 * @author gv
 * 
 */
public interface AgentService extends CrownService {

	public boolean createAgent(AgentBO agentBO, int userID) throws CrownException;

	public boolean updateAgent(AgentBO agentBO, int userID) throws CrownException;

	public boolean deleteAgent(AgentBO agentBO, int userID) throws CrownException;

	public List<AgentBO> findAllAgents();

	public List<AgentTypeBO> findAllAgentTypes();
}
