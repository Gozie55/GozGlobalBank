/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bank.service;

import com.bank.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */
@Service
public class JWTService {
private static final Logger logger = LoggerFactory.getLogger(JWTService.class);
    private String secretKey = "SkyeBankKeys";

    public JWTService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(Customer user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("balance", user.getBalance()); // Include balance in token

        String token = Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 12))
                .and()
                .signWith(getKey()).compact();

    // Log the token
        System.out.println(token);
    logger.info("Generated JWT Token: {}", token);
    logger.debug(token);
        return token;
//        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkxpZ2h0IiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyNTkwMjJ9.MgA5H0DAyam3ITr0BIvkudkDQ2mj8WedZhTwWO2D4-k";
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claim = extractAllClaims(token);
        return claimResolver.apply(claim);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build().parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userdetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userdetails.getUsername()) && !isTokenExpired(token));

    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public static void main(String[] args) {
        JWTService jwtService = new JWTService();
        
        // Create a sample customer object
        Customer testCustomer = new Customer();
        testCustomer.setUsername("testUser");
        testCustomer.setBalance(1000.00f);
        

//         Generate JWT Token
        String token = jwtService.generateToken(testCustomer);

        // Print Token
//        System.out.println("Generated Token: " + token);
    }

}