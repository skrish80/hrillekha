/**
 * 
 */
package com.techlords.crown.service;

import java.util.List;
import java.util.Map;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.PurchaseInvoiceBO;
import com.techlords.crown.business.model.PurchaseInvoiceItemBO;
import com.techlords.crown.business.model.SupplierBO;

/**
 * @author gv
 * 
 */
public interface PurchaseInvoiceService extends CrownService {

	public PurchaseInvoiceBO createInvoice(PurchaseInvoiceBO invoiceBO,
			int userID) throws CrownException;

	public boolean receiveInvoice(PurchaseInvoiceBO invoiceBO, int userID)
			throws CrownException;

	public boolean cancelInvoice(PurchaseInvoiceBO invoiceBO, int userID)
			throws CrownException;

	public boolean printInvoice(PurchaseInvoiceBO invoiceBO, int userID)
			throws CrownException;

	public List<PurchaseInvoiceBO> findAllInvoices();

	public boolean createSupplier(SupplierBO supplierBO, int userID)
			throws CrownException;

	public boolean updateSupplier(SupplierBO supplierBO, int userID)
			throws CrownException;

	public boolean deleteSupplier(SupplierBO supplierBO, int userID)
			throws CrownException;

	public List<SupplierBO> findAllSuppliers();

	public int getFilteredInvoiceCount();

	public List<PurchaseInvoiceBO> findAllInvoices(int first, int pageSize,
			Map<String, Object> filters, String invoiceState);

	public List<PurchaseInvoiceBO> findAllInvoices(int first, int pageSize,
			String invoiceState);

	public List<PurchaseInvoiceItemBO> findBulkInvoiceItems(Integer brand,
			Integer category);

}
