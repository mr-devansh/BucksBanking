package com.bucks.banking.repositories;

import java.sql.Connection;
import java.util.List;

import com.bucks.banking.model.Account;

public interface AccountRepository {
	Account findAccountByNumber(Long accountNumber);
	List<Account> findAllAccounts();
	long save(Account account);
	void update(Account account, Connection connection);
	void delete(Account account);

}
