package com.bucks.banking.repositories;

import java.util.List;

import com.bucks.banking.model.Account;

public interface AccountRepository {
	Account findAccountByNumber(Long accountNumber);
	List<Account> findAllAccounts();
	void save(Account account);
	void update(Account account);
	void delete(Account account);

}
