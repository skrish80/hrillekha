package com.techlords.crown.service;

import java.util.List;

import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.ReceiptBO;
import com.techlords.crown.business.model.ReceiptPaymentBO;

public interface ReceiptService extends CrownService {

	ReceiptBO createReceipt(ReceiptBO bo, int userID) throws CrownException;

	void printReceipt(ReceiptBO bo, int userID) throws CrownException;

	boolean amendReceiptPayments(ReceiptBO bo,
			List<ReceiptPaymentBO> removedCheques, int userID)
			throws CrownException;

	List<ReceiptBO> findAllReceipts(int customerID);
	
	List<ReceiptBO> findAllUnusedReceipts(int customerID);

	List<ReceiptBO> findAllReceipts(String chequeNumber);
}
