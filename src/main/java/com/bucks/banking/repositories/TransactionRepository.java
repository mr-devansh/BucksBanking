package com.bucks.banking.repositories;

import java.sql.Connection;
import java.util.List;

import com.bucks.banking.model.TransactionDetail;

public interface TransactionRepository {
	Long addTransaction(TransactionDetail transactionDetail, Connection connection);//: Add a new transaction.
	List<TransactionDetail> getAllTransactionDetailsByAccountNumber(Long accountNumber);
}
