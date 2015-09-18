/**
 * 
 */
package com.techlords.crown.helpers;

import java.util.Date;
import java.util.Set;

import com.techlords.crown.business.model.CustomerBO;
import com.techlords.crown.business.model.ReceiptBO;
import com.techlords.crown.business.model.ReceiptPaymentBO;
import com.techlords.crown.business.model.enums.PaymentModeBO;
import com.techlords.crown.persistence.Bank;
import com.techlords.crown.persistence.Company;
import com.techlords.crown.persistence.Customer;
import com.techlords.crown.persistence.Receipt;
import com.techlords.crown.persistence.ReceiptPayment;

/**
 * @author gv
 * 
 */
public final class ReceiptHelper {

	public ReceiptBO createReceiptBO(Receipt receipt) {
		final ReceiptBO bo = new ReceiptBO();
		bo.setId(receipt.getReceiptId());
		bo.setVersion(receipt.getVersion());
		bo.setReceiptNumber(receipt.getReceiptNumber());
		bo.setReceiptDate(receipt.getReceiptDate());
		// bo.setAmount(receipt.getAmount());
		bo.setUsedAmount(receipt.getUsedAmount());
		bo.setUsedState(receipt.getUsedState());
		bo.setIssued(receipt.isIssued());
		bo.setRemarks(receipt.getRemarks());

		Customer cust = receipt.getCustomerBean();
		bo.setCustomer(cust.getCustomerId());
		CustomerBO custBO = new CustomerBO();
		custBO.setId(cust.getCustomerId());
		custBO.setVersion(cust.getVersion());
		custBO.setCustomerCode(cust.getCustomerCode());
		custBO.setCustomerName(cust.getCustomerName());
		bo.setCustomerBO(custBO);

		Company comp = receipt.getIssuingCompany();
		bo.setCompany(comp.getCompanyId());
		bo.setCompanyBO(new CompanyHelper().createCompanyBO(comp));

		Set<ReceiptPayment> payments = receipt.getReceiptPayments();
		for (ReceiptPayment pmt : payments) {
			bo.addPayment(createReceiptPaymentBO(pmt));
		}
		return bo;
	}

	public ReceiptPayment createReceiptPayment(ReceiptPaymentBO bo) {
		final ReceiptPayment payment = new ReceiptPayment();
		payment.setVersion(bo.getVersion());
		payment.setChequeNumber(bo.getChequeNumber());
		payment.setPayer(bo.getPayer());
		payment.setChequeDate(bo.getChequeDate());
		payment.setAmount(bo.getAmount());
		return payment;
	}

	public ReceiptPaymentBO createReceiptPaymentBO(ReceiptPayment pmt) {
		final ReceiptPaymentBO bo = new ReceiptPaymentBO();
		bo.setId(pmt.getPaymentId());
		bo.setVersion(pmt.getVersion());
		bo.setAmount(pmt.getAmount());
		bo.setPaymentMode(pmt.getPaymentModeBean().getPaymentModeId());
		bo.setPaymentModeBO(PaymentModeBO.valueOf(bo.getPaymentMode()));
		bo.setChequeNumber(pmt.getChequeNumber());
		bo.setChequeDate(pmt.getChequeDate());
		bo.setPayer(pmt.getPayer());

		bo.setAmount(pmt.getAmount());

		// Get the bank
		final Bank bank = pmt.getBank();
		if (bank != null) {
			bo.setBank(bank.getBankId());
			bo.setBankBO(new GeneralHelper().createBankBO(bank));
		}
		return bo;
	}

	public Receipt createReceipt(ReceiptBO bo) {
		return createReceipt(bo, null);
	}

	public Receipt createReceipt(ReceiptBO bo, Receipt toEdit) {
		Receipt receipt = (toEdit == null) ? new Receipt() : toEdit;
		receipt.setVersion(bo.getVersion());
		receipt.setReceiptDate(new Date());
		receipt.setAmount(bo.getAmount());
		receipt.setUsedAmount(bo.getUsedAmount());
		receipt.setUsedState(bo.getUsedState());
		receipt.setRemarks(bo.getRemarks());
		return receipt;
	}
}
