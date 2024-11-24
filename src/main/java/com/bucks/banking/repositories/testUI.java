package com.bucks.banking.repositories;

import com.bucks.banking.model.Account;
import com.bucks.banking.model.Address;
import com.bucks.banking.model.Beneficiary;
import com.bucks.banking.model.Reward;
import com.bucks.banking.services.DBUtil;

import java.util.*;

public class testUI {

    public static void main(String[] args) {
        // Initialize the repository for account and reward
        JdbcAccountRepositoryImpl accountRepo = new JdbcAccountRepositoryImpl();
        JdbcRewardRepositoryImpl rewardRepo = new JdbcRewardRepositoryImpl();

        // Create dummy data for accounts
        Account account1 = createDummyAccount((long) 10001, "Shiva", "Bangalore", "India", 10000, "sivaprasad.valluru@gmail.com");
        Account account2 = createDummyAccount((long) 10002, "Prasad", "Hyderabad", "India", 20000, "siva@gmail.com");

        // Save accounts to the database
//        accountRepo.save(account1);
//        accountRepo.save(account2);

        // Find account by account number
        Account foundAccount = accountRepo.findAccountByNumber(10001L);
        System.out.println("Found Account: " + foundAccount);

        // Add rewards to accounts
        Reward reward1 = new Reward(1L, 500, account1.getAccountNumber());
        Reward reward2 = new Reward(2L, 200, account1.getAccountNumber());
        Reward reward3 = new Reward(3L, 1000, account2.getAccountNumber());

        // Add rewards using the reward repository
        rewardRepo.addReward(reward1);
        rewardRepo.addReward(reward2);
        rewardRepo.addReward(reward3);

        // Get total reward amount for account1 and account2
        int totalRewardAccount1 = rewardRepo.getTotalRewardAmount(account1.getAccountNumber());
        int totalRewardAccount2 = rewardRepo.getTotalRewardAmount(account2.getAccountNumber());
        
        System.out.println("Total Reward for Account 10001: " + totalRewardAccount1);
        System.out.println("Total Reward for Account 10002: " + totalRewardAccount2);

        // Get all rewards for account1
        List<Reward> allRewardsAccount1 = rewardRepo.getAllRewardsForAccount(account1.getAccountNumber());
        System.out.println("All Rewards for Account 10001: " + allRewardsAccount1);

        // Get all rewards for account2
        List<Reward> allRewardsAccount2 = rewardRepo.getAllRewardsForAccount(account2.getAccountNumber());
        System.out.println("All Rewards for Account 10002: " + allRewardsAccount2);

        // Find all accounts
        List<Account> allAccounts = accountRepo.findAllAccounts();
        System.out.println("All Accounts: " + allAccounts);

        // Update an account
        account1.setBalance(15000); // Updating balance of account1
        accountRepo.update(account1);
        System.out.println("Updated Account: " + account1);

        // Delete an account (dummy, since method isn't implemented in the repo)
        //accountRepo.delete(account2);

        foundAccount = accountRepo.findAccountByNumber(10001L);
        System.out.println("Found Account after update: " + foundAccount);
    }

    // Helper method to create dummy accounts with beneficiaries and address
    private static Account createDummyAccount(Long accountNumber, String name, String city, String country, int balance, String email) {
        Address address = new Address(city, country);
        Set<Beneficiary> beneficiaries = new HashSet<Beneficiary>();
        beneficiaries.add(new Beneficiary((long) ((Math.random()*10*Math.random())+ 123456789L), "John Doe"));
        beneficiaries.add(new Beneficiary((long) ((Math.random()*10*Math.random())+ 123456784L), "Jane Doe"));
        return new Account(accountNumber, name, true, beneficiaries, address, balance, email);
    }
}
