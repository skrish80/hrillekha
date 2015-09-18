/**
 * 
 */
package com.techlords.crown.service;

import java.util.List;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CompanyBO;
import com.techlords.crown.business.model.CompanyTypeBO;

/**
 * @author gv
 * 
 */
public interface CompanyService extends CrownService {

	/**
	 * adds a company to the application
	 */
	public boolean createCompany(CompanyBO companyBO, int userID) throws CrownException;

	public boolean updateCompany(CompanyBO companyBO, int userID) throws CrownException;

	public List<CompanyBO> findAllCompanies();

	public List<CompanyBO> searchCompanies(String companyName,
			CompanyTypeBO companyType);

	public List<CompanyTypeBO> searchCompanyTypes();

	public boolean deleteCompany(CompanyBO companyBO, int userID) throws CrownException;
}
