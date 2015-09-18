/**
 * 
 */
package com.techlords.crown.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.springframework.transaction.annotation.Transactional;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.business.model.DesignationBO;
import com.techlords.crown.business.model.RoleBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.helpers.AuditActionEnum;
import com.techlords.crown.helpers.CrownUserHelper;
import com.techlords.crown.helpers.RoleHelper;
import com.techlords.crown.persistence.CrownUser;
import com.techlords.crown.persistence.Department;
import com.techlords.crown.persistence.Designation;
import com.techlords.crown.persistence.Location;
import com.techlords.crown.persistence.Role;
import com.techlords.crown.persistence.Status;
import com.techlords.crown.service.CrownUserService;
import com.techlords.crown.service.GeneralService;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
final class CrownUserServiceImpl extends AbstractCrownService implements CrownUserService {

	private void setUserAttributes(CrownUser user, CrownUserBO bo) {
		user.setDesignationBean(manager.find(Designation.class, bo.getDesignationBO().getId()));
		user.setDepartmentBean(manager.find(Department.class, bo.getDepartmentBO().getId()));
		user.setRoleBean(manager.find(Role.class, bo.getRoleBO().getId()));
		user.setLocationBean(manager.find(Location.class, bo.getLocationBO().getId()));
		user.setStatusBean(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));
		user.setVersion(bo.getVersion());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.CrownUserService#addUser(com.techlords.crown
	 * .business.model.CrownUserBO)
	 */
	@Transactional
	@Override
	public boolean createUser(CrownUserBO userBO, int userID) throws CrownException {
		try {
			final CrownUserHelper helper = new CrownUserHelper();
			final CrownUser user = helper.createUser(userBO);
			final GeneralService generalService = CrownServiceLocator.INSTANCE
					.getCrownService(GeneralService.class);
			final String employeeID = "EMP"
					+ generalService.getRunningSequenceNumber("CROWN_USER", null);
			user.setEmployeeNo(employeeID);

			setUserAttributes(user, userBO);

			manager.persist(user);

			auditLog(AuditActionEnum.CREATE, userID, "crownUser", user.getUsername());
		} catch (Exception e) {
			throw new CrownException("User cannot be created", e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.CrownUserService#updateUser(com.techlords
	 * .crown.business.model.CrownUserBO)
	 */
	@Transactional
	@Override
	public boolean updateUser(CrownUserBO userBO, int userID) throws CrownException {
		try {
			final CrownUserHelper helper = new CrownUserHelper();
			final CrownUser user = helper.createUser(userBO,
					manager.find(CrownUser.class, userBO.getId()));
			setUserAttributes(user, userBO);
			manager.merge(user);

			auditLog(AuditActionEnum.UPDATE, userID, "crownUser", user.getUsername());
		} catch (OptimisticLockException e) {
			throw new CrownException("User has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean changePassword(CrownUserBO userBO, int userID) throws CrownException {
		try {
			final CrownUser user = manager.find(CrownUser.class, userBO.getId());
			user.setVersion(userBO.getVersion());
			user.setPassword(userBO.getPassword());
			manager.merge(user);

			auditLog(AuditActionEnum.PASSWORD, userID, "crownUser", user.getUsername());
		} catch (OptimisticLockException e) {
			throw new CrownException("User has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.CrownUserService#deleteUser(com.techlords
	 * .crown.business.model.CrownUserBO)
	 */
	@Transactional
	@Override
	public boolean deleteUser(CrownUserBO userBO, int userID) throws CrownException {
		try {
			final CrownUser user = manager.find(CrownUser.class, userBO.getId());
			user.setVersion(userBO.getVersion());
			user.setStatusBean(manager.find(Status.class, StatusBO.DISABLED.getStatusID()));
			manager.merge(user);

			auditLog(AuditActionEnum.DELETE, userID, "crownUser", user.getUsername());
		} catch (OptimisticLockException e) {
			throw new CrownException("User has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.CrownUserService#findAllUsers()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CrownUserBO> findAllUsers() {
		final List<CrownUser> users = manager.createNamedQuery("CrownUser.findActiveUsers")
				.getResultList();
		final List<CrownUserBO> userBOs = new ArrayList<CrownUserBO>();
		final CrownUserHelper helper = new CrownUserHelper();
		for (final CrownUser company : users) {
			userBOs.add(helper.createCrownUserBO(company));
		}
		return userBOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.CrownUserService#findAllDesignations()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DesignationBO> findAllDesignations() {
		final List<Designation> designations = manager.createQuery(
				"select d from Designation d order by d.designation").getResultList();
		final List<DesignationBO> bos = new ArrayList<DesignationBO>();
		final CrownUserHelper helper = new CrownUserHelper();
		for (final Designation designation : designations) {
			bos.add(helper.createDesignationBO(designation));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleBO> findAllRoles() {
		final List<Role> roles = manager.createQuery("select r from Role r order by r.roleName")
				.getResultList();
		final List<RoleBO> bos = new ArrayList<RoleBO>();
		final RoleHelper helper = new RoleHelper();
		for (final Role role : roles) {
			bos.add(helper.createRoleBO(role));
		}
		return bos;
	}

}
