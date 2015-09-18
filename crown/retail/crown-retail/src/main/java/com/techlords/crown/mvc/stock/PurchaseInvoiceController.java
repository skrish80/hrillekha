package com.techlords.crown.mvc.stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.CompanyBO;
import com.techlords.crown.business.model.CurrencyBO;
import com.techlords.crown.business.model.InvoiceItemBO;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.ItemBrandBO;
import com.techlords.crown.business.model.ItemCategoryBO;
import com.techlords.crown.business.model.PurchaseInvoiceBO;
import com.techlords.crown.business.model.PurchaseInvoiceItemBO;
import com.techlords.crown.business.model.SupplierBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.PurchaseInvoiceStateBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.StockValidator;
import com.techlords.crown.service.CompanyService;
import com.techlords.crown.service.ItemService;
import com.techlords.crown.service.PurchaseInvoiceService;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class PurchaseInvoiceController extends CrownModelController {

	private static final Logger LOGGER = Logger
			.getLogger(PurchaseInvoiceController.class);

	private static final String GRN_PREFIX = "GRN";
	private final List<SupplierBO> suppliers = new ArrayList<SupplierBO>();
	private final List<CompanyBO> companies = new ArrayList<CompanyBO>();
	private final List<CurrencyBO> currencies = new ArrayList<CurrencyBO>();
	private final LazyPurchaseInvoiceDataModel invoiceModel = new LazyPurchaseInvoiceDataModel();
	protected final List<ItemBO> itemBOs = new ArrayList<ItemBO>();
	protected final List<AllocationTypeBO> typeBOs = Arrays
			.asList(AllocationTypeBO.values());
	private boolean receiveOnly;
	private PurchaseInvoiceBO currentInvoice;

	@SuppressWarnings("unchecked")
	public void reloadInvoices() {
		invoiceModel.setInvoiceState(receiveOnly ? "PRT" : null);
		invoiceModel.load(0, invoiceModel.getPageSize(), null, null,
				Collections.EMPTY_MAP);
		navigationBean.setNavigationUrl("stock/AllPurchaseInvoices.xhtml");
	}

	public String setupForm(boolean isCreateMode) {
		loadAssociations();
		loadBulkItemAssociations();
		setCurrentInvoice(new PurchaseInvoiceBO());
		navigationBean.setNavigationUrl("stock/CreatePurchaseInvoice.xhtml");
		return null;
	}

	public String setupReceiveForm(PurchaseInvoiceBO bo) {
		setCurrentInvoice(bo);
		String invoiceNum = currentInvoice.getInvoiceNumber();
		if (invoiceNum != null) {
			currentInvoice.setGoodsReceiptNumber(GRN_PREFIX + invoiceNum.substring(4));
		}
		loadAssociations();
		navigationBean.setNavigationUrl("stock/ReceivePurchaseInvoice.xhtml");
		return null;
	}

	public void priceChange(PurchaseInvoiceItemBO itemBO) {
		double invoiceAmount = 0;
		for (PurchaseInvoiceItemBO bo : currentInvoice.getInvoiceItems()) {
			invoiceAmount += bo.getItemQty() * bo.getPrice();
		}
		currentInvoice.setInvoiceAmount(invoiceAmount);
	}

	public void updateInvoiceAmount() {

		double invoiceAmount = 0;
		for (PurchaseInvoiceItemBO bo : currentInvoice.getInvoiceItems()) {
			ItemBO item = bo.getItemBO();
			int alloc = bo.getAllocationType();
			bo.setPrice((item == null) ? 0 : (alloc == AllocationTypeBO.UOM.getAllocationTypeID()) ? item
					.getUomPrice() : item.getItemPrice());
			invoiceAmount += bo.getItemQty() * bo.getPrice();
		}
		currentInvoice.setInvoiceAmount(invoiceAmount);
	}

	private void loadAssociations() {
		final PurchaseInvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(PurchaseInvoiceService.class);
		suppliers.clear();
		suppliers.addAll(service.findAllSuppliers());

		final CompanyService companyService = CrownServiceLocator.INSTANCE
				.getCrownService(CompanyService.class);
		companies.clear();
		companies.addAll(companyService.findAllCompanies());

		currencies.clear();
		currencies.addAll(CrownMVCHelper.getCurrencyBos());
	}

	@Override
	public String save() {
		return null;
	}

	public void setAssociations() {
		currentInvoice.setCompanyBO(getAppModel(currentInvoice.getCompany(),
				companies));
		currentInvoice.setSupplierBO(getAppModel(currentInvoice.getSupplier(),
				suppliers));
	}

	public String createInvoice() {
		setAssociations();
		currentInvoice.setInvoiceState(PurchaseInvoiceStateBO.NEW);
		final StockValidator validator = new StockValidator();
		try {
			validator.validatePurchaseInvoice(currentInvoice);
		} catch(Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		final PurchaseInvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(PurchaseInvoiceService.class);
		try {
			currentInvoice = service.createInvoice(currentInvoice,
					CrownUserDetailsService.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		viewInvoice(currentInvoice);
		return null;
	}

	public String receiveInvoice() {
		currentInvoice.setInvoiceState(PurchaseInvoiceStateBO.RECEIVED);
		final StockValidator validator = new StockValidator();
		try {
			validator.validatePurchaseInvoiceOnReceive(currentInvoice);
		} catch(Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		final PurchaseInvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(PurchaseInvoiceService.class);
		try {
			service.receiveInvoice(currentInvoice, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		navigationBean.setNavigationUrl("stock/AllPurchaseInvoices.xhtml");
		return null;
	}

	public void cancelInvoice() {
		final PurchaseInvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(PurchaseInvoiceService.class);
		try {
			service.cancelInvoice(currentInvoice, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return;
		}
		navigationBean.setNavigationUrl("stock/AllPurchaseInvoices.xhtml");
	}

	public void printInvoice() {
		if (currentInvoice.getInvoiceState() == PurchaseInvoiceStateBO.NEW) {
			final PurchaseInvoiceService service = CrownServiceLocator.INSTANCE
					.getCrownService(PurchaseInvoiceService.class);
			try {
				service.printInvoice(currentInvoice, CrownUserDetailsService
						.getCurrentUser().getId());
			} catch(Exception e) {
				LOGGER.error(e.getMessage(), e);
				FacesUtil.addExceptionMessages(e);
				return;
			}
		}
		navigationBean.setNavigationUrl("stock/AllPurchaseInvoices.xhtml");
	}

	public void viewInvoice(PurchaseInvoiceBO bo) {
		setCurrentInvoice(bo);
		navigationBean.setNavigationUrl("stock/ViewPurchaseInvoice.xhtml");
	}

	public final PurchaseInvoiceBO getCurrentInvoice() {
		return currentInvoice;
	}

	public final void setCurrentInvoice(PurchaseInvoiceBO currentInvoice) {
		this.currentInvoice = currentInvoice;
	}

	public final List<SupplierBO> getSuppliers() {
		return suppliers;
	}

	public final List<CompanyBO> getCompanies() {
		return companies;
	}

	public final List<CurrencyBO> getCurrencies() {
		return currencies;
	}

	public final LazyDataModel<PurchaseInvoiceBO> getInvoiceModel() {
		navigationBean.setNavigationUrl("stock/AllPurchaseInvoices.xhtml");
		return invoiceModel;
	}

	// ADD ITEMS

	public String removeInvoiceItem(PurchaseInvoiceItemBO bo) {
		currentInvoice.removeInvoiceItem(bo);
		updateInvoiceAmount();
		return null;
	}

	public void setItemToInvoiceItem(PurchaseInvoiceItemBO bo, ItemBO item) {
		bo.setItemBO(item);
		int alloc = bo.getAllocationType();
		bo.setPrice((alloc == AllocationTypeBO.UOM.getAllocationTypeID()) ? item
				.getUomPrice() : item.getItemPrice());
		updateInvoiceAmount();
	}

	public String addInvoiceItem() {
		currentInvoice.addInvoiceItem(new PurchaseInvoiceItemBO());
		return null;
	}

	public List<ItemBO> completeItems(String query) {
		final ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);
		// Search by Item name and model number
		final Map<String, Object> filters = Collections.singletonMap(
				"globalFilter", query);

		itemBOs.clear();
		itemBOs.addAll(service.findAllItems(0, 20, filters));
		return itemBOs;
	}

	public final List<ItemBO> getItemBOs() {
		return itemBOs;
	}

	public final List<AllocationTypeBO> getTypeBOs() {
		return typeBOs;
	}

	// INVOICE BULK ADD ITEMS
	private final List<ItemBrandBO> brandBOs = new ArrayList<ItemBrandBO>();
	private final List<ItemCategoryBO> categoryBOs = new ArrayList<ItemCategoryBO>();
	private ItemCategoryBO selCategory;
	private ItemBrandBO selBrand;
	private final List<PurchaseInvoiceItemBO> bulkItems = new ArrayList<PurchaseInvoiceItemBO>();

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

	public final List<PurchaseInvoiceItemBO> getBulkItems() {
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
		final PurchaseInvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(PurchaseInvoiceService.class);
		bulkItems.addAll(service.findBulkInvoiceItems(brdID, catID));
		return null;
	}

	public void setItemAllocation(int allocation, InvoiceItemBO bo) {
		bo.setAllocationType(allocation);
		updateInvoiceAmount();
	}

	public void addBulkItems() {
		for (PurchaseInvoiceItemBO itmBO : bulkItems) {
			if (itmBO.getItemQty() > 0) {
				currentInvoice.addInvoiceItem(itmBO);
			}
			itemBOs.add(itmBO.getItemBO());
		}
		bulkItems.clear();
		updateInvoiceAmount();
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

	public final boolean isReceiveOnly() {
		return receiveOnly;
	}

	public final void setReceiveOnly(boolean receiveOnly) {
		this.receiveOnly = receiveOnly;
	}
}
