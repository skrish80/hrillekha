package com.techlords.crown.service.impl;

import java.util.List;

import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.helpers.AuditActionEnum;
import com.techlords.crown.helpers.CrownUserHelper;
import com.techlords.crown.persistence.CrownUser;
import com.techlords.crown.service.LoginService;

@SuppressWarnings("serial")
final class LoginServiceImpl extends AbstractCrownService implements
		LoginService {

	@Override
	public CrownUserBO login(String username) {
		@SuppressWarnings("unchecked")
		List<CrownUser> users = manager.createNamedQuery(
				"CrownUser.findByUsername").setParameter(1, username).getResultList();
		if (users.isEmpty()) {
			return null;
		}
		CrownUser user = users.get(0);

		auditLog(AuditActionEnum.LOGIN, user.getUserId(), "login",
				user.getUsername());
		return new CrownUserHelper().createCrownUserBO(user);
	}
}
