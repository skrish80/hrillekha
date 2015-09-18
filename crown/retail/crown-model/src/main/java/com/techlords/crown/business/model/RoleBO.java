package com.techlords.crown.business.model;

import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class RoleBO extends AppModel {

	private String role;
	private String description;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}