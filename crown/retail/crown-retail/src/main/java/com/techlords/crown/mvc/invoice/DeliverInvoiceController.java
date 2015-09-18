package com.techlords.crown.mvc.invoice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.InvoiceBO;
import com.techlords.crown.business.model.InvoiceItemBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.InvoiceValidator;
import com.techlords.crown.service.InvoiceService;
import com.techlords.crown.service.WarehouseService;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class DeliverInvoiceController extends AbstractInvoiceController {
	private static final Logger LOGGER = Logger
			.getLogger(DeliverInvoiceController.class);

	@PostConstruct
	public void loadMyInvoices() {
		invoiceModel.setInvoiceStates(InvoiceStateBO.PRINTED.getStateID(),
				InvoiceStateBO.PARTIAL_DELIVERY.getStateID());
	}

	public String setupForm(InvoiceBO bo) {
		setCurrentInvoice(bo);
		// setAssociations();
		loadWarehouses();
		itemBOs.clear();
		for (InvoiceItemBO invItem : currentInvoice.getInvoiceItems()) {
			itemBOs.add(invItem.getItemBO());
		}
		checkWarehouseItemsAvailability();
		invoiceStateBOs.clear();
		invoiceStateBOs.addAll(Arrays.asList(InvoiceStateBO.DELIVERED,
				InvoiceStateBO.PARTIAL_DELIVERY));

		navigationBean.setNavigationUrl("invoice/DeliverInvoice.xhtml");
		isListLoaded = false;
		return null;
	}

	public String deliver() {
		for (InvoiceItemBO itemBO : currentInvoice.getInvoiceItems()) {
			itemBO.setItemBO(getItemBO(itemBO.getItem()));
		}
		final InvoiceValidator validator = new InvoiceValidator();
		try {
			validator.validateInvoiceOnDeliver(currentInvoice,
					warehouseStockBOs);
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			FacesUtil.addExceptionMessages(e);
			return null;
		}

		currentInvoice.setInvoiceStateBO(InvoiceStateBO.valueOf(currentInvoice
				.getInvoiceState()));
		InvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(InvoiceService.class);
		try {
			service.deliverInvoice(currentInvoice, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		FacesUtil.findBean("invoiceControllerFactory",
				InvoiceControllerFactory.class).showView("DELIVER",
				getInvoiceType());
		return null;
	}

	public String checkWarehouseItemsAvailability() {
		warehouseStockBOs.clear();
		List<Integer> itemIDs = new ArrayList<Integer>();
		for (InvoiceItemBO it : currentInvoice.getInvoiceItems()) {
			itemIDs.add(it.getItem());
		}
		int deliveryWarehouseID = currentInvoice.getDeliveryWarehouse();
		if (deliveryWarehouseID <= 0) {
			FacesUtil.addErrorFlashMessage("Select Warehouse",
					"Select Warehouse to check availability");
			return null;
		}
		if (itemIDs.isEmpty()) {
			FacesUtil.addErrorFlashMessage("Add Items",
					"No Items to check availability");
			return null;
		}
		currentInvoice.setDeliveryWarehouseBO(getAppModel(
				currentInvoice.getDeliveryWarehouse(), warehouseBOs));

		WarehouseService stockService = CrownServiceLocator.INSTANCE
				.getCrownService(WarehouseService.class);

		warehouseStockBOs.addAll(stockService.findWarehouseStock(
				deliveryWarehouseID, itemIDs.toArray()));
		return null;
	}

	public void fullDelivery() {
		for (InvoiceItemBO it : currentInvoice.getInvoiceItems()) {
			
			it.setDeliveredQty(it.getItemQty() - it.getAlreadyDeliveredQty());
		}
	}

	public boolean isFullDelivery() {
		return currentInvoice.getInvoiceState() == InvoiceStateBO.DELIVERED
				.getStateID();
	}

	@Override
	public String save() {
		return deliver();
	}
}
