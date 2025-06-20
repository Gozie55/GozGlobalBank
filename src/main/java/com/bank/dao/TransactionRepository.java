package com.bank.dao;

import com.bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
//    List<Transaction> findByUsername(String username);
    List<Transaction> findBySenderAccountOrReceiverAccount(String senderAccount, String receiverAccount);
}