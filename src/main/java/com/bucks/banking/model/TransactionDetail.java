package com.bucks.banking.model;

import java.util.Date;

public class TransactionDetail {
    // Attributes
    private long transactionId;
    private long accountNumber;
    private Date transactionDate;
    private int amount;
    private TransactionType txType;

    // Constructor
    public TransactionDetail(long transactionId, long accountNumber, Date transactionDate, int amount, TransactionType txType) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.txType = txType;
    }

    // Getters and Setters
    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public TransactionType getTxType() {
        return txType;
    }

    public void setTxType(TransactionType txType) {
        this.txType = txType;
    }

    // toString method
    @Override
    public String toString() {
        return "TransactionDetail{" +
                "transactionId=" + transactionId +
                ", accountNumber=" + accountNumber +
                ", transactionDate=" + transactionDate +
                ", amount=" + amount +
                ", txType=" + txType +
                '}';
    }
}
