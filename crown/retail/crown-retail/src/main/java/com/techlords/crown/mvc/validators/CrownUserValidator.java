package com.techlords.crown.mvc.validators;

import java.util.ArrayList;
import java.util.List;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.mvc.util.CrownMVCHelper;

public final class CrownUserValidator {

	public void validateUserCreation(CrownUserBO currentUser)
			throws CrownException {
		boolean errorOccured = false;
		final List<String> messages = new ArrayList<String>();
		if (!CrownMVCHelper.checkUniqueness("username",
				currentUser.getUsername())) {
			messages.add("Username not available");
			errorOccured = true;
		}
		if (!CrownMVCHelper.checkUniqueness("email", currentUser.getEmail())) {
			messages.add("Email not available");
			errorOccured = true;
		}
		if (!CrownMVCHelper.checkUniqueness("employee_no",
				currentUser.getEmployeeNo())) {
			messages.add("Employee Number not available");
			errorOccured = true;
		}
		if (errorOccured) {
			throw new CrownException("User cannot be created", messages);
		}
	}
}
