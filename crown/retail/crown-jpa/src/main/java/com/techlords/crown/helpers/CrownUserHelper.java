package com.techlords.crown.helpers;

import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.business.model.DesignationBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.persistence.CrownUser;
import com.techlords.crown.persistence.Designation;

public final class CrownUserHelper {

	public DesignationBO createDesignationBO(Designation designation) {
		final DesignationBO bo = new DesignationBO();
		bo.setId(designation.getDesignationId());
		bo.setVersion(designation.getVersion());
		bo.setDesignation(designation.getDesignation());
		bo.setDescription(designation.getDescription());
		return bo;
	}

	public Designation createDesignation(DesignationBO bo) {
		return createDesignation(bo, null);
	}

	public Designation createDesignation(DesignationBO bo,
			final Designation toEdit) {
		final Designation designation = (toEdit == null) ? new Designation()
				: toEdit;
		designation.setVersion(bo.getVersion());
		designation.setDesignation(bo.getDesignation());
		designation.setDescription(bo.getDescription());
		return designation;
	}

	public CrownUserBO createCrownUserBO(CrownUser user) {
		final CrownUserBO bo = new CrownUserBO();
		bo.setId(user.getUserId());
		bo.setVersion(user.getVersion());
		bo.setUsername(user.getUsername());
		bo.setPassword(user.getPassword());
		bo.setEmployeeNo(user.getEmployeeNo());
		bo.setFirstName(user.getFirstName());
		bo.setLastName(user.getLastName());
		bo.setEmail(user.getEmail());
		bo.setAddress(user.getAddress());
		bo.setPhone(user.getPhone());
		bo.setMobileNumber(user.getMobile());
		bo.setDateOfBirth(user.getDateOfBirth());
		bo.setJoinDate(user.getJoiningDate());

		bo.setDesignationBO(createDesignationBO(user.getDesignationBean()));
		bo.setDesignation(user.getDesignationBean().getDesignationId());

		bo.setDepartmentBO(new DepartmentHelper().createDepartmentBO(user
				.getDepartmentBean()));
		bo.setDepartment(user.getDepartmentBean().getDepartmentId());

		bo.setRoleBO(new RoleHelper().createRoleBO(user.getRoleBean()));
		bo.setRole(user.getRoleBean().getRoleId());

		bo.setLocationBO(new LocationHelper().createLocationBO(user
				.getLocationBean()));
		bo.setLocation(user.getLocationBean().getLocationId());

		bo.setStatus(StatusBO.valueOf(user.getStatusBean().getStatusId()));

		return bo;
	}

	public CrownUser createUser(CrownUserBO bo) {
		return createUser(bo, null);
	}

	public CrownUser createUser(CrownUserBO bo, CrownUser toEdit) {
		final CrownUser user = (toEdit == null) ? new CrownUser() : toEdit;
		if (toEdit == null) {
			user.setUsername(bo.getUsername());
		}
		user.setVersion(bo.getVersion());
		user.setUsername(bo.getUsername());
		user.setPassword(bo.getPassword());
		user.setEmployeeNo(bo.getEmployeeNo());
		user.setFirstName(bo.getFirstName());
		user.setLastName(bo.getLastName());
		user.setEmail(bo.getEmail());
		user.setAddress(bo.getAddress());
		user.setPhone(bo.getPhone());
		user.setMobile(bo.getMobileNumber());
		user.setDateOfBirth(bo.getDateOfBirth());
		user.setJoiningDate(bo.getJoinDate());

		return user;
	}
}
