/**
 * 
 */
package com.techlords.crown.service;

import java.util.List;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.BankBO;
import com.techlords.crown.business.model.CrownEntityBO;
import com.techlords.crown.business.model.CurrencyBO;
import com.techlords.crown.business.model.TotalStockBO;
import com.techlords.crown.business.model.UomBO;
import com.techlords.crown.persistence.Bank;

/**
 * @author gv
 * 
 */
public interface GeneralService extends CrownService {
	List<CurrencyBO> findAllCurrencies();

	List<UomBO> findAllUnitOfMeasures();

	CurrencyBO findCurrencyBO(String currencyCode);

	/**
	 * @return only the wholesale entities
	 */
	List<CrownEntityBO> findAllWholesaleEntities();

	/**
	 * @return only the retail entities
	 */
	List<CrownEntityBO> findAllRetailEntities();
	
	List<CrownEntityBO> findAllRetailShops();

	TotalStockBO getTotalAvailableQuantity(int itemID) throws CrownException;

	List<TotalStockBO> getAllocationAvailability(Integer entityID,
			Object... itemIDs);
	
	List<TotalStockBO> getInvoiceAvailability(Integer entityID,
			Object... itemIDs);

	String getRunningSequenceNumber(String tableName, String dateColumn);

	Bank findBank(Object id);

	List<BankBO> findAllBanks();
	
	boolean isAvailable(String tableName, String columnName, String value);
}
