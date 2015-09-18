package com.techlords.crown.business.model.enums;


public enum AllocationStateBO  {
	
	ALLOCATED(1), RELEASED(2), DELIVERED(3), STOLEN_DAMAGED(4);
	
	private final int stateID;

	private AllocationStateBO(int id) {
		stateID = id;
	}

	/**
	 * @return the statusID
	 */
	public final int getAllocationStateID() {
		return stateID;
	}

	public static final AllocationStateBO valueOf(int id) {
		for (AllocationStateBO st : values()) {
			if (st.getAllocationStateID() == id) {
				return st;
			}
		}
		return null;
	}
}
