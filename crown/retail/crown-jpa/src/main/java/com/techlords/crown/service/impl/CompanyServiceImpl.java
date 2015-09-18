package com.techlords.crown.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.springframework.transaction.annotation.Transactional;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CompanyBO;
import com.techlords.crown.business.model.CompanyTypeBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.helpers.AuditActionEnum;
import com.techlords.crown.helpers.CompanyHelper;
import com.techlords.crown.persistence.Company;
import com.techlords.crown.persistence.CompanyType;
import com.techlords.crown.persistence.Location;
import com.techlords.crown.persistence.Status;
import com.techlords.crown.service.CompanyService;

@SuppressWarnings("serial")
final class CompanyServiceImpl extends AbstractCrownService implements
		CompanyService {

	private void setCompanyAttributes(Company company, CompanyBO bo) {
		company.setLocationBean(manager.find(Location.class,
				bo.getLocationBO().getId()));
		company.setCompanyTypeBean(manager.find(CompanyType.class,
				bo.getCompanyTypeBO().getId()));
		company.setStatusBean(manager.find(Status.class,
				StatusBO.ACTIVE.getStatusID()));
		company.setVersion(bo.getVersion());
	}

	@Override
	@Transactional
	public boolean createCompany(CompanyBO companyBO, int userID)
			throws CrownException {
		try {
			final CompanyHelper helper = new CompanyHelper();
			final Company company = helper.createCompany(companyBO);
			setCompanyAttributes(company, companyBO);
			manager.persist(company);

			auditLog(AuditActionEnum.CREATE, userID, "company",
					company.getCompanyName());
		} catch (Exception e) {
			throw new CrownException("Company cannot be created", e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean updateCompany(CompanyBO companyBO, int userID)
			throws CrownException {
		try {
			CompanyHelper helper = new CompanyHelper();
			Company company = helper.createCompany(companyBO, manager
					.find(Company.class, companyBO.getId()));
			setCompanyAttributes(company, companyBO);
			manager.merge(company);

			auditLog(AuditActionEnum.UPDATE, userID, "company",
					company.getCompanyName());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Company has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean deleteCompany(CompanyBO companyBO, int userID)
			throws CrownException {
		try {
			Company company = manager.find(Company.class,
					companyBO.getId());
			company.setVersion(companyBO.getVersion());
			company.setStatusBean(manager.find(Status.class,
					StatusBO.DISABLED.getStatusID()));
			manager.merge(company);

			auditLog(AuditActionEnum.DELETE, userID, "company",
					company.getCompanyName());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Agent has changed or been deleted since it was last read",
					e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CompanyBO> findAllCompanies() {
		final List<Company> companies = manager.createNamedQuery(
				"Company.findActiveCompanies").getResultList();
		final List<CompanyBO> companyBOs = new ArrayList<CompanyBO>();
		final CompanyHelper helper = new CompanyHelper();
		for (final Company company : companies) {
			companyBOs.add(helper.createCompanyBO(company));
		}
		return companyBOs;
	}

	@Override
	public List<CompanyBO> searchCompanies(String companyName,
			CompanyTypeBO companyType) {
		manager.createQuery("select c from Company c order by c.companyName").getResultList();
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CompanyTypeBO> searchCompanyTypes() {
		final List<CompanyType> companyTypes = manager.createQuery(
				"select t from CompanyType t").getResultList();
		final List<CompanyTypeBO> bos = new ArrayList<CompanyTypeBO>();
		final CompanyHelper helper = new CompanyHelper();
		for (final CompanyType type : companyTypes) {
			bos.add(helper.createCompanyTypeBO(type));
		}
		return bos;
	}
}
