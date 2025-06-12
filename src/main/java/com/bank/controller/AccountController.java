package com.bank.controller;

import com.bank.dao.CustomerRepository;
import com.bank.dao.TransactionRepository;
import com.bank.entity.Customer;
import com.bank.entity.Transaction;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    private CustomerRepository repo;

    @Autowired
    private TransactionRepository transactionRepo;

    @GetMapping("/transfer")
    public ResponseEntity<?> transfer() {
        return ResponseEntity.ok(Map.of("message", "Transfer endpoint accessed"));
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> transactions(HttpSession session) {
        Object userObj = session.getAttribute("user");

        if (userObj == null) {
            return ResponseEntity.status(401).body(Map.of("error", "You need to log in to view transactions."));
        }

        Customer customer = repo.findByUsername(userObj.toString());
        if (customer == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found."));
        }

        List<Transaction> transactions = transactionRepo.findBySenderAccountOrReceiverAccount(
            customer.getAccnumber(), customer.getAccnumber()
        );

        return ResponseEntity.ok(transactions != null ? transactions : Collections.emptyList());
    }

    @GetMapping("/getCustomerDetails")
public ResponseEntity<?> getCustomerDetails(@RequestParam String accountNumber) {
    Optional<Customer> customer = repo.findByAccnumber(accountNumber);

    if (customer.isPresent()) {
        return ResponseEntity.ok(customer.get());
    } else {
        return ResponseEntity.status(404).body(Map.of("error", "Account not found"));
    }
}


    @GetMapping("/checkCustomerBalance")
    public ResponseEntity<?> getCustomerBalance(@RequestParam Float amount, HttpSession session) {
        Object balanceObj = session.getAttribute("balance");
        if (balanceObj == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Balance not found in session"));
        }

        try {
            Float balance = Float.parseFloat(balanceObj.toString());
            String amountCheck = amount.toString();
            if (amountCheck.contains("e") || amountCheck.contains("-")) {
                return ResponseEntity.ok(Map.of("balance", "NaN"));
            }
            return ResponseEntity.ok(Map.of("balance", balance));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error retrieving balance"));
        }
    }

    @GetMapping("/transactionDetails")
    public ResponseEntity<?> getUserTransactions(HttpSession session) {
        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            return ResponseEntity.status(401).body(Map.of("error", "User not logged in"));
        }

        Customer customer = repo.findByUsername(userObj.toString());
        if (customer == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        List<Transaction> transactions = transactionRepo.findBySenderAccountOrReceiverAccount(
            customer.getAccnumber(), customer.getAccnumber()
        );

        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/TransferMoney")
    @Transactional
    public ResponseEntity<?> transferMoney(@RequestParam String accountNumber,
                                           @RequestParam float amount,
                                           @RequestParam String pin,
                                           HttpSession session) {

        Customer customer = repo.findByUsername(session.getAttribute("user").toString());
        Optional<Customer> senderOpt = repo.findByAccnumber(customer.getAccnumber());
        Optional<Customer> receiverOpt = repo.findByAccnumber(accountNumber);

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "❌ Invalid sender or receiver account"));
        }

        Customer sender = senderOpt.get();
        Customer receiver = receiverOpt.get();

        if (!String.valueOf(sender.getPin()).equals(pin)) {
            return ResponseEntity.status(403).body(Map.of("error", "❌ Incorrect PIN. Please try again."));
        }

        if (sender.getBalance() < amount) {
            return ResponseEntity.status(400).body(Map.of("error", "❌ Insufficient funds."));
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        repo.save(sender);
        repo.save(receiver);

        Transaction sentTransaction = new Transaction(sender.getUsername(), sender.getAccnumber(), receiver.getAccnumber(), amount, Transaction.TransactionType.SENT);
        Transaction receivedTransaction = new Transaction(receiver.getUsername(), sender.getAccnumber(), receiver.getAccnumber(), amount, Transaction.TransactionType.RECEIVED);

        transactionRepo.save(sentTransaction);
        transactionRepo.save(receivedTransaction);

        return ResponseEntity.ok(Map.of("message", "✅ Transfer successful!", "newBalance", sender.getBalance()));
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(HttpSession session) {
        Object sessionUser = session.getAttribute("user");
        if (sessionUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Session expired. Please log in again."));
        }

        Customer customer = repo.findByUsername(sessionUser.toString());
        if (customer == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        Optional<Customer> updatedCustomerOpt = repo.findById(customer.getId());
        if (updatedCustomerOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        Customer updatedCustomer = updatedCustomerOpt.get();
        session.setAttribute("balance", updatedCustomer.getBalance());

        return ResponseEntity.ok(Map.of("balance", updatedCustomer.getBalance()));
    }
}
