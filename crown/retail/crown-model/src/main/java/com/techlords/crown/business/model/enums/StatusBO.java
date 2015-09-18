package com.techlords.crown.business.model.enums;

public enum StatusBO {
	ACTIVE(1), DISABLED(2);

	private final int statusID;

	private StatusBO(int id) {
		statusID = id;
	}

	/**
	 * @return the statusID
	 */
	public final int getStatusID() {
		return statusID;
	}

	public static final StatusBO valueOf(int id) {
		for (StatusBO st : values()) {
			if (st.getStatusID() == id) {
				return st;
			}
		}
		return null;
	}
}
