/**
 * 
 */
package com.techlords.crown.helpers;

import com.techlords.crown.business.model.RoleBO;
import com.techlords.crown.persistence.Role;

/**
 * @author gv
 * 
 */
public final class RoleHelper {

	public RoleBO createRoleBO(Role role) {
		final RoleBO bo = new RoleBO();
		bo.setId(role.getRoleId());
		bo.setVersion(role.getVersion());
		bo.setRole(role.getRoleName());
		bo.setDescription(role.getRoleDescription());
		return bo;
	}
}
