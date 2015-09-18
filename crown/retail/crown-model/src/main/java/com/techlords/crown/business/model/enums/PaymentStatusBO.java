package com.techlords.crown.business.model.enums;

public enum PaymentStatusBO {
	FULL_PAYMENT(1, "Full Payment"), PARTIAL_PAYMENT(2, "Partial Payment"), CREDIT_SALES(
			3, "Credit Sales");

	private final int statusID;
	private final String status;

	private PaymentStatusBO(final int id, final String status) {
		statusID = id;
		this.status = status;
	}

	public int getStatusID() {
		return statusID;
	}

	public static final PaymentStatusBO valueOf(int id) {
		for (PaymentStatusBO ps : values()) {
			if (ps.getStatusID() == id) {
				return ps;
			}
		}
		return null;
	}

	public String getStatus() {
		return status;
	}
}
