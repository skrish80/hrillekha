package com.techlords.crown.business.model;

import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class CrownEntityBO extends AppModel {
	private String entity;
	private String description;
	private String entityType;

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public final String getEntityType() {
		return entityType;
	}

	public final void setEntityType(String entityType) {
		this.entityType = entityType;
	}
}