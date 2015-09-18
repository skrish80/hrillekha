/**
 * 
 */
package com.techlords.crown.helpers;

import com.techlords.crown.business.model.DepartmentBO;
import com.techlords.crown.persistence.Department;

/**
 * @author gv
 * 
 */
public final class DepartmentHelper {

	public DepartmentBO createDepartmentBO(Department department) {
		final DepartmentBO bo = new DepartmentBO();
		bo.setId(department.getDepartmentId());
		bo.setVersion(department.getVersion());
		bo.setDepartment(department.getDepartmentName());
		bo.setDescription(department.getDescription());
		return bo;
	}

	public Department createDepartment(DepartmentBO bo) {
		return createDepartment(bo, null);
	}

	public Department createDepartment(DepartmentBO bo, final Department toEdit) {
		final Department department = (toEdit == null) ? new Department()
				: toEdit;
		department.setVersion(bo.getVersion());
		department.setDepartmentName(bo.getDepartment());
		department.setDescription(bo.getDescription());
		return department;
	}
}
