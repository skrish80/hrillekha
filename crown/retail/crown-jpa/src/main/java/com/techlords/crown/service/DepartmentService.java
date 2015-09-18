/**
 * 
 */
package com.techlords.crown.service;

import java.util.List;

import com.techlords.crown.business.model.DepartmentBO;

/**
 * @author gv
 * 
 */
public interface DepartmentService extends CrownService {

	public boolean createDepartment(DepartmentBO departmentBO, int userID);

	public boolean editDepartment(DepartmentBO departmentBO, int userID);

	public List<DepartmentBO> findAllDepartments();

	public boolean deleteDepartment(DepartmentBO departmentBO, int userID);
}
