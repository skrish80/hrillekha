/**
 * 
 */
package com.techlords.crown.business.model.enums;

import com.techlords.infra.IOperationType;

/**
 * enum for Operation types on crown product.
 * <P>
 * No need of externalizing the String
 * 
 * @author G. Vaidhyanathan
 * 
 */
public enum CrownOperationType implements IOperationType {

	COMPANY_CREATE("Company Creation Operation"), COMPANY_TYPE_SEARCH(
			"Search Company Types"), COMPANY_SEARCH("Search Companies");

	private final String operationType;

	/**
	 * constructor
	 * 
	 * @param operationType
	 */
	CrownOperationType(String operationType_p) {
		operationType = operationType_p;
	}

	@Override
	public String toString() {
		return operationType;
	}
}
