package com.techlords.crown.business.model;

import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public final class BankBO extends AppModel {

	private String bankCode;
	private String bankName;
	private String description;

	public final String getBankCode() {
		return bankCode;
	}

	public final void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public final String getBankName() {
		return bankName;
	}

	public final void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}
}
