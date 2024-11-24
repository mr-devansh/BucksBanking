package com.bucks.banking.repositories;

import java.util.List;

import com.bucks.banking.model.TransactionDetail;

public interface TransactionRepository {
	Long addTransaction(TransactionDetail transactionDetail);//: Add a new transaction.
	List<TransactionDetail> getAllTransactionDetailsByAccountNumber(Long accountNumber);
}
