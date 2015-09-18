package com.techlords.crown.service;

import com.techlords.crown.business.model.CrownUserBO;

public interface LoginService extends CrownService {
	CrownUserBO login(String username);
}
