package com.bucks.banking.services;


import com.bucks.banking.model.*;
import com.bucks.banking.repositories.*;
import com.bucks.banking.services.*;

import java.util.*;

public class Main {

    private static BankService bankService;
    private static RewardRepository rewardRepo;
    private static JdbcTransactionRepositoryImpl transactionRepo;
    private static EmailService emailService;
    private static Scanner scanner;

    public static void main(String[] args) {
        // Initialize repositories and services
    	JdbcAccountRepositoryImpl accountRepo = new JdbcAccountRepositoryImpl();
        transactionRepo = new JdbcTransactionRepositoryImpl();
        rewardRepo = new JdbcRewardRepositoryImpl();
        emailService = new EmailService();
        bankService = new BankServiceImpl(accountRepo, transactionRepo);

        scanner = new Scanner(System.in);

        while (true) {
            // Display menu
            displayMenu();
            int choice = getChoice();
            handleChoice(choice);
        }
    }

    private static void displayMenu() {
        System.out.println("\n--- Banking System Menu ---");
        System.out.println("1. Create New Account");
        System.out.println("2. Debit Amount");
        System.out.println("3. Credit Amount");
        System.out.println("4. Transfer Amount");
        System.out.println("5. Add Reward");
        System.out.println("6. Fetch All Accounts");
        System.out.println("7. Deactivate Account");
        System.out.println("8. Activate Account");
        System.out.println("9. Fetch Transactions for Account");
        System.out.println("10. Fetch Rewards for Account");
        System.out.println("11. Send Email Notification");
        System.out.println("12. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getChoice() {
        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
        return choice;
    }

    private static void handleChoice(int choice) {
        switch (choice) {
            case 1:
                createNewAccount();
                break;
            case 2:
                debitAmount();
                break;
            case 3:
                creditAmount();
                break;
            case 4:
                transferAmount();
                break;
            case 5:
                addReward();
                break;
            case 6:
                fetchAllAccounts();
                break;
            case 7:
                deactivateAccount();
                break;
            case 8:
                activateAccount();
                break;
            case 9:
                fetchTransactionsForAccount();
                break;
            case 10:
                fetchRewardsForAccount();
                break;
            case 11:
                sendEmailNotification();
                break;
            case 12:
                System.out.println("Exiting the application. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }

    private static void createNewAccount() {
    	
    	Set<Beneficiary> beneficiaries = new HashSet<Beneficiary>();
    	
        System.out.println("\nEnter account details: ");
        System.out.print("Account Number: ");
        long accountNumber = Long.parseLong(scanner.nextLine());
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("City: ");
        String city = scanner.nextLine();
        System.out.print("Country: ");
        String country = scanner.nextLine();
        System.out.print("Balance: ");
        int balance = Integer.parseInt(scanner.nextLine());
        System.out.print("Email: ");
        String email = scanner.nextLine();

        Account account = new Account(accountNumber, name, true, beneficiaries, new Address(city, country), balance, email);
        bankService.createNewAccount(account);
        System.out.println("Account created successfully.");
    }

    private static void debitAmount() {
        System.out.print("\nEnter account number: ");
        long accountNumber = Long.parseLong(scanner.nextLine());
        System.out.print("Enter amount to debit: ");
        int amount = Integer.parseInt(scanner.nextLine());

        Long transactionId = bankService.debit(amount, accountNumber);
        if (transactionId != null) {
            System.out.println("Debit successful. Transaction ID: " + transactionId);
        } else {
            System.out.println("Debit failed. Insufficient balance or invalid account.");
        }
    }

    private static void creditAmount() {
        System.out.print("\nEnter account number: ");
        long accountNumber = Long.parseLong(scanner.nextLine());
        System.out.print("Enter amount to credit: ");
        int amount = Integer.parseInt(scanner.nextLine());

        Long transactionId = bankService.credit(amount, accountNumber);
        if (transactionId != null) {
            System.out.println("Credit successful. Transaction ID: " + transactionId);
        } else {
            System.out.println("Credit failed. Invalid account.");
        }
    }

    private static void transferAmount() {
        System.out.print("\nEnter from account number: ");
        long fromAccount = Long.parseLong(scanner.nextLine());
        System.out.print("Enter to account number: ");
        long toAccount = Long.parseLong(scanner.nextLine());
        System.out.print("Enter amount to transfer: ");
        int amount = Integer.parseInt(scanner.nextLine());

        Long transactionId = bankService.transfer(fromAccount, toAccount, amount);
        if (transactionId != null) {
            System.out.println("Transfer successful. Transaction ID: " + transactionId);
        } else {
            System.out.println("Transfer failed. Check accounts or balance.");
        }
    }

    private static void addReward() {
        System.out.print("\nEnter account number: ");
        long accountNumber = Long.parseLong(scanner.nextLine());
        System.out.print("Enter reward amount: ");
        int rewardAmount = Integer.parseInt(scanner.nextLine());

        Reward reward = new Reward(0L, rewardAmount, accountNumber);
        rewardRepo.addReward(reward);
        System.out.println("Reward added successfully.");
    }

    private static void fetchAllAccounts() {
        System.out.println("\nFetching all accounts...");
        List<Account> allAccounts = bankService.getAllAccounts();
        for (Account account : allAccounts) {
            System.out.println(account);
        }
    }

    private static void deactivateAccount() {
        System.out.print("\nEnter account number to deactivate: ");
        long accountNumber = Long.parseLong(scanner.nextLine());
        bankService.deactivateAccount(accountNumber);
        System.out.println("Account deactivated successfully.");
    }

    private static void activateAccount() {
        System.out.print("\nEnter account number to activate: ");
        long accountNumber = Long.parseLong(scanner.nextLine());
        bankService.activateAccount(accountNumber);
        System.out.println("Account activated successfully.");
    }

    private static void fetchTransactionsForAccount() {
        System.out.print("\nEnter account number to fetch transactions: ");
        long accountNumber = Long.parseLong(scanner.nextLine());

        List<TransactionDetail> transactions = transactionRepo.getAllTransactionDetailsByAccountNumber(accountNumber);
        for (TransactionDetail transaction : transactions) {
            System.out.println(transaction);
        }
    }

    private static void fetchRewardsForAccount() {
        System.out.print("\nEnter account number to fetch rewards: ");
        long accountNumber = Long.parseLong(scanner.nextLine());
        System.out.println("Total Account reward: "+rewardRepo.getTotalRewardAmount(accountNumber));
        List<Reward> rewards = rewardRepo.getAllRewardsForAccount(accountNumber);
        for (Reward reward : rewards) {
            System.out.println(reward);
        }
    }

    private static void sendEmailNotification() {
        System.out.print("\nEnter recipient email address: ");
        String toAddress = scanner.nextLine();
        System.out.print("Enter content of the email: ");
        String content = scanner.nextLine();

        emailService.sendMail(toAddress, "bank@bucks.com", content);
    }
}
