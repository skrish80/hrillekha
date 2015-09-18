package com.techlords.crown.mvc.validators;

import java.util.List;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.ReceiptBO;
import com.techlords.crown.business.model.ReceiptPaymentBO;
import com.techlords.crown.business.model.enums.PaymentModeBO;

public class ReceiptValidator {

	public void validateReceipt(ReceiptBO currentReceipt) throws CrownException {
		if (currentReceipt.getAmount() <= 0) {
			throw new CrownException("Add Receipt Payments");
		}

		List<ReceiptPaymentBO> payments = currentReceipt.getPayments();
		if (payments.isEmpty()) {
			throw new CrownException("Add Receipt Payments");
		}

		for (ReceiptPaymentBO pmt : payments) {
			if (pmt.getAmount() <= 0) {
				throw new CrownException("Enter Amount for Payment");
			}
			if (pmt.getPaymentMode() == PaymentModeBO.CHEQUE.getModeID()) {
				if (pmt.getBank() <= 0) {
					throw new CrownException("Select Bank for Payment");
				}
				if (pmt.getChequeNumber() == null
						|| pmt.getChequeNumber().length() < 1) {
					throw new CrownException("Enter Cheque Number");
				}
			}
		}

	}
}
