package com.techlords.crown.business.model.enums;

public enum AllocationTypeBO {
	UOM(1), ITEM(2);

	private final int allocationTypeID;
	@SuppressWarnings("unused")
	private String type;

	private AllocationTypeBO(int id) {
		allocationTypeID = id;
	}

	public final int getAllocationTypeID() {
		return allocationTypeID;
	}

	public static final AllocationTypeBO valueOf(int id) {
		for (AllocationTypeBO at : values()) {
			if (at.getAllocationTypeID() == id) {
				return at;
			}
		}
		return null;
	}
	
	public final String getType() {
		return name();
	}


}