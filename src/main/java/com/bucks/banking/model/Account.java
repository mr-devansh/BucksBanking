package com.bucks.banking.model;

import java.util.Set;

public class Account {
    // Attributes
    private long accountNumber;
    private String name;
    private boolean isActive;
    private Set<Beneficiary> beneficiaries;
    private Address address;
    private int balance;
    private String emailAddress;

    // Constructor
    public Account(long accountNumber, String name, boolean isActive, Set<Beneficiary> beneficiaries, 
                   Address address, int balance, String emailAddress) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.isActive = isActive;
        this.beneficiaries = beneficiaries;
        this.address = address;
        this.balance = balance;
        this.emailAddress = emailAddress;
    }

    public Account() {
		// TODO Auto-generated constructor stub
	}

	// Getters and Setters
    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(Set<Beneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    // toString method
    @Override
    public String toString() {
        return "\nAccount{" +
                "accountNumber=" + accountNumber +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", \n\tbeneficiaries=" + beneficiaries +
                ", \n\taddress=" + address +
                ", \n\tbalance=" + balance +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
