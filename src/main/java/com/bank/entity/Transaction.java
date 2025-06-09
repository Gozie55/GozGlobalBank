package com.bank.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String senderAccount;
    private String receiverAccount;
    private float amount;
    private LocalDateTime timestamp;
    
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // SENT or RECEIVED

    public enum TransactionType {
        SENT, RECEIVED
    }

    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(String username, String senderAccount, String receiverAccount, float amount, TransactionType transactionType) {
        this.username = username;
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getUsername() {return username; }
    public String getSenderAccount() { return senderAccount; }
    public String getReceiverAccount() { return receiverAccount; }
    public float getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionType getTransactionType() { return transactionType; }

    public void setUsername(String username) {this.username = username; }
    public void setSenderAccount(String senderAccount) { this.senderAccount = senderAccount; }
    public void setReceiverAccount(String receiverAccount) { this.receiverAccount = receiverAccount; }
    public void setAmount(float amount) { this.amount = amount; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
}