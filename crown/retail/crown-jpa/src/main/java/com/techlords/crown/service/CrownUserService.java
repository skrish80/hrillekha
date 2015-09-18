package com.techlords.crown.service;

import java.util.List;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.business.model.DesignationBO;
import com.techlords.crown.business.model.RoleBO;

public interface CrownUserService extends CrownService {
	boolean createUser(CrownUserBO userBO, int userID) throws CrownException;

	boolean updateUser(CrownUserBO userBO, int userID) throws CrownException;
	
	boolean changePassword(CrownUserBO userBO, int userID) throws CrownException;

	boolean deleteUser(CrownUserBO userBO, int userID) throws CrownException;

	List<CrownUserBO> findAllUsers();

	List<DesignationBO> findAllDesignations();

	List<RoleBO> findAllRoles();
}
