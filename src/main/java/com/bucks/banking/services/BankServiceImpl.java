package com.bucks.banking.services;

import com.bucks.banking.model.Account;
import com.bucks.banking.model.TransactionDetail;
import com.bucks.banking.model.TransactionType;
import com.bucks.banking.repositories.AccountRepository;
import com.bucks.banking.repositories.JdbcAccountRepositoryImpl;
import com.bucks.banking.repositories.JdbcTransactionRepositoryImpl;
import com.bucks.banking.repositories.TransactionRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class BankServiceImpl implements BankService {
    
    private JdbcAccountRepositoryImpl accountRepo;
    private JdbcTransactionRepositoryImpl transactionRepo;


    public BankServiceImpl(JdbcAccountRepositoryImpl accountRepo, JdbcTransactionRepositoryImpl transactionRepo) {
		// TODO Auto-generated constructor stub
    	this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
	}

    public Long transfer(Long fromAccount, Long toAccount, int amount) {
        Connection connection = null;
        try {
            // Step 1: Establish database connection and disable auto-commit
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);  // Start transaction

            // Step 2: Debit the sender's account
            Long debitTransactionId = debit(amount, fromAccount);
            if (debitTransactionId == null) {
                connection.rollback();  // Rollback if debit fails
                return null;
            }

            // Step 3: Credit the recipient's account
            Long creditTransactionId = credit(amount, toAccount);
            if (creditTransactionId == null) {
                connection.rollback();  // Rollback if credit fails
                return null;
            }

            // Step 4: Commit the transaction if both debit and credit are successful
            connection.commit();
            return creditTransactionId;

        } catch (SQLException e) {
            // Handle exceptions and rollback the transaction if needed
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();  // Rollback transaction on error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;  // Return null if transfer failed

        } finally {
            // Ensure connection is set back to auto-commit mode after transaction
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();  // Close connection
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Long debit(int amount, Long accountNumber) {
        Account account = accountRepo.findAccountByNumber(accountNumber);
        if (account != null && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountRepo.update(account);
            TransactionDetail transaction = new TransactionDetail(0L, accountNumber, new Date(), amount, TransactionType.DEBIT);
            return transactionRepo.addTransaction(transaction);
        }
        return null;
    }


    public Long credit(int amount, Long accountNumber) {
        Account account = accountRepo.findAccountByNumber(accountNumber);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            accountRepo.update(account);
            TransactionDetail transaction = new TransactionDetail(0L, accountNumber, new Date(), amount, TransactionType.CREDIT);
            return transactionRepo.addTransaction(transaction);
        }
        return null;
    }

    public void createNewAccount(Account account) {
        accountRepo.save(account);
    }

    public void deactivateAccount(Long accountNumber) {
        Account account = accountRepo.findAccountByNumber(accountNumber);
        if (account != null) {
            account.setActive(false);
            accountRepo.update(account);
        }
    }

    public void activateAccount(Long accountNumber) {
        Account account = accountRepo.findAccountByNumber(accountNumber);
        if (account != null) {
            account.setActive(true);
            accountRepo.update(account);
        }
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAllAccounts();
    }
}

