/**
 * 
 */
package com.techlords.crown.service.impl;

import java.text.MessageFormat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.util.StringUtils;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.helpers.AuditActionEnum;
import com.techlords.crown.persistence.CrownUser;
import com.techlords.crown.service.AuditService;
import com.techlords.crown.service.CrownService;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractCrownService implements CrownService {
	private static final String DOT = ".";
	private static final String MSG = "msg";

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

	/**
	 * 
	 * @param action
	 * @param userID
	 * @param msgPrefix
	 * @param auditItem
	 *            the item that is to be audited
	 */
	protected void auditLog(AuditActionEnum action, int userID, String msgPrefix, String auditItem) {
		final AuditService auditService = CrownServiceLocator.INSTANCE
				.getCrownService(AuditService.class);
		final CrownUser user = manager.find(CrownUser.class, userID);

		final String descriptionKey = msgPrefix + DOT + action.name().toLowerCase() + DOT + MSG;

		final String description = MessageFormat.format(AuditMessages.getString(descriptionKey),
				auditItem);
		auditService.createAuditLog(action.getActionID(), user, description,
				StringUtils.capitalize(msgPrefix));
	}

	protected final Query getNativeQuery(String nativeQuery) {
		Query query = manager.createNativeQuery(nativeQuery);
		return query;
	}

	protected final Query getNativeQuery(String nativeQuery, Class<?> resultclass) {
		Query query = manager.createNativeQuery(nativeQuery, resultclass);
		return query;
	}
}
