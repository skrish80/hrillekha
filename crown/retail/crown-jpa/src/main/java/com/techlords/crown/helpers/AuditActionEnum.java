package com.techlords.crown.helpers;

public enum AuditActionEnum {
	CREATE(1), UPDATE(2), UPDATE_PMT(2), DELETE(3), LOGIN(4), PRINT(2), DELIVER(
			2), RETURN(2), CANCEL(3), PARTIAL_ACCEPT(2), ACCEPT(2), CLOSED(2), UTILIZED(
			2), GENERATE_RECEIPT(2), AMEND_RECEIPT(2), CREDIT_LIMIT(2), RECEIVE(
			2), PASSWORD(2);

	private final int actionID;

	private AuditActionEnum(int id) {
		actionID = id;
	}

	/**
	 * @return the statusID
	 */
	public final int getActionID() {
		return actionID;
	}

	public static final AuditActionEnum valueOf(int id) {
		for (AuditActionEnum action : values()) {
			if (action.getActionID() == id) {
				return action;
			}
		}
		return null;
	}

	public String getActionName() {
		return name();
	}
}
