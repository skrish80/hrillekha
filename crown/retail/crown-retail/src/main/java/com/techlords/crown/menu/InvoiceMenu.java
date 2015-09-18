package com.techlords.crown.menu;

@SuppressWarnings("serial")
public class InvoiceMenu extends CrownMenu {

	private final String nextAction;
	private final String invoiceType;

	public InvoiceMenu(String imgURL, String link, String desc,
			String nextAction, String invoiceType) {
		super(imgURL, link, desc);
		this.nextAction = nextAction;
		this.invoiceType = invoiceType;
	}

	public String getNextAction() {
		return nextAction;
	}

	public String getInvoiceType() {
		return invoiceType;
	}
}
