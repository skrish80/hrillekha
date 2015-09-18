/**
 * 
 */
package com.techlords.crown.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.BankBO;
import com.techlords.crown.business.model.CrownEntityBO;
import com.techlords.crown.business.model.CurrencyBO;
import com.techlords.crown.business.model.InvoiceItemBO;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.StockAllocationBO;
import com.techlords.crown.business.model.TotalStockBO;
import com.techlords.crown.business.model.UomBO;
import com.techlords.crown.business.model.enums.AllocationStateBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.helpers.GeneralHelper;
import com.techlords.crown.persistence.AllocationType;
import com.techlords.crown.persistence.Bank;
import com.techlords.crown.persistence.CrownEntity;
import com.techlords.crown.persistence.Currency;
import com.techlords.crown.persistence.Item;
import com.techlords.crown.persistence.StockAllocation;
import com.techlords.crown.persistence.TotalStock;
import com.techlords.crown.persistence.UnitOfMeasure;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.InvoiceService;
import com.techlords.crown.service.StockService;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
class GeneralServiceImpl extends AbstractCrownService implements GeneralService {
	private final GeneralHelper helper = new GeneralHelper();
	private final NumberFormat numberFormat = new DecimalFormat("####");

	public GeneralServiceImpl() {
		numberFormat.setMinimumIntegerDigits(4);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.GeneralService#findAllCurrencies()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CurrencyBO> findAllCurrencies() {
		final List<CurrencyBO> bos = new ArrayList<CurrencyBO>();

		final List<Currency> currencies = manager.createQuery(
				"Select C from Currency C order by C.currencyCode").getResultList();

		for (final Currency cur : currencies) {
			bos.add(helper.createCurrencyBO(cur));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UomBO> findAllUnitOfMeasures() {
		final List<UomBO> bos = new ArrayList<UomBO>();

		final List<UnitOfMeasure> uoms = manager.createQuery(
				"Select U from UnitOfMeasure U order by U.uomName").getResultList();

		for (final UnitOfMeasure uom : uoms) {
			bos.add(helper.createUomBO(uom));
		}
		return bos;
	}

	@Override
	public CurrencyBO findCurrencyBO(String currencyCode) {
		Currency curr = (Currency) manager.createNamedQuery("Currency.findByCode")
				.setParameter(1, currencyCode).getSingleResult();
		return helper.createCurrencyBO(curr);
	}

	@Override
	public List<CrownEntityBO> findAllWholesaleEntities() {
		return findAllCrownEntities("WS");
	}

	@SuppressWarnings("unchecked")
	private List<CrownEntityBO> findAllCrownEntities(String entityType) {
		final List<CrownEntityBO> bos = new ArrayList<CrownEntityBO>();

		final List<CrownEntity> entities = manager.createQuery(
				"Select CE from CrownEntity CE where CE.entityType = '" + entityType + "'")
				.getResultList();

		for (final CrownEntity entity : entities) {
			bos.add(helper.createCrownEntityBO(entity));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrownEntityBO> findAllRetailEntities() {
		final List<CrownEntityBO> bos = new ArrayList<CrownEntityBO>();

		final List<CrownEntity> entities = manager.createQuery(
				"Select CE from CrownEntity CE where CE.entityType = 'RT' AND CE.entityId = 1")
				.getResultList();

		for (final CrownEntity entity : entities) {
			bos.add(helper.createCrownEntityBO(entity));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrownEntityBO> findAllRetailShops() {
		final List<CrownEntityBO> bos = new ArrayList<CrownEntityBO>();

		final List<CrownEntity> entities = manager.createQuery(
				"Select CE from CrownEntity CE where CE.entityType = 'RT' AND CE.entityId <> 1")
				.getResultList();

		for (final CrownEntity entity : entities) {
			bos.add(helper.createCrownEntityBO(entity));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TotalStockBO getTotalAvailableQuantity(int itemID) throws CrownException {
		final String stockAllocationQuery = helper.buildTotalAvailableItemQuery(itemID);
		Query query = getNativeQuery(stockAllocationQuery, TotalStock.class);
		TotalStockBO bo = new TotalStockBO();
		try {
			query.setMaxResults(1);
			List<TotalStock> stocks = query.getResultList();
			if (!stocks.isEmpty()) {
				TotalStock totalStock = stocks.get(0);
				bo = getTotalStockBOAgainstAllocation(totalStock, itemID);
				Item item = manager.find(Item.class, totalStock.getItemId());
				final ItemBO itemBO = new ItemBO();

				itemBO.setId(item.getItemId());
				itemBO.setItemCode(item.getItemCode());
				itemBO.setItemName(item.getItemName());
				bo.setItemBO(itemBO);
			}

		} catch (NoResultException e) {
			// do nothing; we don't have any result
			e.printStackTrace();
			throw new CrownException("Item doesn't exist in Stock - cannot be allocated");
		}
		return bo;
	}

	public List<TotalStock> subtractAllocatedStock(List<TotalStock> existingStocks,
			Integer entityID, Object... itemIDs) {
		StockService stockService = CrownServiceLocator.INSTANCE
				.getCrownService(StockService.class);
		final List<StockAllocationBO> allocations = stockService.findUnavailableStockForEntity(
				entityID, itemIDs);

		for (TotalStock stock : existingStocks) {
			for (StockAllocationBO allocation : allocations) {
				if (stock.getItemId().equals(allocation.getItem())) {
					if (allocation.getAllocationType() == AllocationTypeBO.UOM
							.getAllocationTypeID()) {
						Long uomQty = stock.getUomQty();
						uomQty -= allocation.getAllocatedQty();
						stock.setUomQty(uomQty);
					}
					if (allocation.getAllocationType() == AllocationTypeBO.ITEM
							.getAllocationTypeID()) {
						Long itemQty = stock.getItemQty();
						itemQty -= allocation.getAllocatedQty();
						stock.setItemQty(itemQty);
					}
				}
			}
		}
		return existingStocks;
	}

	public List<TotalStock> subtractUndeliveredStock(List<TotalStock> existingStocks,
			Integer entityID, Object... itemIDs) {
		final InvoiceService invoiceService = CrownServiceLocator.INSTANCE
				.getCrownService(InvoiceService.class);
		final List<InvoiceItemBO> items = invoiceService.findUndeliveredInvoiceItems(entityID,
				itemIDs);

		for (TotalStock stock : existingStocks) {
			for (InvoiceItemBO itmBO : items) {
				if (stock.getItemId().equals(itmBO.getItem())) {
					if (itmBO.getAllocationType() == AllocationTypeBO.UOM.getAllocationTypeID()) {
						Long uomQty = stock.getUomQty();
						uomQty -= itmBO.getItemQty();
						stock.setUomQty(uomQty);
					}
					if (itmBO.getAllocationType() == AllocationTypeBO.ITEM.getAllocationTypeID()) {
						Long itemQty = stock.getItemQty();
						itemQty -= itmBO.getItemQty();
						stock.setItemQty(itemQty);
					}
				}
			}
		}
		return existingStocks;
	}

	public List<TotalStockBO> getTotalAvdailableQuantityForAllocation(Integer entityID,
			Object... itemIDs) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TotalStockBO> getAllocationAvailability(Integer entityID, Object... itemIDs) {
		final String stockAllocationQuery = helper.buildTotalAvailableItemQuery(itemIDs);
		Query query = getNativeQuery(stockAllocationQuery, TotalStock.class);
		final List<TotalStockBO> bos = new ArrayList<TotalStockBO>();
		List<TotalStock> stocks = query.getResultList();

		// this is for stock allocation, don't pass entity id
		stocks = subtractAllocatedStock(stocks, null, itemIDs);
		stocks = subtractUndeliveredStock(stocks, null, itemIDs);

		for (TotalStock stock : stocks) {
			TotalStockBO bo = helper.createTotalStockBO(stock);
			Item item = manager.find(Item.class, stock.getItemId());
			final ItemBO itemBO = new ItemBO();

			itemBO.setId(item.getItemId());
			itemBO.setItemCode(item.getItemCode());
			itemBO.setItemName(item.getItemName());
			bo.setItemBO(itemBO);
			bos.add(bo);
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TotalStockBO> getInvoiceAvailability(Integer entityID, Object... itemIDs) {
		final String stockAllocationQuery = helper.buildTotalAvailableItemQuery(itemIDs);
		Query query = getNativeQuery(stockAllocationQuery, TotalStock.class);
		final List<TotalStockBO> bos = new ArrayList<TotalStockBO>();
		List<TotalStock> stocks = query.getResultList();

		stocks = subtractAllocatedStock(stocks, entityID, itemIDs);
		stocks = subtractUndeliveredStock(stocks, entityID, itemIDs);

		for (TotalStock stock : stocks) {
			TotalStockBO bo = helper.createTotalStockBO(stock);
			Item item = manager.find(Item.class, stock.getItemId());
			final ItemBO itemBO = new ItemBO();

			itemBO.setId(item.getItemId());
			itemBO.setItemCode(item.getItemCode());
			itemBO.setItemName(item.getItemName());
			bo.setItemBO(itemBO);
			bos.add(bo);
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	private TotalStockBO getTotalStockBOAgainstAllocation(TotalStock stock, int itemID) {
		TotalStockBO bo = helper.createTotalStockBO(stock);
		List<StockAllocation> allocatedStocks = manager
				.createNamedQuery("StockAllocation.findByAllocatedItemID").setParameter(1, itemID)
				.setParameter(2, AllocationStateBO.ALLOCATED.getAllocationStateID())
				.getResultList();
		if (allocatedStocks != null && !allocatedStocks.isEmpty()) {
			for (StockAllocation sa : allocatedStocks) {
				AllocationType type = sa.getAllocationTypeBean();
				if (type.getAllocationTypeId() == AllocationTypeBO.UOM.getAllocationTypeID()) {
					Long uomQty = bo.getUomQty();
					if (uomQty != null) {
						uomQty -= sa.getAllocationQty();
						bo.setUomQty(uomQty);
					}
				} else if (type.getAllocationTypeId() == AllocationTypeBO.ITEM
						.getAllocationTypeID()) {
					Long itemQty = bo.getItemQty();
					if (itemQty != null) {
						itemQty -= sa.getAllocationQty();
						bo.setItemQty(itemQty);
					}
				}
			}
		}
		return bo;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getRunningSequenceNumber(String tableName, String dateColumn) {
		final String seqNumQuery = new GeneralHelper().buildRunningSeqNumberQuery(tableName,
				dateColumn);
		Query query = getNativeQuery(seqNumQuery);
		query.setMaxResults(1);
		Long returnValue = 0L;
		List<Long> seqNums = query.getResultList();
		if (!seqNums.isEmpty()) {
			returnValue = seqNums.get(0);
		}
		return numberFormat.format(returnValue + 1);
	}

	@Override
	public Bank findBank(Object id) {
		return manager.find(Bank.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BankBO> findAllBanks() {
		final List<BankBO> bos = new ArrayList<BankBO>();

		final List<Bank> banks = manager.createQuery("Select B from Bank B order by B.bankName")
				.getResultList();

		for (final Bank bank : banks) {
			bos.add(helper.createBankBO(bank));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isAvailable(String tableName, String columnName, String value) {
		final String seqNumQuery = new GeneralHelper().buildUniqueFieldQuery(tableName, columnName,
				value);
		Query query = getNativeQuery(seqNumQuery);
		query.setMaxResults(1);
		Long returnValue = 0L;
		List<Long> seqNums = query.getResultList();
		if (!seqNums.isEmpty()) {
			returnValue = seqNums.get(0);
		}
		return returnValue == 0L;
	}
}
