package com.techlords.infra;

public enum OperationType implements IOperationType {
	DUMMY_OPERATION("dummy"); //$NON-NLS-1$

	private final String operationType;

	/**
	 * constructor
	 * 
	 * @param operationType
	 */
	OperationType(String operationType_p) {
		operationType = operationType_p;
	}

	@Override
	public String toString() {
		return operationType;
	}
}
