package com.techlords.crown.service;

import java.util.Date;
import java.util.List;

import com.techlords.crown.business.model.CrownAuditBO;
import com.techlords.crown.persistence.CrownUser;

public interface AuditService extends CrownService {
	void createAuditLog(int action, CrownUser user, String description,
			String tableName);

	List<CrownAuditBO> getAllAuditReport();

	List<CrownAuditBO> getAllAuditReport(Date fromDate, Date toDate);
}
