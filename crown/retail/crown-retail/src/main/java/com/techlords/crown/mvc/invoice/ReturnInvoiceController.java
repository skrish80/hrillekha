package com.techlords.crown.mvc.invoice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.InvoiceBO;
import com.techlords.crown.business.model.InvoiceItemBO;
import com.techlords.crown.business.model.InvoiceReturnBO;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.crown.business.model.enums.InvoiceStateBO;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.mvc.validators.InvoiceValidator;
import com.techlords.crown.service.InvoiceService;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class ReturnInvoiceController extends AbstractInvoiceController {

	private final List<InvoiceReturnBO> existingReturns = new ArrayList<InvoiceReturnBO>();

	@PostConstruct
	public void loadMyInvoices() {
		invoiceModel.setInvoiceStates(InvoiceStateBO.DELIVERED.getStateID(),
				InvoiceStateBO.PARTIAL_DELIVERY.getStateID(),
				InvoiceStateBO.RETURNED.getStateID());
		invoiceModel.setReturn(true);
	}

	public String setupForm(InvoiceBO bo) {
		setCurrentInvoice(bo);
		existingReturns.clear();
		loadWarehouses();
		filterItems();
		existingReturns.addAll(currentInvoice.getInvoiceReturns());
		currentInvoice.getInvoiceReturns().clear();
		invoiceStateBOs.clear();
		invoiceStateBOs.addAll(Arrays.asList(InvoiceStateBO.DELIVERED,
				InvoiceStateBO.PARTIAL_DELIVERY));

		navigationBean.setNavigationUrl("invoice/ReturnInvoice.xhtml");
		isListLoaded = false;
		return null;
	}

	public String returnInvoice() {
		for (InvoiceItemBO itemBO : currentInvoice.getInvoiceItems()) {
			itemBO.setItemBO(getItemBO(itemBO.getItem()));
		}
		final InvoiceValidator validator = new InvoiceValidator();
		try {
			validator.validateInvoiceOnReturn(currentInvoice, existingReturns);
		} catch(Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}

		updateReturnAmount(currentInvoice);
		currentInvoice.setInvoiceStateBO(InvoiceStateBO.RETURNED);
		currentInvoice.setInvoiceState(InvoiceStateBO.RETURNED.getStateID());
		InvoiceService service = CrownServiceLocator.INSTANCE
				.getCrownService(InvoiceService.class);
		try {
			service.returnInvoice(currentInvoice, CrownUserDetailsService
					.getCurrentUser().getId());
		} catch(Exception e) {
			FacesUtil.addExceptionMessages(e);
			return null;
		}
		FacesUtil.findBean("invoiceControllerFactory",
				InvoiceControllerFactory.class).showView("RETURN",
				getInvoiceType());
		return null;
	}

	private void filterItems() {
		itemBOs.clear();
		for (InvoiceItemBO invItem : currentInvoice.getInvoiceItems()) {
			itemBOs.add(invItem.getItemBO());
		}
	}

	public void updateReturnPrice(InvoiceReturnBO bo) {
		ItemBO item = getItemBO(bo.getItem());

		if (item == null) {
			return;
		}
		if (bo.getReturnType() <= 0) {
			return;
		}
		if (bo.getReturnQty() == null) {
			return;
		}
		double returnPrice = 0;

		// If the customer returns the same allocation type (UOM/ITEM),
		// return amount will be calculated from the paid amount
		// Otherwise, the latest item price will be taken into account
		try {
			returnPrice = getItemAmount(bo.getItem(), bo.getReturnType());
		} catch(Exception e) {
			if (bo.getReturnType() == AllocationTypeBO.UOM
					.getAllocationTypeID()) {
				FacesUtil.addErrorFlashMessage("Return Type Mismatch",
						"You cannot return UOM for ITEM Delivery");
				bo.setReturnQty(null);
				return;
			}
			returnPrice = (bo.getReturnType() == AllocationTypeBO.ITEM
					.getAllocationTypeID()) ? item.getItemPriceWithVAT() : item
					.getUOMPriceWithVAT();
			// returnPrice = (bo.getReturnType() == AllocationTypeBO.ITEM
			// .getAllocationTypeID()) ? item.getItemPrice() : item
			// .getUOMPrice();
		}
		bo.setReturnAmount(bo.getReturnQty() * returnPrice);
	}

	private void updateReturnAmount(InvoiceBO invoiceBO) {
		double returnAmount = 0d;
		for (InvoiceReturnBO itemBO : invoiceBO.getInvoiceReturns()) {
			returnAmount += itemBO.getReturnAmount();
		}
		invoiceBO.setReturnAmount(returnAmount);
	}

	private double getItemAmount(int itemID, int returnType)
			throws CrownException {
		for (InvoiceItemBO item : currentInvoice.getInvoiceItems()) {
			if (item.getItem() == itemID
					&& item.getAllocationType() == returnType) {
				return item.getAmount() / item.getItemQty();
			}
		}
		throw new CrownException("Item Allocation Type Varies from Return Type");
	}

	public String addInvoiceReturn() {
		InvoiceReturnBO bo = new InvoiceReturnBO();
		bo.setInvoice(currentInvoice.getId());
		currentInvoice.addInvoiceReturn(bo);
		return null;
	}

	public String removeInvoiceReturn(InvoiceReturnBO bo) {
		currentInvoice.removeInvoiceReturn(bo);
		return null;
	}

	@Override
	public String save() {
		return returnInvoice();
	}

	public final List<InvoiceReturnBO> getExistingReturns() {
		return existingReturns;
	}
}
