/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bank.service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 *
 * @author CHIGOZIE IWUJI
 */
@Service
public class OtpService {

    public final OtpGenerator otpGenerator;
    public final ConcurrentHashMap<String, OtpDetails> otpStore = new ConcurrentHashMap<>();
    
    public OtpService (OtpGenerator otpGenerator){
        this.otpGenerator = otpGenerator;
    }
    
    public String generateOtp(String email){
        String otp = otpGenerator.generateOtp();
        otpStore.put(email, new OtpDetails(otp, LocalDateTime.now().plusMinutes(5)));
        return otp;
    }
    
    public boolean validate(String email, String otp){
        OtpDetails otpDetails = otpStore.get(email);
        if(otpDetails != null && otpDetails.getOtp().equals(otp) 
                && otpDetails.getExpiryTime().isAfter(LocalDateTime.now())){
            otpStore.remove(email);
            return true;
        }
        return false;
    }

    private static class OtpDetails {

        private final String otp;
        private final LocalDateTime expiryTime;

        public OtpDetails(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;

        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }

    }
}
