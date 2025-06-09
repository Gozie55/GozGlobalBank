/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.OtpGenerator to edit this template
 */
package com.bank.service;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

/**
 *
 * @author CHIGOZIE IWUJI
 */
@Component
public class OtpGenerator {
    final SecureRandom sr = new SecureRandom();
    
    public String generateOtp(){
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(sr.nextInt(10));
        }
        return otp.toString();
    }
    
//    public static void main(String[] args){
//        OtpGenerator otp = new OtpGenerator();
//        System.out.println(otp.generateOtp());
//    }
}
