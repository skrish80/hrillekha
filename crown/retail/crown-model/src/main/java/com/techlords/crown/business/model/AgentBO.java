package com.techlords.crown.business.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class AgentBO extends AppModel {

	@NotEmpty(message = "Agent Name cannot be empty")
	@Size(min = 5, max = 25, message = "Agent Name shall be from 5 to 25 chars")
	private String agentName;
	private String address;
	@NotEmpty(message = "Agent Phone cannot be empty")
	@Size(min = 5, max = 25, message = "Agent Phone shall be from 5 to 25 chars")
	private String phone;

	@Min(value = 1, message = "Select an agent type")
	private int agentType;
	@Min(value = 1, message = "Select a location")
	private int location;

	private AgentTypeBO agentTypeBO;
	private LocationBO locationBO;

	private StatusBO status;

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the agentType
	 */
	public final int getAgentType() {
		return agentType;
	}

	/**
	 * @param agentType
	 *            the agentType to set
	 */
	public final void setAgentType(int agentType) {
		this.agentType = agentType;
	}

	/**
	 * @return the location
	 */
	public final int getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public final void setLocation(int location) {
		this.location = location;
	}

	/**
	 * @return the agentTypeBO
	 */
	public final AgentTypeBO getAgentTypeBO() {
		return agentTypeBO;
	}

	/**
	 * @param agentTypeBO
	 *            the agentTypeBO to set
	 */
	public final void setAgentTypeBO(AgentTypeBO agentTypeBO) {
		this.agentTypeBO = agentTypeBO;
	}

	/**
	 * @return the locationBO
	 */
	public final LocationBO getLocationBO() {
		return locationBO;
	}

	/**
	 * @param locationBO
	 *            the locationBO to set
	 */
	public final void setLocationBO(LocationBO locationBO) {
		this.locationBO = locationBO;
	}

	/**
	 * @return the status
	 */
	public final StatusBO getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public final void setStatus(StatusBO status) {
		this.status = status;
	}
}