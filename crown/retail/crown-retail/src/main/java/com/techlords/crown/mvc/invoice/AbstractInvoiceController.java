package com.techlords.crown.mvc.invoice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.AgentBO;
import com.techlords.crown.business.model.BankBO;
import com.techlords.crown.business.model.CompanyBO;
import com.techlords.crown.business.model.CrownEntityBO;
import com.techlords.crown.business.model.CustomerBO;
import com.techlords.crown.business.model.InvoiceBO;
import com.techlords.crown.business.model.InvoiceItemBO;
import com.techlords.crown.business.model.InvoicePaymentBO;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.ReceiptBO;
import com.techlords.crown.business.model.TotalStockBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.business.model.WarehouseStockBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.DiscountTypeBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.business.model.enums.PaymentModeBO;
import com.techlords.crown.business.model.enums.PaymentStatusBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.flow.CrownFlowService;
import com.techlords.crown.service.CompanyService;
import com.techlords.crown.service.CustomerService;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.ItemService;
import com.techlords.crown.service.ReceiptService;
import com.techlords.crown.service.WarehouseService;
import com.techlords.infra.CrownConstants;

@SuppressWarnings("serial")
public abstract class AbstractInvoiceController extends CrownModelController {

	private static final Logger LOGGER = Logger
			.getLogger(AbstractInvoiceController.class);

	protected String invoiceType;

	protected final List<InvoiceBO> invoiceBOs = new ArrayList<InvoiceBO>();

	protected InvoiceBO currentInvoice;

	protected final List<AllocationTypeBO> typeBOs = Arrays
			.asList(AllocationTypeBO.values());
	protected final List<DiscountTypeBO> discountTypeBOs = Arrays
			.asList(DiscountTypeBO.values());

	protected final List<ItemBO> itemBOs = new ArrayList<ItemBO>();
	protected final List<BankBO> bankBOs = new ArrayList<BankBO>();
	protected final List<InvoiceStateBO> invoiceStateBOs = new ArrayList<InvoiceStateBO>(
			Arrays.asList(InvoiceStateBO.values()));
	protected final List<PaymentStatusBO> paymentStatusBOs = new ArrayList<PaymentStatusBO>(
			Arrays.asList(PaymentStatusBO.values()));
	protected final List<PaymentModeBO> paymentModeBOs = new ArrayList<PaymentModeBO>(
			Arrays.asList(PaymentModeBO.values()));
	protected final List<WarehouseBO> warehouseBOs = new ArrayList<WarehouseBO>();
	protected final List<CrownEntityBO> entityBOs = new ArrayList<CrownEntityBO>();
	protected final List<AgentBO> agentBOs = new ArrayList<AgentBO>();
	protected final List<CustomerBO> customerBOs = new ArrayList<CustomerBO>();
	protected final List<CompanyBO> companyBOs = new ArrayList<CompanyBO>();
	protected final List<WarehouseStockBO> warehouseStockBOs = new ArrayList<WarehouseStockBO>();
	protected final List<TotalStockBO> totalStockBOs = new ArrayList<TotalStockBO>();

	protected final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	protected final LazyInvoiceDataModel invoiceModel = new LazyInvoiceDataModel();

	public void setAssociations() {
		currentInvoice.setAgentBO(getAppModel(currentInvoice.getAgent(),
				agentBOs));
		currentInvoice.setEntityBO(getAppModel(currentInvoice.getEntity(),
				entityBOs));
		currentInvoice.setCustomerBO(getAppModel(currentInvoice.getCustomer(),
				customerBOs));
		currentInvoice.setCompanyBO(getAppModel(currentInvoice.getCompany(),
				companyBOs));
		currentInvoice.setDiscountTypeBO(DiscountTypeBO.valueOf(currentInvoice
				.getDiscountType()));
		currentInvoice.setPaymentStatusBO(PaymentStatusBO
				.valueOf(currentInvoice.getPaymentStatus()));

		for (InvoiceItemBO itemBO : currentInvoice.getInvoiceItems()) {
			itemBO.setAllocationTypeBO(AllocationTypeBO.valueOf(itemBO
					.getAllocationType()));
		}
	}

	protected final double getItemPrice(int itemID, int itemQty,
			int allocationType) {
		final ItemBO itemBO = getItemBO(itemID);
		if (itemBO == null) {
			return 0d;
		}
		// final double itemPrice = (allocationType == AllocationTypeBO.UOM
		// .getAllocationTypeID()) ? (itemQty * itemBO.getUomPrice())
		// : (itemQty * itemBO.getItemPrice());
		// INVOICE AMOUNT REQUIRED WITHOUT VAT; VAT IS CALCULATED IN THE FINAL
		// PRICE
		final double itemPrice = (allocationType == AllocationTypeBO.UOM
				.getAllocationTypeID()) ? (itemQty * itemBO
				.getUOMPriceWithVAT()) : (itemQty * itemBO
				.getItemPriceWithVAT());
		return itemPrice;
	}

	protected InvoiceBO getInvoiceBO(int id) {
		for (InvoiceBO bo : invoiceBOs) {
			if (bo.getId() == id) {
				for (InvoiceItemBO itemBO : bo.getInvoiceItems()) {
					int itemID = itemBO.getItem();
					itemBO.setItemBO(getItemBO(itemID));
				}
				return bo;
			}
		}
		return null;
	}

	protected ItemBO getItemBO(int id) {
		return getAppModel(id, itemBOs);
	}

	protected WarehouseBO getWarehouseBO(int id) {
		return getAppModel(id, warehouseBOs);

	}

	protected CrownEntityBO getEntityBO(int id) {
		return getAppModel(id, entityBOs);

	}

	protected final void loadAssociations() {

		final ExecutorService executorService = Executors.newFixedThreadPool(4);

		executorService.execute(new Runnable() {

			@Override
			public void run() {
				itemBOs.clear();
				if (currentInvoice == null) {
					return;
				}
				for (InvoiceItemBO invItem : currentInvoice.getInvoiceItems()) {
					itemBOs.add(invItem.getItemBO());
				}
			}
		});

		executorService.execute(new Runnable() {

			@Override
			public void run() {
				entityBOs.clear();
				GeneralService generalService = CrownServiceLocator.INSTANCE
						.getCrownService(GeneralService.class);
				if ("WS".equals(AbstractInvoiceController.this.getInvoiceType())) {
					entityBOs.addAll(generalService.findAllWholesaleEntities());
				} else {
					entityBOs.addAll(generalService.findAllRetailShops());
				}
			}
		});

		// executorService.execute(new Runnable() {
		//
		// @Override
		// public void run() {
		// final CustomerService customerService = CrownServiceLocator.INSTANCE
		// .getCrownService(CustomerService.class);
		// customerBOs.clear();
		// customerBOs.addAll(customerService.findAllCustomers());
		// }
		// });

		executorService.execute(new Runnable() {

			@Override
			public void run() {
				final GeneralService generalService = CrownServiceLocator.INSTANCE
						.getCrownService(GeneralService.class);
				bankBOs.clear();
				bankBOs.addAll(generalService.findAllBanks());
			}
		});

		executorService.execute(new Runnable() {

			@Override
			public void run() {
				final CompanyService service = CrownServiceLocator.INSTANCE
						.getCrownService(CompanyService.class);
				companyBOs.clear();
				companyBOs.addAll(service.findAllCompanies());
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

		totalStockBOs.clear();
	}

	protected final void loadWarehouses() {
		warehouseStockBOs.clear();
		warehouseBOs.clear();
		final WarehouseService warehouseService = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);
		if (getInvoiceType().equals("WS")) {
			warehouseBOs.addAll(warehouseService.findAllWarehouses());
		} else {
			warehouseBOs.addAll(warehouseService.findAllRetailShops());
			warehouseBOs.addAll(warehouseService.findAllRetailWarehouses());
		}
	}

	protected final void loadStatuses(InvoiceBO bo) {
		invoiceStateBOs.clear();
		final CrownFlowService flowService = CrownFlowService.INSTANCE;
		final String flowClass = InvoiceBO.class.getSimpleName();
		if (bo.isNew()) {
			int firstStepID = flowService.getFirstStepID(flowClass);
			invoiceStateBOs.add(InvoiceStateBO.valueOf(firstStepID));
		} else {
			invoiceStateBOs.addAll(InvoiceStateBO.getValues(flowService
					.getNextSteps(flowClass, bo.getInvoiceState())));
		}
	}

	public InvoiceBO getCurrentInvoice() {
		return currentInvoice;
	}

	public void setCurrentInvoice(InvoiceBO currentInvoice) {
		this.currentInvoice = currentInvoice;
	}

	public List<AllocationTypeBO> getTypeBOs() {
		return typeBOs;
	}

	public List<DiscountTypeBO> getDiscountTypeBOs() {
		return discountTypeBOs;
	}

	public List<ItemBO> getItemBOs() {
		return itemBOs;
	}

	public List<BankBO> getBankBOs() {
		return bankBOs;
	}

	public List<InvoiceStateBO> getInvoiceStateBOs() {
		return invoiceStateBOs;
	}

	public List<PaymentStatusBO> getPaymentStatusBOs() {
		if (CrownConstants.RETAIL.equals(invoiceType)) {
			return Collections.singletonList(PaymentStatusBO.FULL_PAYMENT);
		}
		return paymentStatusBOs;
	}

	public List<PaymentModeBO> getPaymentModeBOs() {
		return paymentModeBOs;
	}

	public List<WarehouseBO> getWarehouseBOs() {
		return warehouseBOs;
	}

	public List<CrownEntityBO> getEntityBOs() {
		return entityBOs;
	}

	public List<AgentBO> getAgentBOs() {
		return agentBOs;
	}

	public List<CustomerBO> getCustomerBOs() {
		return customerBOs;
	}

	public List<WarehouseStockBO> getWarehouseStockBOs() {
		return warehouseStockBOs;
	}

	public List<TotalStockBO> getTotalStockBOs() {
		return totalStockBOs;
	}

	// Auto Complete
	public List<CrownEntityBO> completeEntities(String query) {
		final List<CrownEntityBO> suggestions = new ArrayList<CrownEntityBO>();
		for (CrownEntityBO bo : entityBOs) {
			if (bo.getEntity().toUpperCase().startsWith(query.toUpperCase())) {
				suggestions.add(bo);
			}
		}
		return suggestions;
	}

	public List<CustomerBO> completeCustomers(String query) {
		final CustomerService service = CrownServiceLocator.INSTANCE
				.getCrownService(CustomerService.class);
		Map<String, String> filters = Collections.singletonMap("customerName",
				query);
		customerBOs.clear();
		customerBOs.addAll(service.findAllCustomers(0, 20, filters));
		return customerBOs;
		// final List<CustomerBO> suggestions = new ArrayList<CustomerBO>();
		// for (CustomerBO bo : customerBOs) {
		// if (bo.getCustomerName().toUpperCase()
		// .startsWith(query.toUpperCase())) {
		// suggestions.add(bo);
		// }
		// }
		// return suggestions;
	}

	public List<ItemBO> completeItems(String query) {
		long time1 = System.currentTimeMillis();

		final ItemService service = CrownServiceLocator.INSTANCE
				.getCrownService(ItemService.class);
		// Search by Item name and model number
		final Map<String, Object> filters = Collections.singletonMap(
				"globalFilter", query);

		itemBOs.clear();
		itemBOs.addAll(service.findAllItems(0, 20, filters));
		// final List<ItemBO> suggestions = new ArrayList<ItemBO>();
		// for (ItemBO bo : filteredItems) {
		// if (bo.getItemName().toUpperCase().startsWith(query.toUpperCase())) {
		// suggestions.add(bo);
		// }
		// }
		long time2 = System.currentTimeMillis();
		System.err.println("TIME TAKEN TO AC ITEMS ::: " + (time2 - time1));
		return itemBOs;
	}

	public final LazyDataModel<InvoiceBO> getInvoiceModel() {
		return invoiceModel;
	}

	public final String getInvoiceType() {
		return invoiceType;
	}

	public final void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
		invoiceModel.setInvoiceType(invoiceType);
	}

	public final List<CompanyBO> getCompanyBOs() {
		return companyBOs;
	}

	protected final List<ReceiptBO> receiptBOs = new ArrayList<ReceiptBO>();

	protected final void loadReceipts() {
		final ReceiptService service = CrownServiceLocator.INSTANCE
				.getCrownService(ReceiptService.class);
		receiptBOs.clear();
		receiptBOs
				.addAll(service.findAllUnusedReceipts(currentInvoice.getCustomer()));
	}

	public final List<ReceiptBO> getReceiptBOs() {
		return receiptBOs;
	}
	
	public void addInvoicePayment() {
		currentInvoice.addInvoicePayment(new InvoicePaymentBO());
		loadReceipts();
	}

	public void selectReceipt(InvoicePaymentBO bo) {
		final ReceiptBO rec = getUnusedAmount(bo.getDraftNumber());
		if(rec == null) {
			return;
		}
		bo.setAmount(rec.getAmount() - rec.getUsedAmount());
		bo.setChequeDate(rec.getReceiptDate());
	}
	
	protected final ReceiptBO getUnusedAmount(String receiptNumber) {
		for (ReceiptBO rec : receiptBOs) {
			if (rec.getReceiptNumber().equals(receiptNumber)) {
				return rec;
			}
		}
		return null;
	}
}
