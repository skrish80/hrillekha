package com.techlords.crown.business.model.enums;

public enum DiscountTypeBO {
	PERCENTAGE(1), FIXED(2);
	private final int typeID;

	private DiscountTypeBO(int id) {
		typeID = id;
	}

	public final int getTypeID() {
		return typeID;
	}

	public static final DiscountTypeBO valueOf(int id) {
		for (DiscountTypeBO dt : values()) {
			if (dt.getTypeID() == id) {
				return dt;
			}
		}
		return null;
	}
	
	public final String getDiscountType() {
		return name();
	}
}
