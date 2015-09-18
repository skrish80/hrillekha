package com.techlords.crown.service;

import java.util.List;
import java.util.Map;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CreditNoteBO;
import com.techlords.crown.business.model.InvoiceBO;
import com.techlords.crown.business.model.InvoiceItemBO;
import com.techlords.crown.business.model.InvoicePaymentBO;

public interface InvoiceService extends CrownService {

	// INVOICE
	InvoiceBO createInvoice(InvoiceBO bo, int userID) throws CrownException;

	void printInvoice(InvoiceBO bo, int userID) throws CrownException;

	boolean deliverInvoice(InvoiceBO bo, int userID) throws CrownException;

	boolean returnInvoice(InvoiceBO bo, int userID) throws CrownException;

	boolean updateInvoicePayments(InvoiceBO bo,
			List<InvoicePaymentBO> removedCheques, int userID)
			throws CrownException;

	boolean cancelInvoice(InvoiceBO bo, int userID) throws CrownException;

	List<InvoiceBO> findAllInvoices(String invoiceType);

	List<InvoiceBO> findAllInvoices(String invoiceType, Object... invoiceStates);

	List<InvoiceBO> findPaymentPendingInvoices();

	List<InvoiceItemBO> findUndeliveredInvoiceItems(Integer entityID,
			Object... itemIDs);

	boolean createCreditNote(CreditNoteBO bo, int userID) throws CrownException;

	boolean issueCreditNote(int creditNoteID, int userID) throws CrownException;

	List<CreditNoteBO> isCreditNoteUtilized(String draftNumber);

	void removeInvoicePayment(String chequeNumber) throws CrownException;

	/**
	 * FILTERING & LAZY LOADING add the invoice code(WS or RT) for wholesale or
	 * retail
	 */
	List<InvoiceBO> findAllInvoices(String invoiceType, int first,
			int pageSize, Map<String, String> filters, boolean isAmendPayment,
			int entity, Object... invoiceStates);

	List<InvoiceBO> findAllInvoices(String invoiceType, int first,
			int pageSize, boolean isAmendPayment, int entity,
			Object... invoiceStates);

	int findInvoiceCount(String invoiceType);

	int getFilteredInvoiceCount();

	/**
	 * BULK ITEMS
	 */
	int getFilteredBulkItemsCount();

	List<InvoiceItemBO> findBulkInvoiceItems(Integer brand, Integer category);

}
