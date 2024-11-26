package com.bucks.banking.services;

import com.bucks.banking.model.Account;
import com.bucks.banking.model.Beneficiary;
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
    EmailService emailService = new EmailService();

    public BankServiceImpl(JdbcAccountRepositoryImpl accountRepo, JdbcTransactionRepositoryImpl transactionRepo) {
		// TODO Auto-generated constructor stub
    	this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
	}
    
    public int updateBeneficiaries(String[] names, long accountNumber) {
    	Connection connection = null;
    	try {
    		connection = DBUtil.getConnection();
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
        Account account = accountRepo.findAccountByNumber(accountNumber);
        if (account != null) {
        	Set<Beneficiary> setBene = new HashSet<Beneficiary>();
            // Update the balance
        	for (int i = 0; i < names.length; i++) {
				setBene.add(new Beneficiary(0L, names[i]));
			}
            account.setBeneficiaries(setBene);
            accountRepo.update(account, connection);  // Pass connection to use it in update method
            emailService.sendMail(account.getEmailAddress(), "bank@bucks.com"
            		, "Hi "+account.getName()+
            		",your Beneficiaries are added Successfully");
            return 1;  // Pass connection to use it in addTransaction
        }
    	return -1;
    }
    
    public Long transfer(Long fromAccount, Long toAccount, int amount) {
        Connection connection = null;
        try {
            // Step 1: Establish database connection and disable auto-commit
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);  // Start a transaction

            // Step 2: Debit the sender's account
            Long debitTransactionId = debit(amount, fromAccount, connection);
            if (debitTransactionId == null) {
                connection.rollback();  // Rollback if debit fails
                return null;
            }

            // Step 3: Credit the recipient's account
            Long creditTransactionId = credit(amount, toAccount, connection);
            if (creditTransactionId == null) {
                connection.rollback();  // Rollback if credit fails
                return null;
            }

            // Step 4: Commit the transaction if both debit and credit are successful
            connection.commit();
        	Account from = accountRepo.findAccountByNumber(fromAccount);
        	Account to = accountRepo.findAccountByNumber(toAccount);

            emailService.sendMail(from.getEmailAddress(), "bank@bucks.com"
            		, "Hi "+from.getName()+
            		",The transfer request from your Account number "+
            		from.getAccountNumber()+" to "+to.getAccountNumber()+" for "+amount+" rupees is completed");
            return creditTransactionId;  // Return the transaction ID of the credit operation

        } catch (SQLException e) {
            // Handle SQL exceptions and rollback the transaction if needed
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();  // Rollback transaction on error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null; 
            // Return null if transfer failed

        } finally {
            // Ensure connection is set back to auto-commit mode after transaction
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);  // Reset auto-commit to true
                    connection.close();  // Close the connection
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public Long credit(int amount, Long accountNumber, Connection connection) {
    	if(connection==null) {
    		try {
    			connection = DBUtil.getConnection();
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
        Account account = accountRepo.findAccountByNumber(accountNumber);
        if (account != null) {
            // Update the balance
            account.setBalance(account.getBalance() + amount);
            accountRepo.update(account, connection);  // Pass connection to use it in update method

            // Create a credit transaction
            TransactionDetail transaction = new TransactionDetail(0L, accountNumber, new Date(), amount, TransactionType.CREDIT);
            emailService.sendMail(account.getEmailAddress(), "bank@bucks.com"
            		, "Hi "+account.getName()+
            		",your account with Account number "+
            				account.getAccountNumber()+" is credited with "+amount+" rupees");
            return transactionRepo.addTransaction(transaction, connection);  // Pass connection to use it in addTransaction
        }
        return null;  // Return null if credit fails (account not found)
    }

    public Long debit(int amount, Long accountNumber, Connection connection) {
    	if(connection==null) {
    		try {
    			connection = DBUtil.getConnection();
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	Account account = accountRepo.findAccountByNumber(accountNumber);
        if (account != null && account.getBalance() >= amount) {
            // Update the balance
            account.setBalance(account.getBalance() - amount);
            accountRepo.update(account, connection);  // Pass connection to use it in update method

            // Create a debit transaction
            TransactionDetail transaction = new TransactionDetail(0L, accountNumber, new Date(), amount, TransactionType.DEBIT);
            emailService.sendMail(account.getEmailAddress(), "bank@bucks.com"
            		, "Hi "+account.getName()+
            		",your account with Account number "+
            				account.getAccountNumber()+" is debited with "+amount+" rupees");
            return transactionRepo.addTransaction(transaction, connection);  // Pass connection to use it in addTransaction
        }
        return null;  // Return null if debit fails (insufficient balance or account not found)
    }


    public long createNewAccount(Account account) {
        long ans = accountRepo.save(account);
        if(ans!=0) {
        		emailService.sendMail(account.getEmailAddress(), "bank@bucks.com"
        		, "Welcome to the world of Bucks Bank "+account.getName()+", your account is created successfully");
        }
        return ans;
    }

    public void deactivateAccount(Long accountNumber) {
        Connection connection = null;
		try {
			connection = DBUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Account account = accountRepo.findAccountByNumber(accountNumber);
        if (account != null) {
            account.setActive(false);
            accountRepo.update(account, connection);
            emailService.sendMail(account.getEmailAddress(), "bank@bucks.com"
            		, "Hi "+account.getName()+
            		",your account with Account number "+
            				account.getAccountNumber()+" is been deactivated successfully");
        }
    }

    public void activateAccount(Long accountNumber) {
    	Connection connection = null;
		try {
			connection = DBUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Account account = accountRepo.findAccountByNumber(accountNumber);
        if (account != null) {
            account.setActive(true);
            accountRepo.update(account, connection);
            emailService.sendMail(account.getEmailAddress(), "bank@bucks.com"
            		, "Hi "+account.getName()+
            		",your account with Account number "+
            				account.getAccountNumber()+" is been activated successfully");
        }
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAllAccounts();
    }
}

