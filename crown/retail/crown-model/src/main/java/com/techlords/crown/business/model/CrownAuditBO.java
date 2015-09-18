package com.techlords.crown.business.model;

import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class CrownAuditBO extends AppModel {
	private String tableName;
	private String crownUser;
	private String timestamp;
	private String action;
	private String description;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCrownUser() {
		return crownUser;
	}

	public void setCrownUser(String crownUser) {
		this.crownUser = crownUser;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
