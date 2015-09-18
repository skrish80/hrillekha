/**
 * 
 */
package com.techlords.crown.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.techlords.crown.business.model.DepartmentBO;
import com.techlords.crown.helpers.DepartmentHelper;
import com.techlords.crown.persistence.Department;
import com.techlords.crown.service.DepartmentService;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
final class DepartmentServiceImpl extends AbstractCrownService implements DepartmentService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.DepartmentService#createDepartment(com.techlords
	 * .crown.business.model.DepartmentBO)
	 */
	@Transactional
	@Override
	public boolean createDepartment(DepartmentBO departmentBO, int userID) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.DepartmentService#editDepartment(com.techlords
	 * .crown.business.model.DepartmentBO)
	 */
	@Transactional
	@Override
	public boolean editDepartment(DepartmentBO departmentBO, int userID) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.DepartmentService#findAllDepartments()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DepartmentBO> findAllDepartments() {
		final List<Department> departments = manager.createQuery(
				"select d from Department d order by d.departmentName").getResultList();
		final List<DepartmentBO> bos = new ArrayList<DepartmentBO>();
		final DepartmentHelper helper = new DepartmentHelper();
		for (final Department dept : departments) {
			bos.add(helper.createDepartmentBO(dept));
		}
		return bos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.DepartmentService#deleteDepartment(com.techlords
	 * .crown.business.model.DepartmentBO)
	 */
	@Transactional
	@Override
	public boolean deleteDepartment(DepartmentBO departmentBO, int userID) {
		return false;
	}

}
