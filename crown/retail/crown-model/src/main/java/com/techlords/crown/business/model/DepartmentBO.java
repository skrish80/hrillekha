package com.techlords.crown.business.model;

import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class DepartmentBO extends AppModel {

	private String department;
	private String description;

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}