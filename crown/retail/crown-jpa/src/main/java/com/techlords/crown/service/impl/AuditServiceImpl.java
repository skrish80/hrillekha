/**
 * 
 */
package com.techlords.crown.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import org.springframework.transaction.annotation.Transactional;

import com.techlords.crown.business.model.CrownAuditBO;
import com.techlords.crown.helpers.GeneralHelper;
import com.techlords.crown.persistence.AuditAction;
import com.techlords.crown.persistence.CrownAudit;
import com.techlords.crown.persistence.CrownUser;
import com.techlords.crown.service.AuditService;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
final class AuditServiceImpl implements AuditService {

	protected EntityManager manager;

	/**
	 * @return the manager
	 */
	public EntityManager getManager() {
		return manager;
	}

	/**
	 * @param manager
	 *            the manager to set
	 */
	@PersistenceContext
	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	@Transactional
	@Override
	public void createAuditLog(int action, CrownUser user, String description, String tableName) {
		CrownAudit audit = new CrownAudit();
		audit.setAuditAction(manager.find(AuditAction.class, action));
		audit.setCrownUser(user);
		audit.setDescription(description);
		audit.setTableName(tableName);
		audit.setActionTime(new Timestamp(System.currentTimeMillis()));

		manager.persist(audit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrownAuditBO> getAllAuditReport() {
		final List<CrownAuditBO> bos = new ArrayList<CrownAuditBO>();
		final GeneralHelper helper = new GeneralHelper();

		List<CrownAudit> audits = manager.createNamedQuery("CrownAudit.findAllAudit")
				.setParameter(1, "Login").getResultList();
		for (CrownAudit audit : audits) {
			bos.add(helper.createCrownAuditBO(audit));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrownAuditBO> getAllAuditReport(Date fromDate, Date toDate) {
		final List<CrownAuditBO> bos = new ArrayList<CrownAuditBO>();
		final GeneralHelper helper = new GeneralHelper();
		List<CrownAudit> audits = manager.createNamedQuery("CrownAudit.findAllAuditRange")
				.setParameter(1, "Login").setParameter(2, fromDate, TemporalType.DATE)
				.setParameter(3, toDate, TemporalType.DATE).getResultList();
		for (CrownAudit audit : audits) {
			bos.add(helper.createCrownAuditBO(audit));
		}
		return bos;
	}

}
