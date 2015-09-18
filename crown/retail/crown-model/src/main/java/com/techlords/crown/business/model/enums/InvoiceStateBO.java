package com.techlords.crown.business.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum InvoiceStateBO {
	NEW(1, "NEW"), PRINTED(2, "PRINTED"), DELIVERED(3, "FULL DELIVERY"), RETURNED(
			4, "RETURNED"), CANCELLED(5, "CANCELLED"), PARTIAL_DELIVERY(6,
			"PARTIAL DELIVERY"), CREDIT_NOTE(7, "CREDIT NOTE RELEASED"), CLOSED(
			8, "CLOSED"), PAYMENT_PENDING(9, "PAYMENT PENDING");
	private final int stateID;
	private final String stateName;

	private InvoiceStateBO(int id, String stateName) {
		stateID = id;
		this.stateName = stateName;
	}

	public final int getStateID() {
		return stateID;
	}

	public final String getStateName() {
		return stateName;
	}

	public static final InvoiceStateBO valueOf(int id) {
		for (InvoiceStateBO is : values()) {
			if (is.getStateID() == id) {
				return is;
			}
		}
		return null;
	}

	public static final List<InvoiceStateBO> getValues(List<Integer> ids) {
		final List<InvoiceStateBO> states = new ArrayList<InvoiceStateBO>();
		for (final int id : ids) {
			InvoiceStateBO bo = valueOf(id);
			if (bo != null) {
				states.add(bo);
			}
		}
		return states;
	}

}
