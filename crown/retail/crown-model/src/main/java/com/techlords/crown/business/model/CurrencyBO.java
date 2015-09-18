package com.techlords.crown.business.model;

import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class CurrencyBO extends AppModel {
	private String currencyCode;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public final boolean isNew() {
		return (currencyCode == null);
	}

	/**
	 * @return the currencyCode
	 */
	public final String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode
	 *            the currencyCode to set
	 */
	public final void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
}