package com.techlords.crown.business.model.enums;

public enum PaymentModeBO {
	CASH(1, "Cash"), CHEQUE(2, "Cheque"), CREDIT_NOTE(3, "Credit Note"), RECEIPT(4, "Receipt");

	private final int modeID;
	private final String modeName;

	private PaymentModeBO(int id, String mode) {
		this.modeID = id;
		this.modeName = mode;
	}

	public int getModeID() {
		return modeID;
	}

	public String getModeName() {
		return modeName;
	}

	public static final PaymentModeBO valueOf(int id) {
		for (PaymentModeBO pm : values()) {
			if (pm.getModeID() == id) {
				return pm;
			}
		}
		return null;
	}
}
