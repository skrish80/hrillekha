/**
 * 
 */
package com.techlords.crown.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.techlords.crown.business.model.BankBO;
import com.techlords.crown.business.model.CrownAuditBO;
import com.techlords.crown.business.model.CrownEntityBO;
import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.business.model.CurrencyBO;
import com.techlords.crown.business.model.SearchCriteria;
import com.techlords.crown.business.model.TotalStockBO;
import com.techlords.crown.business.model.UomBO;
import com.techlords.crown.business.model.enums.AllocationStateBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.business.model.enums.MoveStatusBO;
import com.techlords.crown.business.model.enums.PaymentStatusBO;
import com.techlords.crown.persistence.Bank;
import com.techlords.crown.persistence.CrownAudit;
import com.techlords.crown.persistence.CrownEntity;
import com.techlords.crown.persistence.CrownUser;
import com.techlords.crown.persistence.Currency;
import com.techlords.crown.persistence.TotalStock;
import com.techlords.crown.persistence.UnitOfMeasure;

/**
 * @author gv
 * 
 */
public final class GeneralHelper {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd-MMM-yyyy");
	private static final DateFormat RUNNING_DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");

	public CurrencyBO createCurrencyBO(Currency currency) {
		final CurrencyBO bo = new CurrencyBO();
		bo.setVersion(currency.getVersion());
		bo.setCurrencyCode(currency.getCurrencyCode());
		bo.setDescription(currency.getDescription());

		return bo;
	}

	public UomBO createUomBO(UnitOfMeasure uom) {
		final UomBO bo = new UomBO();
		bo.setId(uom.getUomId());
		bo.setVersion(uom.getVersion());
		bo.setUom(uom.getUomName());
		bo.setDescription(uom.getDescription());

		return bo;
	}

	public BankBO createBankBO(Bank bank) {
		final BankBO bo = new BankBO();
		bo.setId(bank.getBankId());
		bo.setVersion(bank.getVersion());
		bo.setBankCode(bank.getBankCode());
		bo.setBankName(bank.getBankName());
		bo.setDescription(bank.getDescription());
		return bo;
	}

	public Bank createBank(BankBO bo) {
		return createBank(bo, null);
	}

	public Bank createBank(BankBO bo, Bank toEdit) {
		final Bank bank = (toEdit == null) ? new Bank() : toEdit;
		if (toEdit == null) {
			bank.setBankCode(bo.getBankCode());
		}
		bank.setVersion(bo.getVersion());
		bank.setBankName(bo.getBankName());
		bank.setDescription(bo.getDescription());
		return bank;
	}

	public CrownEntityBO createCrownEntityBO(CrownEntity entity) {
		final CrownEntityBO bo = new CrownEntityBO();
		bo.setId(entity.getEntityId());
		bo.setVersion(entity.getVersion());
		bo.setEntity(entity.getEntityName());
		bo.setDescription(entity.getDescription());
		bo.setEntityType(entity.getEntityType());
		return bo;
	}

	//QUERIES
	public String buildStockAllocationQuery(SearchCriteria searchCriteria) {
		final StringBuilder builder = new StringBuilder();

		final QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.addQuery("ENTITY", searchCriteria.getCrownEntityID());
		queryBuilder.addQuery("ITEM_ID", searchCriteria.getItemID());
		queryBuilder.addJoinQuery("ITEM_ID",
				searchCriteria.getItemCategoryID(), "ITEM", "ITEM_CATEGORY");
		queryBuilder.addJoinQuery("ITEM_ID", searchCriteria.getItemBrandID(),
				"ITEM", "ITEM_BRAND");

		builder.append("SELECT * FROM STOCK_ALLOCATION SA WHERE ");
		builder.append(queryBuilder.getQuery());

		return builder.toString();
	}

	public String buildTotalAvailableItemQuery(Object... itemIDs) {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM TOTAL_STOCK TS WHERE ");

		final QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.addInQuery("ITEM_ID", itemIDs);

		builder.append(queryBuilder.getQuery());

		return builder.toString();
	}

	public String buildInvoiceStateQuery(String invoiceType,
			Object... invoiceStates) {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM INVOICE INV WHERE INV.INVOICE_TYPE = '"
				+ invoiceType + "' AND ");

		final QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.addInQuery("INVOICE_STATE", invoiceStates);

		builder.append(queryBuilder.getQuery());

		return builder.toString();
	}

	public String buildPaymentInvoiceQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM INVOICE INV WHERE ");

		final QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.addQuery("INVOICE_STATE",
				InvoiceStateBO.CANCELLED.getStateID(), true);
		queryBuilder.addInQuery("PAYMENT_STATUS",
				PaymentStatusBO.CREDIT_SALES.getStatusID(),
				PaymentStatusBO.PARTIAL_PAYMENT.getStatusID());

		builder.append(queryBuilder.getQuery());

		return builder.toString();
	}

	public String buildWarehouseStockQuery(int warehouseID, Object... itemIDs) {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM WAREHOUSE_STOCK WS WHERE ");

		final QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.addQuery("WAREHOUSE_ID", warehouseID);
		queryBuilder.addInQuery("ITEM_ID", itemIDs);

		builder.append(queryBuilder.getQuery());

		return builder.toString();
	}

	public String buildMovedStockQuery(int warehouseID, Object... itemIDs) {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM STOCK_MOVEMENT_ITEMS WHERE ");
		// SELECT * FROM STOCK_MOVEMENT_ITEMS WHERE ITEM_ID IN (1) AND
		// MOVEMENT_ID IN (SELECT MOVEMENT_ID FROM STOCK_MOVEMENT WHERE
		// MOVE_STATUS=1)
		final QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.addInQuery("ITEM_ID", itemIDs);

		builder.append(queryBuilder.getQuery());
		builder.append(" AND MOVEMENT_ID IN (SELECT MOVEMENT_ID FROM STOCK_MOVEMENT WHERE FROM_WAREHOUSE = "
				+ warehouseID
				+ " AND MOVE_STATUS = "
				+ MoveStatusBO.MOVED.getMoveStatusID() + ")");
		return builder.toString();
	}

	public String buildUnavailableEntityStockQuery(Integer entityID,
			Object... itemIDs) {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT ITEM_ID, SUM(ALLOCATION_QTY) AS ALLOCATION_QTY, ALLOCATION_TYPE FROM STOCK_ALLOCATION WHERE ");

		final QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.addQuery("ENTITY", entityID, true);
		queryBuilder.addInQuery("ITEM_ID", itemIDs);
		queryBuilder.addQuery("STATE",
				AllocationStateBO.ALLOCATED.getAllocationStateID());
		queryBuilder.addGroupByClause("ITEM_ID", "ALLOCATION_TYPE");

		builder.append(queryBuilder.getQuery());

		return builder.toString();
	}

	public String buildTotalAllocatedItemQuery(int itemID) {
		final StringBuilder builder = new StringBuilder();
		builder.append("select allocation_type, sum(allocation_qty) as allocation_qty from stock_allocation where ");

		final QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.addQuery("ITEM_ID", itemID);

		builder.append(queryBuilder.getQuery());
		builder.append(" group by allocation_type");

		return builder.toString();
	}

	public String buildRunningSeqNumberQuery(String tableName, String dateColumn) {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(*) FROM " + tableName);
		if (dateColumn != null) {
			builder.append(" WHERE " + dateColumn + " = '"
					+ DATE_FORMAT.format(new Date()) + "'");
		}
		return builder.toString();
	}

	public String buildRunningSeqNumberQuery(String tableName) {
		return buildRunningSeqNumberQuery(tableName, null);
	}

	public String buildUniqueFieldQuery(String tableName, String columnName,
			String value) {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(*) FROM " + tableName + " WHERE ");
		builder.append(columnName + " = '" + value + "'");
		return builder.toString();
	}

	public TotalStockBO createTotalStockBO(TotalStock stock) {
		final TotalStockBO bo = new TotalStockBO();
		bo.setId(stock.getItemId());
		bo.setItemID(stock.getItemId());
		bo.setUomID(stock.getUom());
		bo.setItemQty(stock.getItemQty());
		bo.setUomQty(stock.getUomQty());
		return bo;
	}

	public CrownAuditBO createCrownAuditBO(CrownAudit audit) {
		final CrownAuditBO bo = new CrownAuditBO();
		bo.setId(audit.getAuditId());
		bo.setVersion(audit.getVersion());
		bo.setAction(AuditActionEnum.valueOf(
				audit.getAuditAction().getActionId()).getActionName());
		final CrownUser user = audit.getCrownUser();
		bo.setCrownUser(user.getFirstName() + " " + user.getLastName());
		bo.setTableName(audit.getTableName());
		bo.setTimestamp(audit.getActionTime().toString());
		bo.setDescription(audit.getDescription());
		return bo;
	}

	public static final String getFormattedRunningDate() {
		return RUNNING_DATE_FORMAT.format(new Date());
	}

	public String buildUndeliveredInvoiceItemsQuery(Integer entityID,
			Object... itemIDs) {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT ITEM_ID, SUM(ITEM_QTY-DELIVERED_QTY), ALLOCATION_TYPE FROM INVOICE_ITEMS WHERE ");
		final QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.addInQuery("ITEM_ID", itemIDs);
		queryBuilder.addJoinInQuery(
				"INVOICE_ID",
				"INVOICE",
				"INVOICE_STATE",
				// (entityID!= null) ?" AND ENTITY_ID = "+entityID : //DON'T
				// PASS ENTITY SPECIFIC
				null,// pass the entity id
				InvoiceStateBO.NEW.getStateID(),
				InvoiceStateBO.PRINTED.getStateID(),
				InvoiceStateBO.PARTIAL_DELIVERY.getStateID());
		queryBuilder.addGroupByClause("ITEM_ID", "ALLOCATION_TYPE");

		builder.append(queryBuilder.getQuery());
		return builder.toString();
	}

	public CrownUserBO getCrownUserBO(CrownUser user) {
		final CrownUserBO userBO = new CrownUserBO();
		if (user != null) {
			userBO.setId(user.getUserId());
			userBO.setVersion(user.getVersion());
			userBO.setUsername(user.getUsername());
			userBO.setEmployeeNo(user.getEmployeeNo());
			userBO.setFirstName(user.getFirstName());
			userBO.setLastName(user.getLastName());
		}
		return userBO;
	}

	public static void main(String[] args) {
		List<Boolean> flags = new ArrayList<Boolean>();
		for (int i = 0; i < 5; i++) {
			flags.add(i % 2 == 0);
		}
		for (boolean flag : flags) {
			System.err.println(flag);
		}

		System.out.println(flags.contains(true));
	}
}
