/**
 * 
 */
package com.techlords.crown.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.ItemBrandBO;
import com.techlords.crown.business.model.ItemCategoryBO;
import com.techlords.crown.business.model.enums.StatusBO;
import com.techlords.crown.helpers.AuditActionEnum;
import com.techlords.crown.helpers.GeneralHelper;
import com.techlords.crown.helpers.ItemHelper;
import com.techlords.crown.persistence.Currency;
import com.techlords.crown.persistence.Item;
import com.techlords.crown.persistence.ItemBrand;
import com.techlords.crown.persistence.ItemCategory;
import com.techlords.crown.persistence.Status;
import com.techlords.crown.persistence.UnitOfMeasure;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.ItemService;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
final class ItemServiceImpl extends AbstractCrownService implements ItemService {
	private static final String ITEM_PREFIX = "ITM";
	private final ItemHelper helper = new ItemHelper();
	private int filteredItemCount;

	private void setItemAttributes(Item item, ItemBO bo) {
		item.setCurrencyBean((Currency) manager.createNamedQuery("Currency.findByCode")
				.setParameter(1, bo.getCurrency()).getSingleResult());
		item.setItemCategoryBean(manager.find(ItemCategory.class, bo.getCategory()));
		item.setItemBrandBean(manager.find(ItemBrand.class, bo.getBrand()));
		item.setUnitOfMeasure(manager.find(UnitOfMeasure.class, bo.getUom()));
		item.setStatusBean(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));
		item.setVersion(bo.getVersion());
	}

	public ItemBO getItemBO(int id) {
		final Item item = manager.find(Item.class, id);
		if (item == null) {
			return null;
		}
		return helper.createItemBO(item);
	}

	@Transactional
	@Override
	public boolean createItemCategory(ItemCategoryBO bo, int userID) throws CrownException {
		try {
			final ItemCategory category = helper.createItemCategory(bo);
			manager.persist(category);
			category.setStatusBean(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));
			auditLog(AuditActionEnum.CREATE, userID, "itemCategory", bo.getItemCategory());
		} catch (Exception e) {
			throw new CrownException("Item Category cannot be created", e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean updateItemCategory(ItemCategoryBO bo, int userID) throws CrownException {
		try {
			final ItemCategory category = helper.createItemCategory(bo,
					manager.find(ItemCategory.class, bo.getId()));
			manager.merge(category);
			category.setStatusBean(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));
			auditLog(AuditActionEnum.UPDATE, userID, "itemCategory", bo.getItemCategory());
		} catch (DataAccessException e) {
			throw new CrownException(
					"Item Category has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean deleteItemCategory(ItemCategoryBO bo, int userID) throws CrownException {
		try {
			final ItemCategory category = manager.find(ItemCategory.class, bo.getId());
			category.setVersion(bo.getVersion());
			category.setStatusBean(manager.find(Status.class, StatusBO.DISABLED.getStatusID()));
			manager.merge(category);

			auditLog(AuditActionEnum.DELETE, userID, "itemCategory", bo.getItemCategory());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Item Category has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemCategoryBO> findAllItemCategories() {
		final List<ItemCategory> categories = manager
				.createQuery(
						"select C from ItemCategory C where C.statusBean.statusId=1 order by C.categoryName")
				.getResultList();
		final List<ItemCategoryBO> bos = new ArrayList<ItemCategoryBO>();
		for (final ItemCategory category : categories) {
			bos.add(helper.createItemCategoryBO(category));
		}
		return bos;
	}

	@Transactional
	@Override
	public boolean createItemBrand(ItemBrandBO bo, int userID) throws CrownException {
		try {
			final ItemBrand brand = helper.createItemBrand(bo);
			manager.persist(brand);
			brand.setStatusBean(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));
			auditLog(AuditActionEnum.CREATE, userID, "itemBrand", bo.getItemBrand());
		} catch (Exception e) {
			throw new CrownException("Item Brand cannot be created", e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean updateItemBrand(ItemBrandBO bo, int userID) throws CrownException {
		try {
			final ItemBrand brand = helper.createItemBrand(bo,
					manager.find(ItemBrand.class, bo.getId()));

			brand.setStatusBean(manager.find(Status.class, StatusBO.ACTIVE.getStatusID()));
			manager.merge(brand);
			auditLog(AuditActionEnum.UPDATE, userID, "itemBrand", bo.getItemBrand());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Item Brand has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean deleteItemBrand(ItemBrandBO bo, int userID) throws CrownException {
		try {
			final ItemBrand brand = manager.find(ItemBrand.class, bo.getId());
			brand.setVersion(bo.getVersion());
			brand.setStatusBean(manager.find(Status.class, StatusBO.DISABLED.getStatusID()));
			manager.merge(brand);

			auditLog(AuditActionEnum.DELETE, userID, "itemBrand", bo.getItemBrand());
		} catch (OptimisticLockException e) {
			throw new CrownException(
					"Item Brand has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemBrandBO> findAllItemBrands() {
		final List<ItemBrand> brands = manager.createQuery(
				"select B from ItemBrand B where B.statusBean.statusId=1 order by B.brandName")
				.getResultList();
		final List<ItemBrandBO> bos = new ArrayList<ItemBrandBO>();
		for (final ItemBrand brand : brands) {
			bos.add(helper.createItemBrandBO(brand));
		}
		return bos;
	}

	@Transactional
	@Override
	public boolean createItem(ItemBO bo, int userID) throws CrownException {
		try {
			final Item item = helper.createItem(bo);
			setItemAttributes(item, bo);
			final GeneralService generalService = CrownServiceLocator.INSTANCE
					.getCrownService(GeneralService.class);
			item.setItemCode(ITEM_PREFIX + GeneralHelper.getFormattedRunningDate()
					+ generalService.getRunningSequenceNumber("ITEM", null));
			manager.persist(item);

			auditLog(AuditActionEnum.CREATE, userID, "item", item.getItemCode());
		} catch (Exception e) {
			throw new CrownException("Item cannot be created", e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean updateItem(ItemBO bo, int userID) throws CrownException {
		try {
			final Item item = helper.createItem(bo, manager.find(Item.class, bo.getId()));
			setItemAttributes(item, bo);
			manager.merge(item);

			auditLog(AuditActionEnum.UPDATE, userID, "item", bo.getItemCode());
		} catch (OptimisticLockException e) {
			throw new CrownException("Item has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean deleteItem(ItemBO bo, int userID) throws CrownException {
		try {
			final Item item = helper.createItem(bo, manager.find(Item.class, bo.getId()));
			item.setVersion(bo.getVersion());
			item.setStatusBean(manager.find(Status.class, StatusBO.DISABLED.getStatusID()));
			manager.merge(item);

			auditLog(AuditActionEnum.UPDATE, userID, "item", bo.getItemCode());
		} catch (OptimisticLockException e) {
			throw new CrownException("Item has changed or been deleted since it was last read", e);
		} catch (Exception e) {
			throw new CrownException("Exception " + e.getMessage(), e);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemBO> findAllItems() {
		final List<Item> items = manager.createQuery("select I from Item I order by I.itemName")
				.getResultList();
		final List<ItemBO> bos = new ArrayList<ItemBO>();
		for (final Item item : items) {
			bos.add(helper.createItemBO(item));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemBO> findByItemName(String itemName) {
		Query qq = manager.createNamedQuery("Item.findNameStartsWith")
				.setParameter(1, itemName + "%").setFirstResult(2).setMaxResults(10);
		final List<Item> items = qq.getResultList();
		// manager.findByNamedQuery("Item.findNameStartsWith",
		// itemName+"%").from(2).startAt(3);
		final List<ItemBO> bos = new ArrayList<ItemBO>();
		for (final Item item : items) {
			bos.add(helper.createItemBO(item));
		}
		return bos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemBO> findByItemCode(String itemCode) {
		Query qq = manager.createNamedQuery("Item.findCodeLike")
				.setParameter(1, "%" + itemCode + "%").setMaxResults(20);
		final List<Item> items = qq.getResultList();
		final List<ItemBO> bos = new ArrayList<ItemBO>();
		for (final Item item : items) {
			bos.add(helper.createItemBO(item));
		}
		return bos;
	}

	/**
	 * FILTERING & LAZY LOADING
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemBO> findAllItems(int first, int pageSize) {
		Query query = manager
				.createQuery("select I from Item I where I.statusBean.statusId=1 order by I.itemName");
		// final String countQuery =
		// "SELECT COUNT(*) FROM ITEM WHERE STATUS = 1";
		setFilteredItemCount(query.getResultList().size());
		final List<Item> items = query.setFirstResult(first).setMaxResults(pageSize)
				.getResultList();
		return buildItemBOs(items);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemBO> findAllItems(int first, int pageSize, Map<String, Object> filters) {

		final CriteriaBuilder builder = manager.getCriteriaBuilder();
		final CriteriaQuery<Item> criteriaQuery = builder.createQuery(Item.class);
		final Root<Item> item = criteriaQuery.from(Item.class);
		criteriaQuery.select(item);
		List<Predicate> predicateList = new ArrayList<Predicate>();
		predicateList.add(builder.equal(item.<Status> get("statusBean").<Integer> get("statusId"),
				1));

		// final StringBuilder builder = new StringBuilder();
		// builder.append("SELECT * FROM ITEM");

		// final QueryBuilder queryBuilder = new QueryBuilder();
		// queryBuilder.addQuery("STATUS", 1);
		for (String filterProperty : filters.keySet()) {

			Object filter = filters.get(filterProperty);
			if (filter == null || filter.toString().trim().length() < 2) {
				continue;
			}
			String filterValue = filter.toString().trim();

			if (filterProperty.equals("globalFilter")) {
				predicateList.add(builder.or(
				// ITEM CODE
						builder.like(builder.upper(item.<String> get("itemCode")),
								filterValue.toUpperCase() + "%"),
						// ITEM NAME
						builder.like(builder.upper(item.<String> get("itemName")),
								filterValue.toUpperCase() + "%"),
						// MODEL NUMBER
						builder.like(builder.upper(item.<String> get("modelNumber")),
								filterValue.toUpperCase() + "%")));
			}

			if (filterProperty.equals("itemCode")) {
				predicateList.add(builder.like(builder.upper(item.<String> get("itemCode")),
						filterValue.toUpperCase() + "%"));
			}
			if (filterProperty.equals("itemName")) {
				predicateList.add(builder.like(builder.upper(item.<String> get("itemName")),
						filterValue.toUpperCase() + "%"));
			}
			if (filterProperty.equals("modelNumber")) {
				predicateList.add(builder.like(builder.upper(item.<String> get("modelNumber")),
						filterValue.toUpperCase() + "%"));
			}
		}
		criteriaQuery.where(predicateList.toArray(new Predicate[0])).distinct(true)
				.orderBy(builder.asc(item.get("itemName")));

		Query query = manager.createQuery(criteriaQuery);
		if (query == null) {
			final String countQuery = "SELECT COUNT(*) FROM ITEM WHERE STATUS = 1";
			setFilteredItemCount(((Long) getNativeQuery(countQuery).getSingleResult()).intValue());
			return buildItemBOs(Collections.EMPTY_LIST);
		}
		final List<Item> items = query.setFirstResult(first).setMaxResults(pageSize)
				.getResultList();
		return buildItemBOs(items);
	}

	@Override
	public int findItemCount() {
		Query query = manager.createNativeQuery("select Count(*) from item");
		System.err.println("FIRST RESULT ::: " + query.getSingleResult());
		return ((Long) query.getSingleResult()).intValue();
	}

	@Override
	public int getFilteredItemCount() {
		return filteredItemCount;
	}

	private void setFilteredItemCount(int filteredItemCount) {
		this.filteredItemCount = filteredItemCount;
	}

	private List<ItemBO> buildItemBOs(List<Item> items) {
		final List<ItemBO> bos = new ArrayList<ItemBO>();
		for (final Item item : items) {
			ItemBO bo = helper.createItemBO(item);
			if (bo.getStatus().equals(StatusBO.DISABLED)) {
				continue;
			}
			bos.add(bo);
		}
		return bos;
	}

}
