package com.techlords.crown.business.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum PurchaseInvoiceStateBO {
	NEW(1, "NEW", "New"), PRINTED(2, "PRT", "Printed"), RECEIVED(3, "REC",
			"Received"), CANCELLED(5, "CAN", "Cancelled");
	private final int stateID;
	private final String stateCode;
	private final String stateName;

	private PurchaseInvoiceStateBO(int id, String code, String name) {
		stateID = id;
		this.stateCode = code;
		this.stateName = name;
	}

	public final int getStateID() {
		return stateID;
	}

	public final String getStateName() {
		return stateName;
	}

	public static final PurchaseInvoiceStateBO valueOf(int id) {
		for (PurchaseInvoiceStateBO is : values()) {
			if (is.getStateID() == id) {
				return is;
			}
		}
		return null;
	}
	
	public static final PurchaseInvoiceStateBO getValueOf(String code) {
		for (PurchaseInvoiceStateBO is : values()) {
			if (is.getStateCode().equals(code)) {
				return is;
			}
		}
		return null;
	}

	public static final List<PurchaseInvoiceStateBO> getValues(List<Integer> ids) {
		final List<PurchaseInvoiceStateBO> states = new ArrayList<PurchaseInvoiceStateBO>();
		for (final int id : ids) {
			PurchaseInvoiceStateBO bo = valueOf(id);
			if (bo != null) {
				states.add(bo);
			}
		}
		return states;
	}

	public final String getStateCode() {
		return stateCode;
	}

}
