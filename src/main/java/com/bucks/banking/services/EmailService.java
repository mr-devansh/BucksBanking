package com.bucks.banking.services;

public class EmailService {

    public void sendMail(String toAddress, String fromAddress, String content) {
        // Simulating email sending by printing to console
        System.out.println("Sending email to: " + toAddress);
        System.out.println("From: " + fromAddress);
        System.out.println("Content: " + content);
    }
}
