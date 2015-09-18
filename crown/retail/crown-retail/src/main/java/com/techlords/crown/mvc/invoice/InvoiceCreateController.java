package com.techlords.crown.mvc.invoice;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.business.model.InvoiceBO;
import com.techlords.crown.business.model.InvoiceItemBO;
import com.techlords.crown.business.model.InvoicePaymentBO;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.ItemBrandBO;
import com.techlords.crown.business.model.ItemCategoryBO;
import com.techlords.crown.business.model.TotalStockBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.InvoiceValidator;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.InvoiceService;
import com.techlords.crown.service.ItemService;
import com.techlords.infra.CrownConstants;

/**
 * Handles requests for the application home page.
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class InvoiceCreateController extends AbstractInvoiceController {

	private final Set<InvoiceBO> localInvoices = new HashSet<InvoiceBO>();
	private String payer;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InvoiceCreateController.class);

	public String addInvoiceItem() {
		final InvoiceItemBO bo = new InvoiceItemBO();
		bo.setAllocationTypeBO(AllocationTypeBO.ITEM);
		bo.setAllocationType(AllocationTypeBO.ITEM.getAllocationTypeID());
		currentInvoice.addInvoiceItem(bo);
		return null;
	}

	public String removeInvoiceItem(InvoiceItemBO bo) {
		currentInvoice.removeInvoiceItem(bo);
		if (currentInvoice.getInvoiceItems().isEmpty()) {
			currentInvoice.resetDiscount();
		}
		updateInvoiceAmount();
		return null;
	}

	public String removeInvoicePayment(InvoicePaymentBO bo) {
		currentInvoice.removeInvoicePayment(bo);
		updateInvoicePayment();
		return null;
	}

	public String save() {
		setAssociations();
		return createInvoice();
	}

	public void setupForm(String invoiceType) {
		setInvoiceType(invoiceType);
		InvoiceBO bo = new InvoiceBO();
		bo.setInvoiceType(getInvoiceType());
		setupForm(bo);
	}

	public void setupForm(InvoiceBO bo) {
		setCurrentInvoice(bo);
		setPayer(null);

		clearBulkItemsForm();

		currentInvoice.setStatus(InvoiceStateBO.NEW.getStateID());
		if (invoiceType.equals(CrownConstants.RETAIL)) {
			final WarehouseBO wh = CrownUserDetailsService.getCurrentShop();
			if (wh != null) {
				currentInvoice.setEntity(wh.getEntity());
				currentInvoice.setEntityBO(wh.getEntityBO());
			}
		}

		final ExecutorService executorService = Executors.newFixedThreadPool(3);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				loadAssociations();
			}
		});
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				loadStatuses(currentInvoice);
			}
		});
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				loadBulkItemAssociations();
			}
		});
		executorService.shutdown();
		try {
			executorService.awaitTermination(CrownConstants.WAIT_TIME,
					TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		}
		checkEntityItemsAvailability();
		isListLoaded = false;
		navigationBean.setNavigationUrl("invoice/CreateInvoice.xhtml");
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 * 
	 * @throws CrownException
	 */
	public String checkEntityItemsAvailability() {

		totalStockBOs.clear();
		List<Integer> itemIDs = new ArrayList<Integer>();
		for (InvoiceItemBO it : currentInvoice.getInvoiceItems()) {
			itemIDs.add(it.getItem());
		}
		currentInvoice.setEntityBO(getAppModel(currentInvoice.getEntity(),
				entityBOs));
		if (currentInvoice.getEntityBO() == null) {
			String ent = "Entity";
			if (CrownConstants.RETAIL.equals(invoiceType)) {
				ent = "Shop";
			}
			FacesUtil.addErrorFlashMessage("Select " + ent, "Select " + ent
					+ " to check availability");
			return null;
		}
		if (itemIDs.isEmpty()) {
			FacesUtil.addErrorFlashMessage("Add Items",
					"Add Items to check availability");
			return null;
		}

		GeneralService generalService = CrownServiceLocator.INSTANCE
				.getCrownService(GeneralService.class);
		totalStockBOs.addAll(generalService.getInvoiceAvailability(
				currentInvoice.getEntity(), itemIDs.toArray()));

		for (TotalStockBO bo : totalStockBOs) {
			bo.setItemBO(getItemBO(bo.getItemID()));
		}

		Collections.sort(totalStockBOs, new Comparator<TotalStockBO>() {

			@Override
			public int compare(TotalStockBO o1, TotalStockBO o2) {
				final ItemBO bo1 = o1.getItemBO();
				final ItemBO bo2 = o2.getItemBO();
				if (bo1 != null && bo2 != null) {
					return bo1.getItemName().compareTo(bo2.getItemName());
				}
				return 0;
			}
		});

		return null;
	}

	// protected ItemBO getItemBO(int id) {
	// for(InvoiceItemBO bo : currentInvoice.getInvoiceItems()) {
	// if(bo.getItem() == id) {
	// return bo.getItemBO();
	// }
	// }
	// return getAppModel(id, itemBOs);
	// }

	public String createInvoice() {
		for (InvoicePaymentBO pmt : currentInvoice.getInvoicePayments()) {
			pmt.setPayer(payer);
		}

		final InvoiceValidator validator = new InvoiceValidator();
		try {
			checkEntityItemsAvailability();
			validator.validateInvoiceOnCreate(currentInvoice, totalStockBOs);
		} catch (Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}

		// NEW INVOICE
		currentInvoice.setInvoiceStateBO(InvoiceStateBO.NEW);
		currentInvoice.setInvoiceState(InvoiceStateBO.NEW.getStateID());
		currentInvoice.setInvoiceType(getInvoiceType());
		final CrownUserBO user = CrownUserDetailsService.getCurrentUser();
		String remarks = currentInvoice.getRemarks();
		remarks += "#Created by ::: " + user.getFirstName() + " "
				+ user.getLastName() + "#";
		currentInvoice.setRemarks(remarks);

		InvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(InvoiceService.class);
		try {
			InvoiceBO tmp = service.createInvoice(currentInvoice, user.getId());
			removeLocalInvoice(currentInvoice);
			setCurrentInvoice(tmp);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		FacesUtil.findBean("invoiceCommonController",
				InvoiceCommonController.class).viewInvoice(currentInvoice);
		return null;
	}

	public void setItemToInvoiceItem(InvoiceItemBO bo, ItemBO item) {
		bo.setItemBO(item);
		itemBOs.add(item);
		updateInvoiceAmount();
	}

	public void updateInvoiceAmount() {
		if (currentInvoice.getDiscountType() <= 0) {
			currentInvoice.setDiscountAmount(0);
		}
		double invoiceAmount = 0;
		for (InvoiceItemBO bo : currentInvoice.getInvoiceItems()) {
			bo.setAmount(getItemPrice(bo.getItem(), bo.getItemQty(),
					bo.getAllocationType()));
			invoiceAmount += bo.getAmount();
		}
		currentInvoice.setInvoiceAmount(invoiceAmount
				+ currentInvoice.getOtherPriceAmount());
		checkEntityItemsAvailability();
	}

	public void updateInvoicePayment() {
		double invoicePayment = 0;
		for (InvoicePaymentBO bo : currentInvoice.getInvoicePayments()) {
			invoicePayment += bo.getAmount();
		}
		currentInvoice.setTotalPaidAmount(invoicePayment);
	}

	// INVOICE BULK ADD ITEMS
	private final List<ItemBrandBO> brandBOs = new ArrayList<ItemBrandBO>();
	private final List<ItemCategoryBO> categoryBOs = new ArrayList<ItemCategoryBO>();
	private ItemCategoryBO selCategory;
	private ItemBrandBO selBrand;
	private final List<InvoiceItemBO> bulkItems = new ArrayList<InvoiceItemBO>();

	private void loadBulkItemAssociations() {
		ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);

		brandBOs.clear();
		categoryBOs.clear();

		brandBOs.addAll(service.findAllItemBrands());
		categoryBOs.addAll(service.findAllItemCategories());
	}

	public final List<ItemBrandBO> getBrandBOs() {
		return brandBOs;
	}

	public final List<ItemCategoryBO> getCategoryBOs() {
		return categoryBOs;
	}

	public final ItemCategoryBO getSelCategory() {
		return selCategory;
	}

	public final void setSelCategory(ItemCategoryBO selCategory) {
		this.selCategory = selCategory;
	}

	public final ItemBrandBO getSelBrand() {
		return selBrand;
	}

	public final void setSelBrand(ItemBrandBO selBrand) {
		this.selBrand = selBrand;
	}

	public final List<InvoiceItemBO> getBulkItems() {
		return bulkItems;
	}

	public void clearBulkItemsForm() {
		setSelBrand(null);
		setSelCategory(null);
		bulkItems.clear();
	}

	public void handleBulkSelect(SelectEvent event) {
		searchBulkItems();
	}

	public void handleBulkUnselect(UnselectEvent event) {
		searchBulkItems();
	}

	public String searchBulkItems() {
		bulkItems.clear();
		Integer catID = selCategory != null ? selCategory.getId() : null;
		Integer brdID = selBrand != null ? selBrand.getId() : null;
		final InvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(InvoiceService.class);
		bulkItems.addAll(service.findBulkInvoiceItems(brdID, catID));
		return null;
	}

	public void setItemAllocation(int allocation, InvoiceItemBO bo) {
		bo.setAllocationType(allocation);
		updateInvoiceAmount();
	}

	public void addBulkItems() {
		for (InvoiceItemBO itmBO : bulkItems) {
			if (itmBO.getItemQty() > 0) {
				currentInvoice.addInvoiceItem(itmBO);
			}
			itemBOs.add(itmBO.getItemBO());
		}
		bulkItems.clear();
		checkEntityItemsAvailability();
		updateInvoiceAmount();
	}

	private final NumberFormat numberFormat = new DecimalFormat("######");

	public void saveLocalInvoice() {
		setAssociations();
		localInvoices.add(currentInvoice);
		currentInvoice.setInvoiceNumber("INV"
				+ numberFormat.format(localInvoices.size()));
		navigationBean.setNavigationUrl("invoice/LocalInvoices.xhtml");
	}

	public void removeLocalInvoice(InvoiceBO bo) {
		localInvoices.remove(currentInvoice);
	}

	public void removeAllLocalInvoices() {
		localInvoices.clear();
	}

	public final List<InvoiceBO> getLocalInvoices() {
		final List<InvoiceBO> list = new ArrayList<InvoiceBO>();
		for (InvoiceBO bo : localInvoices) {
			if (bo.getInvoiceType().equals(getInvoiceType())) {
				list.add(bo);
			}
		}
		return list;
	}

	// Auto Complete
	public List<ItemBrandBO> completeBrands(String query) {
		final List<ItemBrandBO> suggestions = new ArrayList<ItemBrandBO>();
		for (ItemBrandBO bo : brandBOs) {
			if (bo.getItemBrand().toUpperCase().startsWith(query.toUpperCase())) {
				suggestions.add(bo);
			}
		}
		return suggestions;
	}

	public List<ItemCategoryBO> completeCategories(String query) {
		final List<ItemCategoryBO> suggestions = new ArrayList<ItemCategoryBO>();
		for (ItemCategoryBO bo : categoryBOs) {
			if (bo.getItemCategory().toUpperCase()
					.startsWith(query.toUpperCase())) {
				suggestions.add(bo);
			}
		}
		return suggestions;
	}

	public final String getPayer() {
		return payer;
	}

	public final void setPayer(String payer) {
		this.payer = payer;
	}
}
