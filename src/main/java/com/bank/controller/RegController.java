package com.bank.controller;

import com.bank.dao.CustomerRepository;
import com.bank.entity.Customer;
import com.bank.service.EmailService;
import com.bank.service.OtpService;
import com.bank.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class RegController {

    @Autowired
    private CustomerRepository repo;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    // ✅ Step 1: Start Registration
    @PostMapping("/register")
    public Map<String, Object> registerCustomer(@RequestBody Customer cust) {
        String email = cust.getEmail();
        String response = userService.checkEmail(email);

        if (response.equals("Email Already Exists")) {
            return Map.of("status", "error", "message", "Email already exists");
        }

        // ✅ Generate and send OTP
        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);

        // ✅ Temporarily store user in session
        session.setAttribute("pendingUser", cust);
        session.setAttribute("pendingEmail", email);

        return Map.of("status", "otp_sent", "message", "OTP sent to " + email);
    }

    // ✅ Step 2: Validate OTP
    @PostMapping("/validate-otp")
    public Map<String, Object> validateOtp(@RequestParam String otp) {
        String email = (String) session.getAttribute("pendingEmail");
        Customer cust = (Customer) session.getAttribute("pendingUser");

        if (email == null || cust == null) {
            return Map.of("status", "error", "message", "Session expired. Please register again.");
        }

        boolean isValid = otpService.validate(email, otp);

        if (!isValid) {
            return Map.of("status", "error", "message", "Invalid OTP");
        }

        // ✅ Create account number from phone
        String phone = String.valueOf(cust.getPhone());
        if (phone != null && phone.startsWith("0")) {
            cust.setAccnumber(phone.substring(1));
        } else {
            cust.setAccnumber(phone);
        }

        // ✅ Save user (without PIN yet)
        userService.registerUser(cust);

        // ✅ Save email for PIN confirmation step
        session.setAttribute("verifiedEmail", email);

        return Map.of("status", "otp_verified", "message", "OTP verified. Proceed to set PIN.");
    }

    // ✅ Step 3: Set PIN
    @PostMapping("/set-pin")
    public Map<String, Object> confirmPin(@RequestParam int pin) {
        String email = (String) session.getAttribute("verifiedEmail");

        if (email == null) {
            return Map.of("status", "error", "message", "Session expired. Please register again.");
        }

        Optional<Customer> customerOpt = repo.findByEmail(email);

        if (customerOpt.isEmpty()) {
            return Map.of("status", "error", "message", "User not found");
        }

        Customer customer = customerOpt.get();
        customer.setPin(pin); // ❗ Ideally hash the PIN before saving
        repo.save(customer);

        // ✅ Clean up session
        session.removeAttribute("verifiedEmail");
        session.removeAttribute("pendingUser");
        session.removeAttribute("pendingEmail");

        return Map.of("status", "success", "message", "PIN set successfully. You can now log in.");
    }

}
