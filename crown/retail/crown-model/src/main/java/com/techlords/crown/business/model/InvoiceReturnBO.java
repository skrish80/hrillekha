package com.techlords.crown.business.model;

import java.util.Date;

import com.techlords.crown.business.model.enums.AllocationTypeBO;
import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public final class InvoiceReturnBO extends AppModel {
	private int invoice;
	private InvoiceBO invoiceBO;

	private int item;
	private ItemBO itemBO;

	private int returnType;
	private AllocationTypeBO returnTypeBO;

	private Integer returnQty;
	private double returnAmount;
	private String remarks;
	private Date returnDate;

	public int getInvoice() {
		return invoice;
	}

	public void setInvoice(int invoice) {
		this.invoice = invoice;
	}

	public InvoiceBO getInvoiceBO() {
		return invoiceBO;
	}

	public void setInvoiceBO(InvoiceBO invoiceBO) {
		this.invoiceBO = invoiceBO;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public ItemBO getItemBO() {
		return itemBO;
	}

	public void setItemBO(ItemBO itemBO) {
		this.itemBO = itemBO;
	}

	public int getReturnType() {
		return returnType;
	}

	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

	public AllocationTypeBO getReturnTypeBO() {
		return returnTypeBO;
	}

	public void setReturnTypeBO(AllocationTypeBO returnTypeBO) {
		this.returnTypeBO = returnTypeBO;
	}

	public Integer getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
	}

	public double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
}
