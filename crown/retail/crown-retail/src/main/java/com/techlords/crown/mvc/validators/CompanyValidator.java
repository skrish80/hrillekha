package com.techlords.crown.mvc.validators;

import java.util.ArrayList;
import java.util.List;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CompanyBO;
import com.techlords.crown.mvc.util.CrownMVCHelper;

public final class CompanyValidator {

	public void validateCompanyCreation(CompanyBO currentCompany)
			throws CrownException {
		boolean errorOccured = false;
		final List<String> messages = new ArrayList<String>();
		if (!CrownMVCHelper.checkUniqueness("company_name",
				currentCompany.getCompanyName())) {
			messages.add("Company Name not available");
			errorOccured = true;
		}
		if (errorOccured) {
			throw new CrownException("Company cannot be created", messages);
		}
	}
}
