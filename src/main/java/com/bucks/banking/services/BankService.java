package com.bucks.banking.services;

import com.bucks.banking.model.Account;

import java.sql.Connection;
import java.util.List;

public interface BankService {

    Long transfer(Long fromAccount, Long toAccount, int amount);

    Long debit(int amount, Long accountNumber, Connection connection);

    Long credit(int amount, Long accountNumber, Connection connection);

    long createNewAccount(Account account);

    void deactivateAccount(Long accountNumber);

    void activateAccount(Long accountNumber);

    List<Account> getAllAccounts();
}
