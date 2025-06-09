package com.bank.controller;

import com.bank.dao.CustomerRepository;
import com.bank.dao.TransactionRepository;
import com.bank.entity.Customer;
import com.bank.entity.Transaction;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Component
@Controller
public class AccountController {

    @Autowired
    private CustomerRepository repo;

    @Autowired
    private TransactionRepository transactionRepo;

    @GetMapping("/transfer")
    public String transfer() {
        return "transfer";
    }

    @GetMapping("/transactions")
    public String transactions(HttpSession session, Model model) {
        Object userObj = session.getAttribute("user");

        if (userObj == null) {
            model.addAttribute("error", "You need to log in to view transactions.");
            return "transactions"; // Stay on transactions page with error message
        }

        Customer customer = repo.findByUsername(userObj.toString());

        if (customer == null) {
            model.addAttribute("error", "User not found.");
            return "transactions"; // Stay on transactions page with error message
        }

        List<Transaction> transactions = transactionRepo.findBySenderAccountOrReceiverAccount(
                customer.getAccnumber(), customer.getAccnumber()
        );

        // Debug: Print fetched transactions to the console
        System.out.println("Fetched Transactions: " + transactions);

        // Ensure transactions are not null
        if (transactions == null || transactions.isEmpty()) {
            model.addAttribute("transactions", Collections.emptyList());
        } else {
            model.addAttribute("transactions", transactions);
        }

        return "transactions";
    }

    @GetMapping("/getCustomerDetails")
    public ResponseEntity<Object> getCustomerDetails(@RequestParam String accountNumber) {
        Optional<Customer> customer = repo.findByAccnumber(accountNumber);

        return customer.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("Account not found"));
    }

    @GetMapping("/checkCustomerBalance")
    public ResponseEntity<?> getCustomerBalance(@RequestParam Float amount, HttpSession session) {
        Object balanceObj = session.getAttribute("balance");

        if (balanceObj == null) {
            return ResponseEntity.status(400).body("Balance not found in session");
        }

        try {
            Float balance = Float.parseFloat(balanceObj.toString());

            String amountCheck = amount.toString();
            if (amountCheck.contains("e") || amountCheck.contains("-")) {
                return ResponseEntity.ok("NaN");
            }

            return ResponseEntity.ok(balance);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(500).body("Error retrieving balance");
        }
    }

    // 🔥 Fixed: Now fetches transactions as JSON
    @GetMapping("/transactionDetails")
    @ResponseBody
    public ResponseEntity<List<Transaction>> getUserTransactions(HttpSession session) {
        Object userObj = session.getAttribute("user");

        if (userObj == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        Customer customer = repo.findByUsername(userObj.toString());
        if (customer == null) {
            return ResponseEntity.status(404).build(); // User not found
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
            @RequestParam String pin, // ✅ Added PIN validation
            HttpSession session) {

        // 🔍 Fetch the sender's details using session username
        Customer customer = repo.findByUsername(session.getAttribute("user").toString());

        Optional<Customer> senderOpt = repo.findByAccnumber(customer.getAccnumber());
        Optional<Customer> receiverOpt = repo.findByAccnumber(accountNumber);

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return ResponseEntity.status(404).body("❌ Invalid sender or receiver account");
        }

        Customer sender = senderOpt.get();
        Customer receiver = receiverOpt.get();

        // ✅ Ensure correct PIN validation
        if (!String.valueOf(sender.getPin()).equals(pin)) {
            return ResponseEntity.status(403).body("❌ Incorrect PIN. Please try again.");
        }

        // ✅ Check if sender has enough balance
        if (sender.getBalance() < amount) {
            return ResponseEntity.status(400).body("❌ Insufficient funds.");
        }

        // 💰 Deduct amount from sender and add to receiver
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        repo.save(sender);
        repo.save(receiver);

        // ✅ Save transaction records
        Transaction sentTransaction = new Transaction(sender.getUsername(), sender.getAccnumber(), receiver.getAccnumber(), amount, Transaction.TransactionType.SENT);
        Transaction receivedTransaction = new Transaction(receiver.getUsername(), sender.getAccnumber(), receiver.getAccnumber(), amount, Transaction.TransactionType.RECEIVED);

        transactionRepo.save(sentTransaction);
        transactionRepo.save(receivedTransaction);

        return ResponseEntity.ok("✅ Transfer successful! New balance: ₦" + sender.getBalance());
    }

    @GetMapping("/account-balance")
    public ResponseEntity<?> getBalance(HttpSession session) {
        Object sessionUser = session.getAttribute("user");

        if (sessionUser == null) {
            return ResponseEntity.status(401).body("Session expired. Please log in again.");
        }

        Customer customer = repo.findByUsername(sessionUser.toString());

        if (customer == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        // Fetch latest balance from DB
        Optional<Customer> updatedCustomerOpt = repo.findById(customer.getId());

        if (updatedCustomerOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        Customer updatedCustomer = updatedCustomerOpt.get();

        // Store balance in session
        session.setAttribute("balance", updatedCustomer.getBalance());

        return ResponseEntity.ok(updatedCustomer.getBalance()); // Return latest balance
    }
    
    
}
