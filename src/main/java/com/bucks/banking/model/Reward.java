package com.bucks.banking.model;
public class Reward {
    // Attributes
    private long rewardConfirmationNumber;
    private int rewardAmount;
    private long accountNumber;

    // Constructor
    public Reward(long rewardConfirmationNumber, int rewardAmount, long accountNumber) {
        this.rewardConfirmationNumber = rewardConfirmationNumber;
        this.rewardAmount = rewardAmount;
        this.accountNumber = accountNumber;
    }

    // Getters and Setters
    public long getRewardConfirmationNumber() {
        return rewardConfirmationNumber;
    }

    public void setRewardConfirmationNumber(long rewardConfirmationNumber) {
        this.rewardConfirmationNumber = rewardConfirmationNumber;
    }

    public int getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(int rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    // toString method
    @Override
    public String toString() {
        return "Reward{" +
                "rewardConfirmationNumber=" + rewardConfirmationNumber +
                ", rewardAmount=" + rewardAmount +
                ", accountNumber=" + accountNumber +
                '}';
    }
}
