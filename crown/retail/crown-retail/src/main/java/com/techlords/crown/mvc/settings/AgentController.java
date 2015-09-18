package com.techlords.crown.mvc.settings;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.AgentBO;
import com.techlords.crown.business.model.AgentTypeBO;
import com.techlords.crown.business.model.LocationBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.service.AgentService;

/**
 * Handles requests for the application home page.
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class AgentController extends CrownModelController {

	private static final Logger LOGGER = Logger.getLogger(AgentController.class); 

	private final List<AgentBO> agentBOs = new ArrayList<AgentBO>();
	private AgentBO currentAgent;
	private final List<LocationBO> locationBOs = new ArrayList<LocationBO>();
	private final List<AgentTypeBO> agentTypeBOs = new ArrayList<AgentTypeBO>();
	private boolean areAssociationsLoaded = false;

	public void getHome() {
		navigationBean.setNavigationUrl("config/Agent.xhtml");
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	public List<AgentBO> getAgentBOs() {
		if (FacesUtil.isRenderPhase() && !isListLoaded) {
			agentBOs.clear();
			setCurrentAgent(null);
			AgentService service = CrownServiceLocator.INSTANCE
					.getCrownService(AgentService.class);
			agentBOs.addAll(service.findAllAgents());
			isListLoaded = true;
		}
		return agentBOs;
	}

	public String setupForm(boolean isCreateMode) {
		return isCreateMode ? setupForm(new AgentBO()) : null;
	}

	public String setupForm(AgentBO bo) {
		setCurrentAgent(bo);
		setFieldAvailability(EMPTY_STRING);
		loadAssociations();
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/UpdateAgent.xhtml");
		return null;
	}

	public void checkUniqueAgentName() {
		setFieldAvailability(CrownMVCHelper.checkUniqueness("agent_name",
				currentAgent.getAgentName()) ? AVAILABLE : UNAVAILABLE);
	}

	public String save() {
		currentAgent.setAgentTypeBO(getAppModel(currentAgent.getAgentType(),
				agentTypeBOs));
		currentAgent.setLocationBO(getAppModel(currentAgent.getLocation(),
				locationBOs));
		return currentAgent.isNew() ? create() : update();
	}

	public String create() {

		AgentService service = CrownServiceLocator.INSTANCE
				.getCrownService(AgentService.class);
		try {
			service.createAgent(currentAgent, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("config/Agent.xhtml");
		return null;
	}

	public String update() {

		AgentService service = CrownServiceLocator.INSTANCE
				.getCrownService(AgentService.class);
		try {
			service.updateAgent(currentAgent, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("config/Agent.xhtml");
		return null;
	}

	public String delete(AgentBO bo) {
		AgentService service = CrownServiceLocator.INSTANCE
				.getCrownService(AgentService.class);
		try {
			service.deleteAgent(bo, CrownUserDetailsService.getCurrentUser()
					.getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/Agent.xhtml");
		return null;
	}

	public String view(AgentBO bo) {
		setCurrentAgent(bo);
		isListLoaded = false;
		navigationBean.setNavigationUrl("config/ViewAgent.xhtml");
		return null;
	}

	private void loadAssociations() {
		if (areAssociationsLoaded) {
			return;
		}

		agentTypeBOs.clear();
		locationBOs.clear();

		agentTypeBOs.addAll(CrownMVCHelper.getAgentTypeBos());
		locationBOs.addAll(CrownMVCHelper.getLocationBos());
		areAssociationsLoaded = true;
	}

	public AgentBO getCurrentAgent() {
		return currentAgent;
	}

	public void setCurrentAgent(AgentBO currentAgent) {
		this.currentAgent = currentAgent;
	}

	public List<LocationBO> getLocationBOs() {
		return locationBOs;
	}

	public List<AgentTypeBO> getAgentTypeBOs() {
		return agentTypeBOs;
	}

}
